package com.roombie.loopingdiscs.mixin;

import com.roombie.loopingdiscs.loop.JukeboxLoopSync;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.network.PlayerChunkSender;
import net.minecraft.world.level.chunk.LevelChunk;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Announces every playing jukebox in a chunk to the player the chunk was just sent to.
 *
 * <p>Vanilla announces a jukebox once, to whoever is in level-event range at that instant. For a
 * finite song that is harmless; for a looping one it means anyone who was not present hears
 * nothing until the next loop boundary, which can be minutes away.
 *
 * <p>This hooks the moment the chunk packet is dispatched, so the announcement lands exactly when
 * the client becomes able to act on it. NeoForge fires {@code ChunkWatchEvent.Sent} from this same
 * method; hooking the vanilla method directly gets the same timing on both loaders and avoids a
 * Fabric API dependency, since Fabric API has no equivalent event.
 */
@Mixin(PlayerChunkSender.class)
public abstract class PlayerChunkSenderMixin {

    @Inject(method = "sendChunk", at = @At("TAIL"))
    private static void loopingDiscs$announcePlayingJukeboxes(
            ServerGamePacketListenerImpl connection,
            ServerLevel level,
            LevelChunk chunk,
            CallbackInfo ci
    ) {
        JukeboxLoopSync.syncChunkTo(connection.player, level, chunk);
    }
}
