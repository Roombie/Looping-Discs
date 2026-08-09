package com.roombie.loopingdiscs.loop;

import com.roombie.loopingdiscs.config.LoopingDiscsConfig;
import com.roombie.loopingdiscs.mixin.JukeboxBlockEntityAccessor;
import com.roombie.loopingdiscs.mixin.JukeboxSongPlayerAccessor;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.game.ClientboundLevelEventPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.item.JukeboxSongPlayer;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;

/**
 * The two ways a client is told that a jukebox is playing.
 *
 * <p>Vanilla announces a jukebox exactly once, to whoever is within the level-event radius at that
 * instant. That is fine for a finite song and useless for a looping one. Rather than compensating
 * with a periodic re-announcement -- which pays a constant packet cost forever to cover a rare
 * event, and still leaves a window of several seconds -- the announcement is sent at the two
 * moments it is actually needed: when the song restarts, and when a player is sent the chunk.
 */
public final class JukeboxLoopSync {

    /** Vanilla's "a jukebox song started playing" level event. */
    private static final int PLAY_JUKEBOX_SONG_EVENT = 1010;

    private JukeboxLoopSync() {
    }

    /**
     * Announces a restart to everyone in range. Uses the same call vanilla uses, so a client
     * without the mod handles it with no special support.
     */
    public static void broadcastPlay(LevelAccessor level, BlockPos pos, Holder<JukeboxSong> song) {
        level.levelEvent(null, PLAY_JUKEBOX_SONG_EVENT, pos, songId(level, song));
    }

    /**
     * Announces every playing jukebox in a chunk to one specific player.
     *
     * <p>Called when the chunk is sent, which is precisely when the client becomes able to act on
     * it -- earlier and the client has nowhere to put the sound, later and there is dead air.
     */
    public static void syncChunkTo(ServerPlayer player, ServerLevel level, LevelChunk chunk) {
        for (BlockEntity be : chunk.getBlockEntities().values()) {
            if (!(be instanceof JukeboxBlockEntity jukebox)) {
                continue;
            }

            JukeboxSongPlayer songPlayer =
                    ((JukeboxBlockEntityAccessor) jukebox).loopingDiscs$getSongPlayer();

            if (songPlayer == null) {
                continue;
            }

            Holder<JukeboxSong> song =
                    ((JukeboxSongPlayerAccessor) songPlayer).loopingDiscs$getSong();

            if (song == null || !LoopingDiscsConfig.shouldLoop(level, be.getBlockPos())) {
                continue;
            }

            player.connection.send(new ClientboundLevelEventPacket(
                    PLAY_JUKEBOX_SONG_EVENT,
                    be.getBlockPos(),
                    songId(level, song),
                    false
            ));
        }
    }

    /** Uses the exact idiom already proven to compile in this project. */
    private static int songId(LevelAccessor level, Holder<JukeboxSong> song) {
        return level.registryAccess()
                .lookupOrThrow(Registries.JUKEBOX_SONG)
                .getId(song.value());
    }
}