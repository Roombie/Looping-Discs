package com.roombie.loopingdiscs.mixin;

import net.minecraft.core.Holder;
import net.minecraft.world.item.JukeboxSong;
import net.minecraft.world.item.JukeboxSongPlayer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * Reads the currently playing song for the chunk-sent sync path.
 *
 * <p>VERIFY: field name of the song holder inside JukeboxSongPlayer. Note this is the same field
 * {@code JukeboxSongPlayerMixin} shadows; the accessor exists so the sync path can read it from
 * the outside without that mixin having to expose anything.
 */
@Mixin(JukeboxSongPlayer.class)
public interface JukeboxSongPlayerAccessor {

    @Accessor("song")
    Holder<JukeboxSong> loopingDiscs$getSong();
}
