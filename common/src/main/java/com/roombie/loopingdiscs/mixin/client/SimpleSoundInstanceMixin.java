package com.roombie.loopingdiscs.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.roombie.loopingdiscs.client.LoopingJukeboxSoundInstance;

import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.Vec3;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SimpleSoundInstance.class)
public abstract class SimpleSoundInstanceMixin {

    /**
     * Swaps vanilla's one-shot jukebox sound for a looping one that stops itself when it is
     * no longer the sound registered for its jukebox.
     */
    @ModifyReturnValue(method = "forJukeboxSong", at = @At("RETURN"))
    private static SimpleSoundInstance loopingDiscs$makeJukeboxSoundLoop(
            SimpleSoundInstance original,
            SoundEvent sound,
            Vec3 position
    ) {
        return new LoopingJukeboxSoundInstance(sound, position);
    }
}