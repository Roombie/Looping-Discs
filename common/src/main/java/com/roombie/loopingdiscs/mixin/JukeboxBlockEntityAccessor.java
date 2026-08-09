package com.roombie.loopingdiscs.mixin;

import net.minecraft.world.item.JukeboxSongPlayer;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Reaches the song player without relying on a public API that may or may not exist in 26.2.
 */
@Mixin(JukeboxBlockEntity.class)
public interface JukeboxBlockEntityAccessor {

    @Accessor("jukeboxSongPlayer")
    JukeboxSongPlayer loopingDiscs$getSongPlayer();
}