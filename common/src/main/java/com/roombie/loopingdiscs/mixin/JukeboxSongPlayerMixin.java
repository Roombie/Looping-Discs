package com.roombie.loopingdiscs.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.roombie.loopingdiscs.config.LoopingDiscsConfig;
import com.roombie.loopingdiscs.loop.JukeboxLoopSync;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.item.JukeboxSongPlayer;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Loops the disc by restarting the song rather than by preventing it from ever ending.
 *
 * <p>The distinction matters. Neutering {@code JukeboxSong.hasFinished} globally affects every
 * caller in the game, present and future, and leaves {@code ticksSinceSongStarted} growing without
 * bound and semantically meaningless to anything else that reads it. Wrapping the single call site
 * inside {@code tick} confines the change to the one decision we care about: what happens at the
 * moment the song would have ended.
 *
 * <p>Because the restart is expressed as a genuine vanilla play event, a client without this mod
 * installed behaves correctly on its own. That is the whole point of doing it here rather than on
 * the client: the mod degrades to "looping with an audible seam" instead of degrading to "broken".
 */
@Mixin(JukeboxSongPlayer.class)
public abstract class JukeboxSongPlayerMixin {

    @Shadow
    private long ticksSinceSongStarted;

    @Shadow
    private Holder<JukeboxSong> song;

    @Shadow
    @Final
    private BlockPos blockPos;

    /**
     * VERIFY: the descriptor below must match {@code JukeboxSong.hasFinished} in 26.2, and that
     * method must actually be invoked from {@code JukeboxSongPlayer.tick}. If either has changed,
     * this injector fails loudly at load time (defaultRequire is 1), which is the behaviour we
     * want -- a silent no-op would be far worse.
     */
    @WrapOperation(
            method = "tick",
            at = @org.spongepowered.asm.mixin.injection.At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/JukeboxSong;hasFinished(J)Z"
            )
    )
    private boolean loopingDiscs$restartInsteadOfFinishing(
            JukeboxSong instance,
            long ticks,
            Operation<Boolean> original,
            LevelAccessor level,
            BlockState blockState
    ) {
        if (!original.call(instance, ticks)) {
            return false;
        }

        if (this.song == null || !LoopingDiscsConfig.shouldLoop(level, this.blockPos)) {
            return true;
        }

        // Both sides reset, so the client's own block entity keeps spawning note particles and
        // stays in step with the server. Only the server announces the restart.
        this.ticksSinceSongStarted = 0L;

        if (!level.isClientSide()) {
            JukeboxLoopSync.broadcastPlay(level, this.blockPos, this.song);
        }

        return false;
    }
}