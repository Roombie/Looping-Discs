package com.roombie.loopingdiscs.mixin.client;

import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelEventHandler;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.item.JukeboxSong;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Ignores the periodic re-announcement sent by {@code JukeboxSongPlayerMixin} when this client is
 * already playing that jukebox, so the repeat is inaudible to anyone in range.
 *
 * <p>A genuine disc change always sends the stop event first, which clears the map entry, so this
 * can only ever suppress a repeat and never a real song change.
 */
@Mixin(LevelEventHandler.class)
public abstract class LevelEventHandlerMixin {

    @Shadow
    @Final
    private ClientLevel level;

    @Shadow
    @Final
    private Map<BlockPos, SoundInstance> playingJukeboxSongs;

    @Inject(method = "playJukeboxSong", at = @At("HEAD"), cancellable = true)
    private void loopingDiscs$ignoreRepeatedAnnouncement(
            Holder<JukeboxSong> songHolder,
            BlockPos pos,
            CallbackInfo ci
    ) {
        SoundInstance current = this.playingJukeboxSongs.get(pos);

        if (current == null || !Minecraft.getInstance().getSoundManager().isActive(current)) {
            return;
        }

        // Still tell nearby entities, so an allay that has just wandered over starts dancing.
        ((LevelEventHandlerAccessor) (Object) this)
                .loopingDiscs$notifyNearbyEntities(this.level, pos, true);

        ci.cancel();
    }
}