package com.gtnewhorizon.gtnhlib.world.observer;

import net.minecraft.world.WorldServer;

public interface WorldObserver {

    void onBlockChanged(WorldServer world, int x, int y, int z);

}
