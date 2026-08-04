package com.roombie.loopingdiscs.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.item.JukeboxSongPlayer;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Re-announces the song that is currently playing, so that players who were not nearby when it
 * started can still hear it.
 *
 * <p>Vanilla fires the "play jukebox song" level event exactly once, to whoever happens to be in
 * range at that moment. Normally that is harmless: the song ends by itself and the jukebox resets.
 * Because this mod stops the song from ever finishing, a player who joins, changes dimension, or
 * simply walks far enough for the chunk to unload would otherwise be left with a jukebox that is
 * permanently silent and cannot be restarted without taking the disc out by hand.
 *
 * <p>Listeners already in range ignore the repeat: see {@code LevelEventHandlerMixin}.
 */
@Mixin(JukeboxSongPlayer.class)
public abstract class JukeboxSongPlayerMixin {

    /** How often the currently playing song is re-announced. */
    private static final long LOOPING_DISCS$REBROADCAST_INTERVAL_TICKS = 100L;

    /** Vanilla's "a jukebox song started playing" level event. */
    private static final int LOOPING_DISCS$PLAY_JUKEBOX_SONG_EVENT = 1010;

    @Shadow
    private long ticksSinceSongStarted;

    @Shadow
    private Holder<JukeboxSong> song;

    @Shadow
    @Final
    private BlockPos blockPos;

    @Inject(method = "tick", at = @At("TAIL"))
    private void loopingDiscs$rebroadcastSong(
            LevelAccessor level,
            BlockState blockState,
            CallbackInfo ci
    ) {
        if (this.song == null || level.isClientSide()) {
            return;
        }

        if (this.ticksSinceSongStarted % LOOPING_DISCS$REBROADCAST_INTERVAL_TICKS != 0L) {
            return;
        }

        int songId = level.registryAccess()
                .lookupOrThrow(Registries.JUKEBOX_SONG)
                .getId(this.song.value());

        level.levelEvent(null, LOOPING_DISCS$PLAY_JUKEBOX_SONG_EVENT, this.blockPos, songId);
    }
}