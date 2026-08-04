package com.roombie.loopingdiscs.mixin.client;

import java.util.Map;

import net.minecraft.client.renderer.LevelEventHandler;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LevelEventHandler.class)
public interface LevelEventHandlerAccessor {

    @Accessor("playingJukeboxSongs")
    Map<BlockPos, SoundInstance> loopingDiscs$getPlayingJukeboxSongs();

    @Invoker("notifyNearbyEntities")
    void loopingDiscs$notifyNearbyEntities(Level level, BlockPos pos, boolean isPlaying);
}