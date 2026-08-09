package com.roombie.loopingdiscs;

import com.roombie.loopingdiscs.config.LoopingDiscsConfig;

import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;

@Mod(Constants.MOD_ID)
public class LoopingDiscs {

    public LoopingDiscs() {
        LoopingDiscsConfig.load(FMLPaths.CONFIGDIR.get());
        Constants.LOG.info("Looping Discs initialized on NeoForge.");
    }
}