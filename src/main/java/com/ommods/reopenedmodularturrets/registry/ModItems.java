package com.ommods.reopenedmodularturrets.registry;

import com.ommods.reopenedmodularturrets.ModConstants;
import com.ommods.reopenedmodularturrets.item.AmmoItem;
import com.ommods.reopenedmodularturrets.item.AmmoType;
import com.ommods.reopenedmodularturrets.item.BulletItem;
import com.ommods.reopenedmodularturrets.item.GrenadeItem;
import com.ommods.reopenedmodularturrets.item.MemoryCardItem;
import com.ommods.reopenedmodularturrets.item.UpgradeItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ModConstants.MOD_ID);

    public static final DeferredItem<BlockItem> TURRET_BASE_TIER_1_ITEM = ITEMS.registerSimpleBlockItem("turret_base_tier_1", ModBlocks.TURRET_BASE_TIER_1);
    public static final DeferredItem<BlockItem> TURRET_BASE_TIER_2_ITEM = ITEMS.registerSimpleBlockItem("turret_base_tier_2", ModBlocks.TURRET_BASE_TIER_2);
    public static final DeferredItem<BlockItem> TURRET_BASE_TIER_3_ITEM = ITEMS.registerSimpleBlockItem("turret_base_tier_3", ModBlocks.TURRET_BASE_TIER_3);
    public static final DeferredItem<BlockItem> TURRET_BASE_TIER_4_ITEM = ITEMS.registerSimpleBlockItem("turret_base_tier_4", ModBlocks.TURRET_BASE_TIER_4);
    public static final DeferredItem<BlockItem> TURRET_BASE_TIER_5_ITEM = ITEMS.registerSimpleBlockItem("turret_base_tier_5", ModBlocks.TURRET_BASE_TIER_5);

    public static final DeferredItem<BlockItem> DISPOSABLE_ITEM_TURRET_ITEM = ITEMS.registerSimpleBlockItem("disposable_item_turret", ModBlocks.DISPOSABLE_ITEM_TURRET);
    public static final DeferredItem<BlockItem> POTATO_CANNON_TURRET_ITEM = ITEMS.registerSimpleBlockItem("potato_cannon_turret", ModBlocks.POTATO_CANNON_TURRET);
    public static final DeferredItem<BlockItem> GUN_TURRET_ITEM = ITEMS.registerSimpleBlockItem("gun_turret", ModBlocks.GUN_TURRET);
    public static final DeferredItem<BlockItem> GRENADE_TURRET_ITEM = ITEMS.registerSimpleBlockItem("grenade_turret", ModBlocks.GRENADE_TURRET);
    public static final DeferredItem<BlockItem> INCENDIARY_TURRET_ITEM = ITEMS.registerSimpleBlockItem("incendiary_turret", ModBlocks.INCENDIARY_TURRET);
    public static final DeferredItem<BlockItem> ROCKET_TURRET_ITEM = ITEMS.registerSimpleBlockItem("rocket_turret", ModBlocks.ROCKET_TURRET);
    public static final DeferredItem<BlockItem> RELATIVISTIC_TURRET_ITEM = ITEMS.registerSimpleBlockItem("relativistic_turret", ModBlocks.RELATIVISTIC_TURRET);
    public static final DeferredItem<BlockItem> TELEPORTER_TURRET_ITEM = ITEMS.registerSimpleBlockItem("teleporter_turret", ModBlocks.TELEPORTER_TURRET);
    public static final DeferredItem<BlockItem> LASER_TURRET_ITEM = ITEMS.registerSimpleBlockItem("laser_turret", ModBlocks.LASER_TURRET);
    public static final DeferredItem<BlockItem> RAIL_GUN_TURRET_ITEM = ITEMS.registerSimpleBlockItem("rail_gun_turret", ModBlocks.RAIL_GUN_TURRET);
    public static final DeferredItem<BlockItem> PLASMA_TURRET_ITEM = ITEMS.registerSimpleBlockItem("plasma_turret", ModBlocks.PLASMA_TURRET);
    public static final DeferredItem<BlockItem> ARC_TURRET_ITEM = ITEMS.registerSimpleBlockItem("arc_turret", ModBlocks.ARC_TURRET);
    public static final DeferredItem<BlockItem> MELEE_TURRET_ITEM = ITEMS.registerSimpleBlockItem("melee_turret", ModBlocks.MELEE_TURRET);
    public static final DeferredItem<BlockItem> CROSSBOW_TURRET_ITEM = ITEMS.registerSimpleBlockItem("crossbow_turret", ModBlocks.CROSSBOW_TURRET);
    public static final DeferredItem<Item> SOLAR_ADDON_ITEM = registerComponent("addon_solar");
    public static final DeferredItem<Item> REDSTONE_REACTOR_ADDON_ITEM = registerComponent("addon_redstone_reactor");
    public static final DeferredItem<BlockItem> LEVER_BLOCK_ITEM = ITEMS.registerSimpleBlockItem("lever_block", ModBlocks.LEVER_BLOCK);
    public static final DeferredItem<Item> BASE_ADDON_LOOT_DELETER_ITEM = registerComponent("base_addon_loot_deleter");

    public static final DeferredItem<BlockItem> EXPANDER_POWER_TIER_1_ITEM = ITEMS.registerSimpleBlockItem("expander_power_tier_1", ModBlocks.EXPANDER_POWER_TIER_1);
    public static final DeferredItem<BlockItem> EXPANDER_POWER_TIER_2_ITEM = ITEMS.registerSimpleBlockItem("expander_power_tier_2", ModBlocks.EXPANDER_POWER_TIER_2);
    public static final DeferredItem<BlockItem> EXPANDER_POWER_TIER_3_ITEM = ITEMS.registerSimpleBlockItem("expander_power_tier_3", ModBlocks.EXPANDER_POWER_TIER_3);
    public static final DeferredItem<BlockItem> EXPANDER_POWER_TIER_4_ITEM = ITEMS.registerSimpleBlockItem("expander_power_tier_4", ModBlocks.EXPANDER_POWER_TIER_4);
    public static final DeferredItem<BlockItem> EXPANDER_POWER_TIER_5_ITEM = ITEMS.registerSimpleBlockItem("expander_power_tier_5", ModBlocks.EXPANDER_POWER_TIER_5);
    public static final DeferredItem<BlockItem> EXPANDER_INV_TIER_1_ITEM = ITEMS.registerSimpleBlockItem("expander_inv_tier_1", ModBlocks.EXPANDER_INV_TIER_1);
    public static final DeferredItem<BlockItem> EXPANDER_INV_TIER_2_ITEM = ITEMS.registerSimpleBlockItem("expander_inv_tier_2", ModBlocks.EXPANDER_INV_TIER_2);
    public static final DeferredItem<BlockItem> EXPANDER_INV_TIER_3_ITEM = ITEMS.registerSimpleBlockItem("expander_inv_tier_3", ModBlocks.EXPANDER_INV_TIER_3);
    public static final DeferredItem<BlockItem> EXPANDER_INV_TIER_4_ITEM = ITEMS.registerSimpleBlockItem("expander_inv_tier_4", ModBlocks.EXPANDER_INV_TIER_4);
    public static final DeferredItem<BlockItem> EXPANDER_INV_TIER_5_ITEM = ITEMS.registerSimpleBlockItem("expander_inv_tier_5", ModBlocks.EXPANDER_INV_TIER_5);

    public static final DeferredItem<BulletItem> BULLET = ITEMS.registerItem("bullet", BulletItem::new);
    public static final DeferredItem<GrenadeItem> GRENADE = ITEMS.registerItem("grenade", GrenadeItem::new);
    public static final DeferredItem<AmmoItem> BLAZING_CLAY = ITEMS.registerItem("blazing_clay", props -> new AmmoItem(AmmoType.BLAZING_CLAY, props));
    public static final DeferredItem<AmmoItem> FERRO_SLUG = ITEMS.registerItem("ferro_slug", props -> new AmmoItem(AmmoType.FERRO_SLUG, props));
    public static final DeferredItem<AmmoItem> ROCKET = ITEMS.registerItem("rocket", props -> new AmmoItem(AmmoType.ROCKET, props));

    public static final DeferredItem<Item> SENSOR_TIER_1 = registerComponent("sensor_tier_1");
    public static final DeferredItem<Item> SENSOR_TIER_2 = registerComponent("sensor_tier_2");
    public static final DeferredItem<Item> SENSOR_TIER_3 = registerComponent("sensor_tier_3");
    public static final DeferredItem<Item> SENSOR_TIER_4 = registerComponent("sensor_tier_4");
    public static final DeferredItem<Item> SENSOR_TIER_5 = registerComponent("sensor_tier_5");
    public static final DeferredItem<Item> CHAMBER_TIER_1 = registerComponent("chamber_tier_1");
    public static final DeferredItem<Item> CHAMBER_TIER_2 = registerComponent("chamber_tier_2");
    public static final DeferredItem<Item> CHAMBER_TIER_3 = registerComponent("chamber_tier_3");
    public static final DeferredItem<Item> CHAMBER_TIER_4 = registerComponent("chamber_tier_4");
    public static final DeferredItem<Item> CHAMBER_TIER_5 = registerComponent("chamber_tier_5");
    public static final DeferredItem<Item> BARREL_TIER_1 = registerComponent("barrel_tier_1");
    public static final DeferredItem<Item> BARREL_TIER_2 = registerComponent("barrel_tier_2");
    public static final DeferredItem<Item> BARREL_TIER_3 = registerComponent("barrel_tier_3");
    public static final DeferredItem<Item> BARREL_TIER_4 = registerComponent("barrel_tier_4");
    public static final DeferredItem<Item> BARREL_TIER_5 = registerComponent("barrel_tier_5");
    public static final DeferredItem<Item> IO_BUS = registerComponent("io_bus");
    public static final DeferredItem<Item> ENERGETIC_BARREL = registerComponent("energetic_barrel");
    public static final DeferredItem<MemoryCardItem> MEMORY_CARD = ITEMS.registerItem("memory_card", MemoryCardItem::new);

    public static final DeferredItem<Item> ADDON_DAMAGE_AMP = registerComponent("addon_damage_amp");
    public static final DeferredItem<Item> ADDON_POTENTIA = registerComponent("addon_potentia");
    public static final DeferredItem<Item> ADDON_SERIAL_PORT = registerComponent("addon_serial_port");
    public static final DeferredItem<Item> ADDON_RECYCLER = registerComponent("addon_recycler");
    public static final DeferredItem<Item> ADDON_CONCEALER = registerComponent("addon_concealer");
    public static final DeferredItem<Item> ADDON_FAKE_DROPS = registerComponent("addon_fake_drops");

    public static final DeferredItem<UpgradeItem> UPGRADE_FIRE_RATE = registerUpgrade("upgrade_fire_rate", UpgradeItem.Type.FIRE_RATE);
    public static final DeferredItem<UpgradeItem> UPGRADE_EFFICIENCY = registerUpgrade("upgrade_efficiency", UpgradeItem.Type.EFFICIENCY);
    public static final DeferredItem<UpgradeItem> UPGRADE_RANGE = registerUpgrade("upgrade_range", UpgradeItem.Type.RANGE);
    public static final DeferredItem<UpgradeItem> UPGRADE_ACCURACY = registerUpgrade("upgrade_accuracy", UpgradeItem.Type.ACCURACY);
    public static final DeferredItem<UpgradeItem> UPGRADE_SCATTER_SHOT = registerUpgrade("upgrade_scatter_shot", UpgradeItem.Type.SCATTER_SHOT);

    private static DeferredItem<Item> registerComponent(String name) {
        return ITEMS.registerSimpleItem(name, new Item.Properties());
    }

    private static DeferredItem<UpgradeItem> registerUpgrade(String name, UpgradeItem.Type type) {
        return ITEMS.registerItem(name, props -> new UpgradeItem(type, props));
    }

    private ModItems() {}
}
