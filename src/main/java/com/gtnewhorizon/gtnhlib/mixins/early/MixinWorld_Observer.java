package com.gtnewhorizon.gtnhlib.mixins.early;

import net.minecraft.world.WorldServer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import com.gtnewhorizon.gtnhlib.world.observer.Observer_WorldExt;
import com.gtnewhorizon.gtnhlib.world.observer.WorldObserverManager;

@Mixin(WorldServer.class)
public class MixinWorld_Observer implements Observer_WorldExt {

    @Unique
    private WorldObserverManager gtnhlib$observers;

    @SuppressWarnings("DataFlowIssue")
    @Override
    public WorldObserverManager gtnhlib$getOrCreateManager() {
        if (gtnhlib$observers == null) {
            gtnhlib$observers = new WorldObserverManager((WorldServer) (Object) this);
            ((WorldServer) (Object) this).addWorldAccess(gtnhlib$observers);
        }

        return gtnhlib$observers;
    }
}
