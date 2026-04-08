# Mob Catcher

A Minecraft Fabric mod that adds a Mob Catcher item - capture and release mobs like a Pokeball!

## Features

- **Mob Catcher Item**: Craft a special device to capture mobs
- **Capture Mobs**: Right-click any mob to capture it into the Mob Catcher
- **Release Mobs**: Right-click the ground with a filled Mob Catcher to release the captured mob
- **Tooltip Display**: See the captured mob's name in the item tooltip
- **Variable Capture Rates**: 
  - Passive and neutral mobs: 100% capture rate
  - Hostile mobs: 50% capture rate
- **Single-Use**: Each Mob Catcher can only be used once

## Screenshots

![Screenshot 1](screenshot-1.png)
![Screenshot 2](screenshot-2.png)
![Screenshot 3](screenshot-3.png)

## Crafting Recipe

```
  S  
I E I
  I  

S = String
I = Iron Ingot
E = Ender Pearl
```

Produces 1 Mob Catcher.

## Requirements

- Minecraft 1.21.1
- Fabric Loader 0.16.0+
- Fabric API

## Installation

1. Install [Fabric Loader](https://fabricmc.net/use/installer/) for Minecraft 1.21.1
2. Download [Fabric API](https://modrinth.com/mod/fabric-api) and place in your mods folder
3. Download the Mob Catcher mod JAR from the releases and place in your mods folder
4. Launch Minecraft with the Fabric profile

## Building from Source

```bash
git clone https://github.com/Simplifine-gamedev/mob-catcher.git
cd mob-catcher
./gradlew build
```

The compiled JAR will be in `build/libs/`.

## Usage

1. Craft a Mob Catcher using the recipe above
2. Right-click any mob to capture it (hostile mobs have 50% success rate)
3. The Mob Catcher tooltip will show which mob is captured
4. Right-click on the ground to release the captured mob

## License

MIT License
