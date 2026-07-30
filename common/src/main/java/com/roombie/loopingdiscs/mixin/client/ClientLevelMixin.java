package com.roombie.loopingdiscs.mixin.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelEventHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin extends Level {

    @Shadow
    @Final
    private LevelEventHandler levelEventHandler;

    protected ClientLevelMixin(
            WritableLevelData levelData,
            ResourceKey<Level> dimension,
            RegistryAccess registryAccess,
            Holder<DimensionType> dimensionType,
            boolean isClientSide,
            boolean isDebug,
            long biomeZoomSeed,
            int maxChainedNeighborUpdates
    ) {
        super(
                levelData,
                dimension,
                registryAccess,
                dimensionType,
                isClientSide,
                isDebug,
                biomeZoomSeed,
                maxChainedNeighborUpdates
        );
    }

    @Inject(
            method = "setServerVerifiedBlockState",
            at = @At("TAIL")
    )
    private void loopingDiscs$stopRemovedJukebox(
            BlockPos pos,
            BlockState state,
            int flags,
            CallbackInfo ci
    ) {
        LevelEventHandlerAccessor accessor =
                (LevelEventHandlerAccessor) (Object) this.levelEventHandler;

        if (!accessor.loopingDiscs$getPlayingJukeboxSongs().containsKey(pos)) {
            return;
        }

        boolean stillPlaying =
                state.is(Blocks.JUKEBOX)
                        && state.getValue(JukeboxBlock.HAS_RECORD);

        if (!stillPlaying) {
            accessor.loopingDiscs$stopJukeboxSongAndNotifyNearby(pos);
        }
    }
}