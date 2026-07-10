package com.gtnewhorizon.gtnhlib.world.observer;

import com.gtnewhorizon.gtnhlib.util.VoxelAABB;

class WorldObserverWrapper implements WorldObserverHandle {

    final VoxelAABB aabb;
    final WorldObserver observer;

    public WorldObserverWrapper(VoxelAABB aabb, WorldObserver observer) {
        this.aabb = aabb;
        this.observer = observer;
    }
}
