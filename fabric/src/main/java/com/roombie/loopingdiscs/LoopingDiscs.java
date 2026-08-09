package com.roombie.loopingdiscs;

import com.roombie.loopingdiscs.config.LoopingDiscsConfig;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class LoopingDiscs implements ModInitializer {

    @Override
    public void onInitialize() {
        LoopingDiscsConfig.load(FabricLoader.getInstance().getConfigDir());
        Constants.LOG.info("Looping Discs initialized on Fabric.");
    }
}