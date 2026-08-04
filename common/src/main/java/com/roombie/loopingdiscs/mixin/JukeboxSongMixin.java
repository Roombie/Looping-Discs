package com.roombie.loopingdiscs.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.item.JukeboxSong;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(JukeboxSong.class)
public abstract class JukeboxSongMixin {

    /**
     * Keeps the jukebox playing while the disc is kept inside.
     *
     * <p>Uses {@link ModifyReturnValue} rather than a cancellable HEAD inject so that
     * other mods wrapping {@code hasFinished} can still see and adjust the value
     * instead of being silently pre-empted.
     */
    @ModifyReturnValue(method = "hasFinished", at = @At("RETURN"))
    private boolean loopingDiscs$neverFinish(boolean original) {
        return false;
    }
}
