#!/usr/bin/env python3
"""Port OMT assets and recipes to reopenedmodularturrets 1.21.1 format."""

from __future__ import annotations

import json
import re
import shutil
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
OLD_ASSETS = ROOT / "src/main/resources/assets/openmodularturrets"
NEW_ASSETS = ROOT / "src/main/resources/assets/reopenedmodularturrets"
NEW_DATA = ROOT / "src/main/resources/data/reopenedmodularturrets/recipes"
OLD_RECIPES = OLD_ASSETS / "recipes"

MOD = "reopenedmodularturrets"
OLD_MOD = "openmodularturrets"

INTERMEDIATE_MAP = {
    i: name
    for tier in range(1, 6)
    for i, name in [
        (tier - 1, f"sensor_tier_{tier}"),
        (tier + 4, f"chamber_tier_{tier}"),
        (tier + 9, f"barrel_tier_{tier}"),
    ]
}

ADDON_MAP = {
    0: "addon_solar",
    1: "addon_redstone_reactor",
    2: "addon_damage_amp",
    3: "addon_potentia",
    4: "addon_serial_port",
    5: "addon_recycler",
    6: "addon_concealer",
    7: "addon_fake_drops",
}

UPGRADE_MAP = {
    0: "upgrade_fire_rate",
    1: "upgrade_efficiency",
    2: "upgrade_range",
    3: "upgrade_accuracy",
    4: "upgrade_scatter_shot",
}

AMMO_MAP = {
    0: "bullet",
    1: "grenade",
    2: "blazing_clay",
    3: "ferro_slug",
    4: "rocket",
}

ITEM_RENAMES = {
    "machine_gun_turret": "gun_turret",
    "turret_base": None,
}

TAG_MAP = {
    "ingotGold": "c:ingots/gold",
    "ingotIron": "c:ingots/iron",
    "ingotOsmium": "c:ingots/osmium",
    "ingotRefinedGlowstone": "c:ingots/refined_glowstone",
    "ingotRefinedObsidian": "c:ingots/refined_obsidian",
    "ingotSteel": "c:ingots/steel",
    "ingotTin": "c:ingots/tin",
    "plankWood": "minecraft:planks",
    "chestWood": "c:chests/wooden",
    "obsidian": "c:obsidians",
    "dustRedstone": "c:dusts/redstone",
    "gemQuartz": "c:gems/quartz",
    "circuitBasic": "c:circuits/basic",
    "circuitAdvanced": "c:circuits/advanced",
    "circuitElite": "c:circuits/elite",
}

CONSTANT_TAGS = {
    "INGOTGOLD": "c:ingots/gold",
    "INGOTIRON": "c:ingots/iron",
    "INGOTOSMIUM": "c:ingots/osmium",
    "INGOTREFINEDGLOWSTONE": "c:ingots/refined_glowstone",
    "INGOTREFINEDOBSIDIAN": "c:ingots/refined_obsidian",
    "INGOTSTEEL": "c:ingots/steel",
    "INGOTTIN": "c:ingots/tin",
    "PLANKWOOD": "minecraft:planks",
    "CHESTWOOD": "c:chests/wooden",
    "OBSIDIAN": "c:obsidians",
    "IOBUS": f"{MOD}:io_bus",
    "MEKBASICCIRCUIT": "c:circuits/basic",
    "MEKADVCIRCUIT": "c:circuits/advanced",
    "MEKELITECIRCUIT": "c:circuits/elite",
}

TURRET_IDS = [
    "disposable_item_turret",
    "potato_cannon_turret",
    "gun_turret",
    "grenade_turret",
    "incendiary_turret",
    "rocket_turret",
    "relativistic_turret",
    "teleporter_turret",
    "laser_turret",
    "rail_gun_turret",
    "plasma_turret",
    "arc_turret",
    "melee_turret",
    "crossbow_turret",
]


def map_item(item: str, data: int | None = None) -> str | dict:
    if item.startswith("#"):
        tag = CONSTANT_TAGS.get(item[1:], None)
        if tag:
            return {"tag": tag}
        raise ValueError(f"Unknown constant tag {item}")

    if ":" not in item:
        item = f"{OLD_MOD}:{item}"

    namespace, path = item.split(":", 1)
    if namespace != OLD_MOD:
        return item

    if path == "intermediate_tiered":
        mapped = INTERMEDIATE_MAP[data or 0]
        return f"{MOD}:{mapped}"
    if path == "addon_meta":
        mapped = ADDON_MAP[data or 0]
        return f"{MOD}:{mapped}"
    if path == "upgrade_meta":
        mapped = UPGRADE_MAP[data or 0]
        return f"{MOD}:{mapped}"
    if path == "ammo_meta":
        mapped = AMMO_MAP[data or 0]
        return f"{MOD}:{mapped}"
    if path == "intermediate_regular":
        return f"{MOD}:io_bus"
    if path == "base_addon_meta":
        return f"{MOD}:base_addon_loot_deleter"
    if path == "turret_base":
        tier = (data or 0) + 1
        return f"{MOD}:turret_base_tier_{tier}"
    if path in ITEM_RENAMES and ITEM_RENAMES[path] is not None:
        return f"{MOD}:{ITEM_RENAMES[path]}"
    if path == "machine_gun_turret":
        return f"{MOD}:gun_turret"
    if path == "turret_base_normal":
        return f"{MOD}:turret_base_tier_1"

    return f"{MOD}:{path}"


def convert_ingredient(raw) -> dict | str:
    if isinstance(raw, str):
        if raw.startswith("#"):
            return {"tag": CONSTANT_TAGS[raw[1:]]}
        return raw
    if "type" in raw and raw["type"] == "forge:ore_dict":
        ore = raw["ore"]
        return {"tag": TAG_MAP.get(ore, f"c:{ore}")}
    if "item" in raw:
        data = raw.get("data")
        mapped = map_item(raw["item"], data)
        if isinstance(mapped, dict):
            return mapped
        return {"item": mapped}
    return raw


def convert_recipe(path: Path) -> dict | None:
    data = json.loads(path.read_text())
    variant = path.stem.split("_")[-1]
    if variant not in {"vanilla", "enderio", "mekanism"}:
        return None

    recipe_type = data.get("type", "")
    if recipe_type != "forge:ore_shaped":
        return None

    result_raw = data["result"]
    result_item = map_item(result_raw["item"], result_raw.get("data"))
    if isinstance(result_item, dict):
        return None

    converted = {
        "type": "minecraft:crafting_shaped",
        "pattern": data["pattern"],
        "key": {k: convert_ingredient(v) for k, v in data["key"].items()},
        "result": {"item": result_item, "count": result_raw.get("count", 1)},
    }

    if variant == "enderio":
        converted["neoforge:conditions"] = [{"type": "neoforge:mod_loaded", "modid": "enderio"}]
    elif variant == "mekanism":
        converted["neoforge:conditions"] = [{"type": "neoforge:mod_loaded", "modid": "mekanism"}]

    return converted


def port_recipes() -> int:
    NEW_DATA.mkdir(parents=True, exist_ok=True)
    count = 0
    for path in sorted(OLD_RECIPES.glob("*.json")):
        if path.name.startswith("_"):
            continue
        converted = convert_recipe(path)
        if converted is None:
            continue
        out = NEW_DATA / path.name
        out.write_text(json.dumps(converted, indent=2) + "\n")
        count += 1
    return count


def generate_turret_assets() -> None:
    for turret_id in TURRET_IDS:
        bs_dir = NEW_ASSETS / "blockstates"
        bs_dir.mkdir(parents=True, exist_ok=True)
        (bs_dir / f"{turret_id}.json").write_text(
            json.dumps({"variants": {"": {"model": f"{MOD}:block/{turret_id}"}}}, indent=2) + "\n"
        )

        model_block = NEW_ASSETS / "models/block"
        model_block.mkdir(parents=True, exist_ok=True)
        (model_block / f"{turret_id}.json").write_text(
            json.dumps({"parent": "minecraft:builtin/entity"}, indent=2) + "\n"
        )

        model_item = NEW_ASSETS / "models/item"
        model_item.mkdir(parents=True, exist_ok=True)
        (model_item / f"{turret_id}.json").write_text(
            json.dumps({"parent": f"{MOD}:block/{turret_id}"}, indent=2) + "\n"
        )

        src_entity = NEW_ASSETS / f"textures/entity/{turret_id}.png"
        src_block = NEW_ASSETS / f"textures/block/{turret_id}.png"
        dst_entity = NEW_ASSETS / f"textures/entity/{turret_id}.png"
        if not src_entity.exists() and src_block.exists():
            shutil.copy2(src_block, dst_entity)


def port_lang() -> None:
    for lang_file in (OLD_ASSETS / "lang").glob("*.lang"):
        locale = lang_file.stem.lower()
        if locale == "en_us":
            out_name = "en_us.json"
        elif locale == "de_de":
            out_name = "de_de.json"
        elif locale == "en_gb":
            out_name = "en_gb.json"
        else:
            continue

        entries: dict[str, str] = {}
        for line in lang_file.read_text(encoding="utf-8", errors="ignore").splitlines():
            line = line.strip()
            if not line or line.startswith("#") or "=" not in line:
                continue
            key, value = line.split("=", 1)
            key = key.strip()
            value = value.strip()

            if key == "itemGroup.openmodularturrets":
                entries["itemGroup.reopenedmodularturrets"] = value.replace("Open Modular Turrets", "Reopened Modular Turrets")
                continue

            new_key = key
            new_key = new_key.replace("openmodularturrets", MOD)
            new_key = new_key.replace("tile.", "block.")
            new_key = new_key.replace("machine_gun_turret", "gun_turret")
            new_key = re.sub(r"turret_base_tier_(one|two|three|four|five)", lambda m: {
                "one": "1", "two": "2", "three": "3", "four": "4", "five": "5"
            }[m.group(1)], new_key)
            new_key = new_key.replace("addon_solar_panel", "addon_solar")
            new_key = new_key.replace("sensor_tier_one", "sensor_tier_1")
            new_key = new_key.replace("sensor_tier_two", "sensor_tier_2")
            new_key = new_key.replace("sensor_tier_three", "sensor_tier_3")
            new_key = new_key.replace("sensor_tier_four", "sensor_tier_4")
            new_key = new_key.replace("sensor_tier_five", "sensor_tier_5")
            new_key = new_key.replace("chamber_tier_one", "chamber_tier_1")
            new_key = new_key.replace("chamber_tier_two", "chamber_tier_2")
            new_key = new_key.replace("chamber_tier_three", "chamber_tier_3")
            new_key = new_key.replace("chamber_tier_four", "chamber_tier_4")
            new_key = new_key.replace("chamber_tier_five", "chamber_tier_5")
            new_key = new_key.replace("barrel_tier_one", "barrel_tier_1")
            new_key = new_key.replace("barrel_tier_two", "barrel_tier_2")
            new_key = new_key.replace("barrel_tier_three", "barrel_tier_3")
            new_key = new_key.replace("barrel_tier_four", "barrel_tier_4")
            new_key = new_key.replace("barrel_tier_five", "barrel_tier_5")
            new_key = new_key.replace("ammo_bullet", "bullet")
            new_key = new_key.replace("ammo_grenade", "grenade")
            new_key = new_key.replace("ammo_rocket", "rocket")
            new_key = new_key.replace("ammo_ferro_slug", "ferro_slug")
            new_key = new_key.replace("ammo_blazing_clay", "blazing_clay")
            entries[new_key] = value

        out_path = NEW_ASSETS / "lang" / out_name
        out_path.parent.mkdir(parents=True, exist_ok=True)
        existing = {}
        if out_path.exists():
            existing = json.loads(out_path.read_text())
        existing.update(entries)
        out_path.write_text(json.dumps(dict(sorted(existing.items())), indent=2, ensure_ascii=False) + "\n")


def copy_textures() -> None:
    src = OLD_ASSETS / "textures"
    if not src.exists():
        return
    for png in src.rglob("*.png"):
        rel = png.relative_to(src)
        name = rel.name
        if name == "machine_gun_turret.png":
            name = "gun_turret.png"
        for folder in ("entity", "block", "item"):
            dst = NEW_ASSETS / "textures" / folder / name
            if not dst.exists():
                dst.parent.mkdir(parents=True, exist_ok=True)
                shutil.copy2(png, dst)


def main() -> None:
    recipe_count = port_recipes()
    generate_turret_assets()
    port_lang()
    copy_textures()
    print(f"Ported {recipe_count} recipes")
    print("Generated turret assets and lang files")


if __name__ == "__main__":
    main()
