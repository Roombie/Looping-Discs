package com.roombie.loopingdiscs.config;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import com.roombie.loopingdiscs.Constants;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;

/**
 * Decides, for a given jukebox, whether the disc should loop.
 *
 * <p>Two independent switches, because they answer different questions. The properties file
 * answers "does this world want the mod's behaviour at all", which is a server owner's decision.
 * The block tag answers "should this particular jukebox loop", which is a builder's decision and
 * has to be expressible in survival without touching a config file or running a command.
 *
 * <p>The tag exists mainly because a playing jukebox emits a redstone signal of 15, and looping
 * means that signal never falls. Jukebox-as-timer is an established vanilla technique, and without
 * an in-world opt-out this mod would break those builds with no recovery short of uninstalling.
 * Defaulting the tag to the blocks those contraptions already use means existing builds keep
 * working untouched.
 */
public final class LoopingDiscsConfig {

    private static final String FILE_NAME = "loopingdiscs.properties";
    private static final String KEY_ENABLED = "enabled";

    /**
     * A jukebox sitting directly on top of one of these plays exactly like vanilla.
     *
     * <p>Data-driven so server owners can extend it without a code change. Prior art: the
     * Jukebox Looping mod solves the same contraption problem with a hardcoded "is a block entity"
     * check; this is the same idea made configurable.
     */
    public static final TagKey<Block> DISABLES_LOOPING = TagKey.create(
            Registries.BLOCK,
            Identifier.fromNamespaceAndPath(Constants.MOD_ID, "disables_looping"));

    private static volatile boolean enabled = true;

    private LoopingDiscsConfig() {
    }

    /** Called once from each loader entrypoint, which is the only thing that knows the path. */
    public static void load(Path configDir) {
        Path file = configDir.resolve(FILE_NAME);
        Properties props = new Properties();

        try {
            if (Files.exists(file)) {
                try (Reader reader = Files.newBufferedReader(file)) {
                    props.load(reader);
                }
            } else {
                props.setProperty(KEY_ENABLED, "true");
                Files.createDirectories(configDir);
                try (Writer writer = Files.newBufferedWriter(file)) {
                    props.store(writer, "Set enabled=false to restore vanilla jukebox behaviour "
                            + "everywhere. For a single jukebox, place a block from the "
                            + "#loopingdiscs:disables_looping tag directly underneath it instead.");
                }
            }

            enabled = Boolean.parseBoolean(props.getProperty(KEY_ENABLED, "true"));
        } catch (IOException e) {
            // A malformed or unreadable config must not stop the game from loading. Defaulting to
            // the advertised behaviour and saying so is the least surprising failure mode.
            Constants.LOG.warn("Could not read {}, defaulting to enabled=true", FILE_NAME, e);
            enabled = true;
        }
    }

    /**
     * @param pos the jukebox position, not the block below it
     */
    public static boolean shouldLoop(LevelAccessor level, BlockPos pos) {
        if (!enabled) {
            return false;
        }

        return !level.getBlockState(pos.below()).is(DISABLES_LOOPING);
    }
}