# Looping Discs

A lightweight Minecraft mod that automatically loops music discs while they remain inside a jukebox.

**Looping Discs** keeps the vanilla jukebox experience intact: no replacement blocks, no custom GUI, no special discs, and no new recipes. It simply makes jukebox music keep playing, while adding sensible behavior for multiplayer and redstone contraptions.

Built for **Fabric** and **NeoForge**.

## Features

- Automatically restarts music discs when they reach the end.
- Keeps looping for as long as the disc remains inside the jukebox.
- Stops normally when the disc is removed.
- Stops correctly when the jukebox is destroyed, even near the end of a song.
- Synchronizes active jukeboxes for players who arrive after the music has already started.
- Uses Minecraft's standard jukebox-song system for broad compatibility.
- Preserves vanilla-style redstone and automation behavior through an in-world looping opt-out.
- Includes a global configuration option to disable looping without uninstalling the mod.
- Supports both **Fabric** and **NeoForge** from a shared multiloader codebase.
- Adds no blocks, items, recipes, or GUIs.

## Vanilla-friendly looping

By default, a jukebox loops normally.

If a jukebox is placed directly on top of a block in the following tag, looping is disabled for that jukebox and it behaves like vanilla:

```text
#loopingdiscs:disables_looping
```

The default tag contains:

```text
minecraft:hopper
minecraft:dropper
minecraft:dispenser
minecraft:observer
```

This helps preserve jukebox-based redstone timers and automation setups that depend on a disc eventually finishing.

The tag is data-driven, so datapacks and server owners can extend or modify the list without changing the mod's code.

## Multiplayer synchronization

Vanilla only announces a jukebox playback event to players who are in range when the event occurs. For looping music, this can cause a player who arrives later to hear nothing until the next restart.

Looping Discs accounts for this by synchronizing already-playing jukeboxes when their chunk is sent to a player.

As a result, players entering the area can begin hearing an active looping jukebox without waiting for the current loop to finish.

### A note about playback position

When a player arrives after a disc has already started, their client begins that disc from the start rather than from the exact playback position heard by players who were already present.

Players who are already listening are not restarted simply because another player enters the area.

## Configuration

Looping Discs creates:

```text
config/loopingdiscs.properties
```

The default configuration is:

```properties
enabled=true
```

Set:

```properties
enabled=false
```

to restore vanilla jukebox behavior everywhere without removing the mod.

For individual jukeboxes, use a block from:

```text
#loopingdiscs:disables_looping
```

directly underneath the jukebox instead.

## Supported versions

| Component | Version |
| --- | --- |
| Minecraft | 26.2 |
| Fabric | Supported |
| NeoForge | Supported |
| Java | 25 or newer |

Fabric and NeoForge use separate JAR files. Install the build made for your selected mod loader.

## Dependencies

### Fabric

The Fabric version requires:

- **Fabric Loader**
- **Fabric API**

### NeoForge

The NeoForge version requires:

- **NeoForge**

## Installation

1. Install the appropriate mod loader for Minecraft 26.2.
2. If you are using Fabric, install Fabric API as well.
3. Download the corresponding **Looping Discs** JAR.
4. Place the required JAR files in your Minecraft `mods` folder.
5. Launch the game and insert a music disc into a jukebox.

For multiplayer, installing Looping Discs on both the server and participating clients is recommended so the complete playback behavior remains consistent.

## Building from source

Clone the repository:

```bash
git clone https://github.com/Roombie/Looping-Discs.git
cd Looping-Discs
```

Then build the project from the repository root.

### Windows PowerShell

```powershell
.\gradlew.bat clean build
```

### Linux and macOS

```bash
./gradlew clean build
```

Generated JAR files can be found in:

```text
fabric/build/libs/
neoforge/build/libs/
```

## Compatibility

Looping Discs modifies Minecraft's standard jukebox playback process using Mixins while continuing to use vanilla jukebox song events.

Mods that completely replace the jukebox or music-disc playback system may require additional compatibility work.

The `#loopingdiscs:disables_looping` block tag is intended to make compatibility with redstone builds and modded automation easier to customize.

## License and permissions

Copyright © 2026 Roombie. **All Rights Reserved.**

You may include an official, unmodified build of Looping Discs in a modpack provided that:

- **Roombie** is clearly credited as the author.
- The modpack includes a link to the original Looping Discs project page or source repository.
- The mod is not presented as the modpack creator's own work.
- Access to the standalone mod is not sold or placed behind a separate fee.

Without prior written permission, you may not:

- Reupload or redistribute Looping Discs as a standalone download.
- Publish modified versions, forks, ports, or derivative works.
- Reuse or redistribute the source code, artwork, branding, or other project assets.
- Sell, sublicense, rent, or otherwise commercially distribute the mod.
- Remove or alter copyright, attribution, or license notices.

See [LICENSE](LICENSE) for the complete terms.
