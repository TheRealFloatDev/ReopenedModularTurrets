package com.ommods.reopenedmodularturrets.client;

import com.ommods.reopenedmodularturrets.config.ModConfig;
import com.ommods.reopenedmodularturrets.item.UpgradeItem;
import com.ommods.reopenedmodularturrets.turret.TurretKind;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

import java.util.List;
import java.util.Locale;

public final class ItemTooltipHelper {
    private static final String PREFIX = "text.reopenedmodularturrets.";
    private static final Style DESCRIPTION = Style.EMPTY.withColor(ChatFormatting.GRAY);
    private static final Style STAT_LABEL = Style.EMPTY.withColor(ChatFormatting.DARK_AQUA);
    private static final Style STAT_VALUE = Style.EMPTY.withColor(ChatFormatting.GREEN);
    private static final Style FLAVOUR = Style.EMPTY.withColor(ChatFormatting.GRAY).withItalic(true);

    private ItemTooltipHelper() {}

    public static void appendTooltips(String itemPath, List<Component> tooltip) {
        TurretKind turretKind = turretKindFromPath(itemPath);
        if (turretKind != null) {
            appendTurretTooltip(turretKind, tooltip);
            return;
        }
        if (itemPath.startsWith("turret_base_tier_")) {
            appendBaseTooltip(parseTier(itemPath, "turret_base_tier_"), tooltip);
            return;
        }
        if (itemPath.startsWith("expander_power_tier_")) {
            appendPowerExpanderTooltip(parseTier(itemPath, "expander_power_tier_"), tooltip);
            return;
        }
        if (itemPath.startsWith("expander_inv_tier_")) {
            appendInventoryExpanderTooltip(parseTier(itemPath, "expander_inv_tier_"), tooltip);
            return;
        }
        switch (itemPath) {
            case "addon_solar" -> appendSolarAddon(tooltip);
            case "addon_redstone_reactor" -> appendRedstoneAddon(tooltip);
            case "base_addon_loot_deleter" -> appendLootDeleterAddon(tooltip);
            case "addon_damage_amp" -> appendDamageAmpAddon(tooltip);
            case "addon_potentia" -> appendPotentiaAddon(tooltip);
            case "addon_serial_port" -> appendSerialAddon(tooltip);
            case "addon_recycler" -> appendRecyclerAddon(tooltip);
            case "addon_concealer" -> appendConcealerAddon(tooltip);
            case "addon_fake_drops" -> appendFakeDropsAddon(tooltip);
            case "upgrade_fire_rate" -> appendUpgradeTooltip(tooltip, UpgradeItem.Type.FIRE_RATE);
            case "upgrade_efficiency" -> appendUpgradeTooltip(tooltip, UpgradeItem.Type.EFFICIENCY);
            case "upgrade_range" -> appendUpgradeTooltip(tooltip, UpgradeItem.Type.RANGE);
            case "upgrade_accuracy" -> appendUpgradeTooltip(tooltip, UpgradeItem.Type.ACCURACY);
            case "upgrade_scatter_shot" -> appendUpgradeTooltip(tooltip, UpgradeItem.Type.SCATTER_SHOT);
            case "bullet" -> appendAmmoTooltip(tooltip, "turret.ammo.bullet", null);
            case "grenade" -> appendAmmoTooltip(tooltip, "turret.ammo.grenade", null);
            case "blazing_clay" -> appendAmmoTooltip(tooltip, "turret.ammo.blazing_clay", null);
            case "ferro_slug" -> appendAmmoTooltip(tooltip, "turret.ammo.ferro_slug", null);
            case "rocket" -> appendAmmoTooltip(tooltip, "turret.ammo.rocket", null);
            case "memory_card" -> appendMemoryCardTooltip(tooltip);
            case "lever_block" -> appendLeverTooltip(tooltip);
            case "io_bus", "energetic_barrel" -> appendComponentTooltip(tooltip);
            default -> {
                if (itemPath.startsWith("sensor_tier_")
                        || itemPath.startsWith("chamber_tier_")
                        || itemPath.startsWith("barrel_tier_")) {
                    appendComponentTooltip(tooltip);
                }
            }
        }
    }

    private static TurretKind turretKindFromPath(String itemPath) {
        return switch (itemPath) {
            case "gun_turret" -> TurretKind.GUN;
            case "grenade_turret" -> TurretKind.GRENADE;
            case "disposable_item_turret" -> TurretKind.DISPOSABLE_ITEM;
            case "potato_cannon_turret" -> TurretKind.POTATO_CANNON;
            case "incendiary_turret" -> TurretKind.INCENDIARY;
            case "rocket_turret" -> TurretKind.ROCKET;
            case "relativistic_turret" -> TurretKind.RELATIVISTIC;
            case "teleporter_turret" -> TurretKind.TELEPORTER;
            case "laser_turret" -> TurretKind.LASER;
            case "rail_gun_turret" -> TurretKind.RAIL_GUN;
            case "plasma_turret" -> TurretKind.PLASMA;
            case "arc_turret" -> TurretKind.ARC;
            case "melee_turret" -> TurretKind.MELEE;
            case "crossbow_turret" -> TurretKind.CROSSBOW;
            default -> null;
        };
    }

    private static void appendTurretTooltip(TurretKind kind, List<Component> tooltip) {
        addDescription(tooltip, "gui.turret_head_description");
        addStat(tooltip, "tier_required", String.valueOf(kind.getMinTier()));
        addStat(tooltip, "range", formatBlocks(kind.getRange()));
        addStat(tooltip, "damage.stat", formatHearts(kind.getDamage()));
        addStat(tooltip, "energy.stat", kind.getEnergyPerShot() + " FE");
        addStat(tooltip, "gui.rof", formatRateOfFire(kind.getCooldown()));
        appendAmmoType(tooltip, kind);
        appendTurretFlavour(kind, tooltip);
    }

    private static void appendAmmoType(List<Component> tooltip, TurretKind kind) {
        String ammoKey = switch (kind) {
            case DISPOSABLE_ITEM -> "turret.ammo.0";
            case POTATO_CANNON -> "turret.ammo.6";
            case GUN, RELATIVISTIC -> "turret.ammo.1";
            case GRENADE -> "turret.ammo.2";
            case ROCKET -> "turret.ammo.3";
            case RAIL_GUN -> "turret.ammo.5";
            case INCENDIARY -> "turret.ammo.7";
            case LASER, PLASMA, ARC, CROSSBOW, TELEPORTER, MELEE -> "turret.ammo.4";
        };
        addStat(tooltip, "ammo", Component.translatable(PREFIX + ammoKey).getString());
    }

    private static void appendBaseTooltip(int tier, List<Component> tooltip) {
        addDescription(tooltip, "base.tier." + tier);
        addDescription(tooltip, "gui.turret_base_description");
        addDescription(tooltip, "base_max_range");
        addStat(tooltip, "energy.label", (20000 * tier * tier) + " FE");
        addDescription(tooltip, "extras.label");
        if (tier >= 2) {
            addDescription(tooltip, "extras.addons.2");
            addDescription(tooltip, "extras.upgrade." + (tier >= 5 ? 2 : 1));
        } else {
            addDescription(tooltip, "extras.addons.0");
            addDescription(tooltip, "extras.upgrade.0");
        }
        if (tier >= 1 && tier <= 5) {
            addFlavour(tooltip, "flavour.base." + tier);
            if (tier == 5) {
                addFlavour(tooltip, "flavour.base.5b");
            }
        }
    }

    private static void appendPowerExpanderTooltip(int tier, List<Component> tooltip) {
        addDescription(tooltip, "expander.power1");
        tooltip.add(Component.translatable(PREFIX + "expander.power2")
                .withStyle(DESCRIPTION)
                .append(Component.literal(" " + tier * 10_000 + " ")
                        .withStyle(STAT_VALUE))
                .append(Component.translatable(PREFIX + "expander.power3").withStyle(DESCRIPTION)));
        addFlavour(tooltip, "flavour.expander.power." + tier);
    }

    private static void appendInventoryExpanderTooltip(int tier, List<Component> tooltip) {
        addDescription(tooltip, "expander.inv1");
        addDescription(tooltip, "expander.inv2");
        tooltip.add(Component.translatable(PREFIX + "expander.inv3")
                .withStyle(DESCRIPTION)
                .append(Component.literal(" " + tier * 16).withStyle(STAT_VALUE)));
        addFlavour(tooltip, "flavour.expander.inv." + tier);
    }

    private static void appendSolarAddon(List<Component> tooltip) {
        addDescription(tooltip, "turret.addon.label");
        tooltip.add(Component.translatable(PREFIX + "turret.addon.solar.a")
                .withStyle(DESCRIPTION)
                .append(Component.literal(" " + ModConfig.SOLAR_GENERATION.get() + " ")
                        .withStyle(STAT_VALUE))
                .append(Component.translatable(PREFIX + "turret.addon.solar.b").withStyle(DESCRIPTION)));
        addFlavour(tooltip, "turret.addon.solar.flavour");
    }

    private static void appendRedstoneAddon(List<Component> tooltip) {
        addDescription(tooltip, "turret.addon.label");
        tooltip.add(Component.translatable(PREFIX + "turret.addon.redstone.a")
                .withStyle(DESCRIPTION)
                .append(Component.literal(" 4 FE/t ")
                        .withStyle(STAT_VALUE))
                .append(Component.translatable(PREFIX + "turret.addon.redstone.b").withStyle(DESCRIPTION))
                .append(Component.translatable(PREFIX + "turret.addon.redstone.c").withStyle(DESCRIPTION)));
        addFlavour(tooltip, "turret.addon.redstone.flavour");
    }

    private static void appendLootDeleterAddon(List<Component> tooltip) {
        addDescription(tooltip, "base_addon_loot_deleter.inv1");
        addDescription(tooltip, "base_addon_loot_deleter.inv2");
        addDescription(tooltip, "base_addon_loot_deleter.inv3");
        addFlavour(tooltip, "flavour.base_addon_loot_deleter.inv.1");
    }

    private static void appendDamageAmpAddon(List<Component> tooltip) {
        addDescription(tooltip, "turret.addon.label");
        addDescription(tooltip, "turret.addon.amp.a.label");
        addDescription(tooltip, "turret.addon.amp.b.label");
        addStat(tooltip, "gui.damage_amp", "x1.5");
        addFlavour(tooltip, "turret.addon.amp.flavour");
    }

    private static void appendPotentiaAddon(List<Component> tooltip) {
        addDescription(tooltip, "turret.addon.label");
        addDescription(tooltip, "turret.addon.potentia.a");
        addDescription(tooltip, "turret.addon.potentia.b");
        addDescription(tooltip, "turret.addon.potentia.c");
        addDescription(tooltip, "turret.addon.potentia.d");
        addDescription(tooltip, "turret.addon.potentia.e");
        addFlavour(tooltip, "turret.addon.potentia.flavour");
    }

    private static void appendSerialAddon(List<Component> tooltip) {
        addDescription(tooltip, "turret.addon.label");
        addDescription(tooltip, "turret.addon.serial.a");
        addDescription(tooltip, "turret.addon.serial.b");
        addFlavour(tooltip, "turret.addon.serial.flavour");
    }

    private static void appendRecyclerAddon(List<Component> tooltip) {
        addDescription(tooltip, "turret.addon.label");
        addDescription(tooltip, "turret.addon.recycler.1");
        addDescription(tooltip, "turret.addon.recycler.2");
        tooltip.add(Component.translatable(PREFIX + "turret.addon.recycler.3")
                .withStyle(DESCRIPTION)
                .append(Component.literal(" 25%")
                        .withStyle(STAT_VALUE)));
        addFlavour(tooltip, "turret.addon.recycler.flavour");
    }

    private static void appendConcealerAddon(List<Component> tooltip) {
        addDescription(tooltip, "turret.addon.label");
        addDescription(tooltip, "turret.addon.concealer.1");
        addFlavour(tooltip, "turret.addon.concealer.flavour");
    }

    private static void appendFakeDropsAddon(List<Component> tooltip) {
        addDescription(tooltip, "turret.addon.label");
        addDescription(tooltip, "turret.addon.fakedrops.a");
        addDescription(tooltip, "turret.addon.fakedrops.b");
        addFlavour(tooltip, "turret.addon.fakedrops.flavour");
    }

    private static void appendUpgradeTooltip(List<Component> tooltip, UpgradeItem.Type type) {
        addDescription(tooltip, "turret.upgrade.label");
        addDescription(tooltip, "turret.upgrade.stacks");
        addDescription(tooltip, "turret.upgrade.turretinfo");
        switch (type) {
            case FIRE_RATE -> {
                addDescription(tooltip, "turret.upgrade.rof");
                addFlavour(tooltip, "turret.upgrade.rof.flavour");
            }
            case EFFICIENCY -> {
                addDescription(tooltip, "turret.upgrade.eff");
                addFlavour(tooltip, "turret.upgrade.eff.flavour");
            }
            case RANGE -> {
                addDescription(tooltip, "turret.upgrade.range");
                addFlavour(tooltip, "turret.upgrade.range.flavour");
            }
            case ACCURACY -> {
                addDescription(tooltip, "turret.upgrade.acc");
                addFlavour(tooltip, "turret.upgrade.acc.flavour.a");
                addFlavour(tooltip, "turret.upgrade.acc.flavour.b");
            }
            case SCATTER_SHOT -> {
                addDescription(tooltip, "turret.upgrade.scatter.a");
                addDescription(tooltip, "turret.upgrade.scatter.b");
                addFlavour(tooltip, "turret.upgrade.scatter.flavour");
            }
        }
    }

    private static void appendAmmoTooltip(List<Component> tooltip, String descriptionKey, String flavourKey) {
        addDescription(tooltip, "turret.ammo.label");
        addDescription(tooltip, descriptionKey);
        if (flavourKey != null) {
            addFlavour(tooltip, flavourKey);
        }
    }

    private static void appendMemoryCardTooltip(List<Component> tooltip) {
        addDescription(tooltip, "memory_card.desc1");
        addDescription(tooltip, "memory_card.desc2");
        addDescription(tooltip, "memory_card.desc3");
    }

    private static void appendLeverTooltip(List<Component> tooltip) {
        addDescription(tooltip, "gui.turret_lever_description");
        addStat(tooltip, "energy.stat", ModConfig.LEVER_GENERATION.get() + " FE");
    }

    private static void appendComponentTooltip(List<Component> tooltip) {
        addDescription(tooltip, "component.generic");
    }

    private static void appendTurretFlavour(TurretKind kind, List<Component> tooltip) {
        switch (kind) {
            case DISPOSABLE_ITEM -> addFlavour(tooltip, "flavour.turret.0");
            case GUN -> addFlavour(tooltip, "flavour.turret.1");
            case GRENADE -> {
                addFlavour(tooltip, "flavour.turret.2a");
                addFlavour(tooltip, "flavour.turret.2b");
            }
            case INCENDIARY -> addFlavour(tooltip, "flavour.turret.7");
            case ROCKET -> addFlavour(tooltip, "flavour.turret.5");
            case RELATIVISTIC -> addFlavour(tooltip, "flavour.turret.8");
            case TELEPORTER -> addFlavour(tooltip, "flavour.turret.9a");
            case LASER -> addFlavour(tooltip, "flavour.turret.3");
            case RAIL_GUN -> addFlavour(tooltip, "flavour.turret.4");
            case PLASMA -> addFlavour(tooltip, "flavour.turret.plasma");
            case ARC -> addFlavour(tooltip, "flavour.turret.arc");
            case MELEE -> addFlavour(tooltip, "flavour.turret.melee");
            case POTATO_CANNON -> addFlavour(tooltip, "flavour.turret.6");
            case CROSSBOW -> addFlavour(tooltip, "flavour.turret.2a");
        }
    }

    private static void addDescription(List<Component> tooltip, String key) {
        tooltip.add(Component.translatable(PREFIX + key).withStyle(DESCRIPTION));
    }

    private static void addFlavour(List<Component> tooltip, String key) {
        tooltip.add(Component.translatable(PREFIX + key).withStyle(FLAVOUR));
    }

    private static void addStat(List<Component> tooltip, String labelKey, String value) {
        tooltip.add(labelLine(labelKey, value));
    }

    private static MutableComponent labelLine(String labelKey, String value) {
        return Component.translatable(PREFIX + labelKey)
                .withStyle(STAT_LABEL)
                .append(Component.literal(": " + value).withStyle(STAT_VALUE));
    }

    private static String formatBlocks(double range) {
        if (range == Math.rint(range)) {
            return String.valueOf((int) range);
        }
        return String.format(Locale.ROOT, "%.1f", range);
    }

    private static String formatHearts(float damage) {
        float hearts = damage / 2.0F;
        if (hearts == Math.rint(hearts)) {
            return ((int) hearts) + " " + Component.translatable(PREFIX + "health").getString();
        }
        return String.format(Locale.ROOT, "%.1f ", hearts)
                + Component.translatable(PREFIX + "health").getString();
    }

    private static String formatRateOfFire(int cooldownTicks) {
        if (cooldownTicks <= 0) {
            return "?";
        }
        float shotsPerSecond = 20.0F / cooldownTicks;
        if (shotsPerSecond >= 10.0F) {
            return String.format(Locale.ROOT, "%.0f/s", shotsPerSecond);
        }
        return String.format(Locale.ROOT, "%.1f/s", shotsPerSecond);
    }

    private static int parseTier(String itemPath, String prefix) {
        try {
            return Integer.parseInt(itemPath.substring(prefix.length()));
        } catch (NumberFormatException ignored) {
            return 1;
        }
    }
}
