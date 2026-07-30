package com.roombie.loopingdiscs.mixin.client;

import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SimpleSoundInstance.class)
public abstract class SimpleSoundInstanceMixin {

    @Inject(
            method = "forJukeboxSong",
            at = @At("RETURN")
    )
    private static void loopingDiscs$makeJukeboxSoundLoop(
            SoundEvent soundEvent,
            Vec3 position,
            CallbackInfoReturnable<SimpleSoundInstance> cir
    ) {
        SimpleSoundInstance soundInstance = cir.getReturnValue();

        ((AbstractSoundInstanceAccessor) (Object) soundInstance)
                .loopingDiscs$setLooping(true);
    }
}