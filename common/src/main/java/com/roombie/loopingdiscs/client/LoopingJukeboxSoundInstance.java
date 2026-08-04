package com.roombie.loopingdiscs.client;

import com.roombie.loopingdiscs.mixin.client.ClientLevelAccessor;
import com.roombie.loopingdiscs.mixin.client.LevelEventHandlerAccessor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelEventHandler;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;

/**
 * The looping sound played by a jukebox, which knows when it should stop.
 *
 * <p>Vanilla keeps exactly one sound instance per jukebox position in
 * {@code LevelEventHandler.playingJukeboxSongs}, and every jukebox sound is finite, so a stop
 * that fails to take effect simply plays itself out. A looping sound has no such safety net:
 * once vanilla drops its reference, nothing in the game can reach the audio again.
 *
 * <p>Rather than mirroring vanilla's bookkeeping, this instance treats that map as the single
 * source of truth and reports itself stopped as soon as it is no longer the sound registered
 * for its position. Because it is a {@link TickableSoundInstance}, the sound engine re-issues
 * a stop on every tick while that holds, which also covers the case where the first stop is
 * overtaken by the asynchronous stream attach in {@code SoundEngine.play}.
 *
 * <p>This covers disc swaps, disc removal, the jukebox being broken, and the level being
 * replaced, without any of those needing a dedicated hook.
 */
public class LoopingJukeboxSoundInstance extends SimpleSoundInstance implements TickableSoundInstance {

    private final BlockPos pos;

    /**
     * Mirrors {@code SimpleSoundInstance.forJukeboxSong}, but looping.
     *
     * <p>Vanilla builds that sound through a private constructor whose whole body delegates to
     * the public identifier-based one with {@code relative = false}, so this call is equivalent.
     */
    public LoopingJukeboxSoundInstance(SoundEvent sound, Vec3 position) {
        super(
                sound.location(),
                SoundSource.RECORDS,
                4.0F,
                1.0F,
                SoundInstance.createUnseededRandom(),
                true,
                0,
                SoundInstance.Attenuation.LINEAR,
                position.x,
                position.y,
                position.z,
                false
        );

        this.pos = BlockPos.containing(position);
    }

    @Override
    public void tick() {
        // Position and volume are fixed; all the work happens in isStopped().
    }

    @Override
    public boolean isStopped() {
        ClientLevel level = Minecraft.getInstance().level;

        if (level == null) {
            return true;
        }

        LevelEventHandler handler =
                ((ClientLevelAccessor) (Object) level).loopingDiscs$getLevelEventHandler();

        if (handler == null) {
            return true;
        }

        SoundInstance current = ((LevelEventHandlerAccessor) (Object) handler)
                .loopingDiscs$getPlayingJukeboxSongs()
                .get(this.pos);

        return current != this;
    }
}