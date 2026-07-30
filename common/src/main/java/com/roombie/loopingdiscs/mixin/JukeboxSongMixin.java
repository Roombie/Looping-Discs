package com.roombie.loopingdiscs.mixin;

import net.minecraft.world.item.JukeboxSong;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(JukeboxSong.class)
public abstract class JukeboxSongMixin {

    /**
     * Keeps the jukebox playing while the disc is kept inside.
     */
    @Inject(
            method = "hasFinished",
            at = @At("HEAD"),
            cancellable = true
    )
    private void loopingDiscs$neverFinish(
            long ticksSinceSongStarted,
            CallbackInfoReturnable<Boolean> cir
    ) {
        cir.setReturnValue(false);
    }
}