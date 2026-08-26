package com.ommods.reopenedmodularturrets.registry;

import com.ommods.reopenedmodularturrets.ModConstants;
import com.ommods.reopenedmodularturrets.item.BulletItem;
import com.ommods.reopenedmodularturrets.item.GrenadeItem;
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
    public static final DeferredItem<BlockItem> GUN_TURRET_ITEM = ITEMS.registerSimpleBlockItem("gun_turret", ModBlocks.GUN_TURRET);
    public static final DeferredItem<BlockItem> GRENADE_TURRET_ITEM = ITEMS.registerSimpleBlockItem("grenade_turret", ModBlocks.GRENADE_TURRET);
    public static final DeferredItem<BlockItem> SOLAR_ADDON_ITEM = ITEMS.registerSimpleBlockItem("addon_solar", ModBlocks.SOLAR_ADDON);

    public static final DeferredItem<BulletItem> BULLET = ITEMS.registerItem("bullet", BulletItem::new);
    public static final DeferredItem<GrenadeItem> GRENADE = ITEMS.registerItem("grenade", GrenadeItem::new);

    private ModItems() {}
}
