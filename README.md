# Reopened Modular Turrets

NeoForge 26.2 revival of [OpenModularTurrets](https://www.curseforge.com/minecraft/mc-mods/openmodularturrets).

Modular automated defense: turret bases, gun and grenade turrets, solar addon, energy, ammo storage, and targeting GUI.

## Requirements

- **Minecraft** 26.2
- **NeoForge** 26.2.0.67+ (see `gradle.properties`)
- **Java 25** toolchain (Gradle downloads via Foojay resolver)
- **Gradle** 9.2.1+ (wrapper included)

For local Gradle runs, use Java 21+ for the Gradle daemon if Java 26 is not supported by your Gradle version:

```bash
export JAVA_HOME=/path/to/java-21
./gradlew runClient
```

## Development

```bash
./gradlew build          # compile + jar
./gradlew runClient      # test client
./gradlew runServer      # test server
```

Mod id: `reopenedmodularturrets`

### EMI / recipe reload

After changing datapack recipes in development or in-game:

- Run **`/reload`** in a world — EMI picks up recipe changes automatically.
- Use **`F3 + T`** to reload resources (textures/models).
- If EMI still shows stale recipes after a mod rebuild, restart the client.

## Current feature set (MVP)

- 5 turret base tiers with energy storage and internal ammo magazines
- Gun turret (hitscan) and grenade turret (projectile + explosion)
- Solar addon (sky-visible FE generation)
- Base GUI with ammo slots and targeting toggles (mobs / players / neutral)
- Ownership on block place
- No separate OMLib dependency (core helpers are internal)

## License

Source code: MIT. Art assets: CC-BY-NC (legacy OMT assets where applicable).
