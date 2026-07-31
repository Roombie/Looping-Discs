# Looping Discs

A lightweight Minecraft mod that continuously loops music discs while they remain inside a jukebox. Playback stops normally when the disc is removed or the jukebox is destroyed.

Looping Discs is built as a multiloader project for **Fabric** and **NeoForge**.

## Features

- Automatically restarts music discs when they finish.
- Keeps the disc playing for as long as it remains inside the jukebox.
- Stops playback when the disc is removed.
- Stops playback when the jukebox is destroyed, including near the end of a song.
- Works through Minecraft's standard jukebox-song system.
- Does not require Fabric API.

## Supported versions

| Component | Version |
| --- | --- |
| Minecraft | 26.2 |
| Fabric | Supported |
| NeoForge | Supported |
| Java | 25 or newer |

Fabric and NeoForge use separate JAR files. Install the file made for your selected mod loader.

## Installation

1. Install Fabric Loader or NeoForge for the supported Minecraft version.
2. Download the corresponding Looping Discs JAR.
3. Place the JAR in the Minecraft `mods` folder.
4. Launch the game and insert a music disc into a jukebox.

For multiplayer, install the mod on both the server and participating clients. The server maintains the jukebox playback state, while each client handles the looping audio.

## Building from source

Clone the repository and run the following command from the project root.

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

Looping Discs modifies the standard jukebox playback process using Mixins. Mods that completely replace Minecraft's jukebox or music-disc playback system may require additional compatibility work.

## License and permissions

Copyright © 2026 Roombie. **All Rights Reserved.**

You may include an unmodified official build of Looping Discs in a modpack, provided that:

- Proper credit is given to **Roombie**.
- A link to the original Looping Discs project page or repository is included.
- The mod is not presented as your own work.

You may not reupload or redistribute Looping Discs as a standalone download, publish modified versions or ports, sell the mod, or reuse its source code or assets without prior written permission.

See [LICENSE](LICENSE) for the complete terms. Third-party code and template components remain subject to their respective licenses.