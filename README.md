# Reopened Modular Turrets

NeoForge 1.21.1 revival of [OpenModularTurrets](https://www.curseforge.com/minecraft/mc-mods/openmodularturrets).

Modular automated defense: turret bases, multiple turret types, addons, energy, ammo storage, and targeting GUI.

## Requirements

- **Minecraft** 1.21.1
- **NeoForge** 21.1.x (see `gradle.properties`)
- **Java 21** toolchain

## Development

```bash
./gradlew build          # compile + jar
./gradlew runClient      # test client
./gradlew runServer      # test server
```

Mod id: `reopenedmodularturrets`

### JEI / recipes

Crafting recipes live under `data/reopenedmodularturrets/recipe/` (singular `recipe` in 1.21+). After changes, restart the client or run `/reload` in a world.

## Current feature set

- 5 turret base tiers with FE storage, ammo magazines, upgrades, and addons
- Multiple turret types (gun, grenade, rocket, laser, and more)
- Solar and redstone reactor addons, expanders, concealer, serial port addon (item)
- Base GUI with targeting toggles, trusted players, camo/light options
- Ownership on block place
- No separate OMLib dependency (core helpers are internal)

## Roadmap

### Addon API (third-party mods)

- **`IBaseController`** — external blocks can register with a turret base to override targeting, redstone mode, trusted players, and per-entity target validation (ported from original OMT).
- **`ITurretBase`** — public interface for turret bases so addon mods can provide **custom base implementations** (not planned for the default `TurretBaseBlockEntity` only).
- **`IPowerSource` / firing-readiness API** — abstract “can this base fire?” and “consume power for one shot” so addons can build non-FE bases (e.g. Create stress, coolant, or custom resources).
- **Example addon mods** (separate projects): Create stress-powered base, contraption-aware targeting controller, etc.

The main mod will expose stable API packages under `com.ommods.reopenedmodularturrets.api`; concrete Create or other mod integrations stay in optional addon mods.

### Computer integration

- CC:Tweaked `turret_base` peripheral (Serial Port addon or Tier 5)
- OpenComputers `turret_base` component (same gate and methods as original OMT)

## License

Source code: MIT. Art assets: CC-BY-NC (legacy OMT assets where applicable).
