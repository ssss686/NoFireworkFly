# NoFireWorkFly Neo

NeoForge 1.21.1 elytra enhancement mod — firework-free flight with configurable speed, hunger consumption, and ground takeoff via double-tap.

## Features

- **No fireworks needed** — fly indefinitely without firework rockets
- **Ground takeoff** — double-tap space to launch from the ground
- **Configurable flight speed** — from vanilla rocket level 1 (0.01) to level 3 (0.03) and beyond
- **Hunger-based flight** — flight consumes hunger, configurable rate and minimum threshold
- **Optional durability consumption** — toggle whether elytra loses durability during flight
- **Slow fall** — press sneak while flying to stop and apply Slow Falling
- **Max height limit** — configurable height ceiling above world limit
- **Armor slowdown** — optional flight speed reduction based on armor weight
- **Compatible with Elytra Slot** — works when elytra is in a custom back slot

## Requirements

| Mod | Version |
|-----|---------|
| [NeoForge](https://neoforged.net/) | 21.1.235+ |
| [Caelus](https://modrinth.com/mod/caelus) | 7.0+ |

## Installation

1. Install NeoForge 1.21.1
2. Install Caelus
3. Place `nofireworkfly_neo-0.1.jar` in your `mods/` folder
4. Launch the game

## Configuration

All settings are in `config/nofireworkfly_neo-common.toml`. The file is generated on first launch.

| Option | Default | Description |
|--------|---------|-------------|
| `enableNoFireworkFlight` | `true` | Master toggle — disable to revert to vanilla elytra |
| `wingsSpeed` | `0.01` | Base flight speed (0.008 – 0.1) |
| `consumeHunger` | `true` | Whether flight consumes hunger |
| `exhaustionAmount` | `0.03` | Hunger exhaustion per tick (0.0 – 1.0) |
| `requiredFoodAmount` | `6.0` | Minimum food level to fly (0.0 – 20.0) |
| `dropOutOfSkyWhenTired` | `true` | Stop flying when too hungry |
| `consumeDurability` | `true` | Whether elytra loses durability during flight |
| `armorSlows` | `false` | Whether armor slows flight speed |
| `maxSlowedMultiplier` | `3.0` | Max speed reduction at full armor (1.0 – 10.0) |
| `canSlowFall` | `true` | Sneak to stop flying and get Slow Falling |
| `maxHeightEnabled` | `true` | Enforce maximum flight height |
| `maxHeightAboveWorld` | `384` | Extra height above world limit (16 – 384) |

## License

MIT — see [LICENSE](LICENSE) for details.
