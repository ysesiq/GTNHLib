package com.gtnewhorizon.gtnhlib.world.observer;

import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.IWorldAccess;
import net.minecraft.world.WorldServer;

import com.gtnewhorizon.gtnhlib.datastructs.space.HashMap3D;
import com.gtnewhorizon.gtnhlib.util.VoxelAABB;

public class WorldObserverManager implements IWorldAccess {

    public static WorldObserverManager getManager(WorldServer world) {
        return ((Observer_WorldExt) world).gtnhlib$getOrCreateManager();
    }

    private final WorldServer world;

    private final HashMap3D<ArrayList<WorldObserverWrapper>> observers = new HashMap3D<>();

    private boolean cached = false;
    private int cacheX, cacheY, cacheZ;
    private ArrayList<WorldObserverWrapper> cachedSection = null;

    public WorldObserverManager(WorldServer world) {
        this.world = world;
    }

    public WorldObserverHandle watch(VoxelAABB aabb, WorldObserver observer) {
        cached = false;

        int cminX = aabb.minX() >> 4;
        int cminY = aabb.minY() >> 4;
        int cminZ = aabb.minZ() >> 4;

        int cmaxX = aabb.maxX() >> 4;
        int cmaxY = aabb.maxY() >> 4;
        int cmaxZ = aabb.maxZ() >> 4;

        WorldObserverWrapper wrapper = new WorldObserverWrapper(aabb.clone(), observer);

        for (int x = cminX; x <= cmaxX; x++) {
            for (int y = cminY; y <= cmaxY; y++) {
                for (int z = cminZ; z <= cmaxZ; z++) {
                    observers.computeIfAbsent(x, y, z, ($x, $y, $z) -> new ArrayList<>()).add(wrapper);
                }
            }
        }

        return wrapper;
    }

    public void unwatch(WorldObserverHandle handle) {
        cached = false;

        WorldObserverWrapper wrapper = (WorldObserverWrapper) handle;

        VoxelAABB aabb = wrapper.aabb;

        int cminX = aabb.minX() >> 4;
        int cminY = aabb.minY() >> 4;
        int cminZ = aabb.minZ() >> 4;

        int cmaxX = aabb.maxX() >> 4;
        int cmaxY = aabb.maxY() >> 4;
        int cmaxZ = aabb.maxZ() >> 4;

        for (int x = cminX; x <= cmaxX; x++) {
            for (int y = cminY; y <= cmaxY; y++) {
                for (int z = cminZ; z <= cmaxZ; z++) {
                    var list = observers.get(x, y, z);

                    if (list == null) continue;

                    list.remove(wrapper);

                    if (list.isEmpty()) {
                        observers.remove(x, y, z);
                    }
                }
            }
        }
    }

    @Override
    public void markBlockForUpdate(int x, int y, int z) {
        int cX = x >> 4;
        int cY = y >> 4;
        int cZ = z >> 4;

        ArrayList<WorldObserverWrapper> section;

        if (cached && cacheX == cX && cacheY == cY && cacheZ == cZ) {
            section = cachedSection;
        } else {
            section = observers.get(cX, cY, cZ);

            cached = true;
            cacheX = cX;
            cacheY = cY;
            cacheZ = cZ;
            cachedSection = section;
        }

        if (section != null) {
            int len = section.size();

            //noinspection ForLoopReplaceableByForEach
            for (int i = 0; i < len; i++) {
                WorldObserverWrapper wrapper = section.get(i);

                if (wrapper.aabb.contains(x, y, z)) {
                    wrapper.observer.onBlockChanged(world, x, y, z);
                }
            }
        }
    }

    //<editor-fold desc="stub methods">

    @Override
    public void markBlockForRenderUpdate(int x, int y, int z) {

    }

    @Override
    public void markBlockRangeForRenderUpdate(
        int minX, int minY, int minZ,
        int maxX, int maxY, int maxZ
    ) {

    }

    @Override
    public void playSound(String soundName, double x, double y, double z, float volume, float pitch) {

    }

    @Override
    public void playSoundToNearExcept(
        EntityPlayer p_85102_1_, String p_85102_2_, double p_85102_3_, double p_85102_5_, double p_85102_7_,
        float p_85102_9_, float p_85102_10_
    ) {

    }

    @Override
    public void spawnParticle(
        String p_72708_1_, double p_72708_2_, double p_72708_4_, double p_72708_6_, double p_72708_8_,
        double p_72708_10_, double p_72708_12_
    ) {

    }

    @Override
    public void onEntityCreate(Entity p_72703_1_) {

    }

    @Override
    public void onEntityDestroy(Entity p_72709_1_) {

    }

    @Override
    public void playRecord(String p_72702_1_, int p_72702_2_, int p_72702_3_, int p_72702_4_) {

    }

    @Override
    public void broadcastSound(int p_82746_1_, int p_82746_2_, int p_82746_3_, int p_82746_4_, int p_82746_5_) {

    }

    @Override
    public void playAuxSFX(
        EntityPlayer p_72706_1_, int p_72706_2_, int p_72706_3_, int p_72706_4_, int p_72706_5_,
        int p_72706_6_
    ) {

    }

    @Override
    public void destroyBlockPartially(
        int p_147587_1_, int p_147587_2_, int p_147587_3_, int p_147587_4_,
        int p_147587_5_
    ) {

    }

    @Override
    public void onStaticEntitiesChanged() {

    }

    //</editor-fold>
}
