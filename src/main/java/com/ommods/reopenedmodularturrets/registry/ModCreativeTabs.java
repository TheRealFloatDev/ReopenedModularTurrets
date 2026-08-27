package com.ommods.reopenedmodularturrets.registry;

import com.ommods.reopenedmodularturrets.ModConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ModConstants.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN = CREATIVE_TABS.register("main", () ->
            CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.reopenedmodularturrets"))
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .icon(() -> ModItems.TURRET_BASE_TIER_1_ITEM.get().getDefaultInstance())
                    .displayItems((params, output) -> {
                        output.accept(ModItems.TURRET_BASE_TIER_1_ITEM.get());
                        output.accept(ModItems.TURRET_BASE_TIER_2_ITEM.get());
                        output.accept(ModItems.TURRET_BASE_TIER_3_ITEM.get());
                        output.accept(ModItems.TURRET_BASE_TIER_4_ITEM.get());
                        output.accept(ModItems.TURRET_BASE_TIER_5_ITEM.get());
                        output.accept(ModItems.DISPOSABLE_ITEM_TURRET_ITEM.get());
                        output.accept(ModItems.POTATO_CANNON_TURRET_ITEM.get());
                        output.accept(ModItems.GUN_TURRET_ITEM.get());
                        output.accept(ModItems.GRENADE_TURRET_ITEM.get());
                        output.accept(ModItems.INCENDIARY_TURRET_ITEM.get());
                        output.accept(ModItems.ROCKET_TURRET_ITEM.get());
                        output.accept(ModItems.RELATIVISTIC_TURRET_ITEM.get());
                        output.accept(ModItems.TELEPORTER_TURRET_ITEM.get());
                        output.accept(ModItems.LASER_TURRET_ITEM.get());
                        output.accept(ModItems.RAIL_GUN_TURRET_ITEM.get());
                        output.accept(ModItems.SOLAR_ADDON_ITEM.get());
                        output.accept(ModItems.REDSTONE_REACTOR_ADDON_ITEM.get());
                        output.accept(ModItems.BASE_ADDON_LOOT_DELETER_ITEM.get());
                        output.accept(ModItems.LEVER_BLOCK_ITEM.get());
                        output.accept(ModItems.MEMORY_CARD.get());
                        output.accept(ModItems.EXPANDER_POWER_TIER_1_ITEM.get());
                        output.accept(ModItems.EXPANDER_POWER_TIER_2_ITEM.get());
                        output.accept(ModItems.EXPANDER_POWER_TIER_3_ITEM.get());
                        output.accept(ModItems.EXPANDER_POWER_TIER_4_ITEM.get());
                        output.accept(ModItems.EXPANDER_POWER_TIER_5_ITEM.get());
                        output.accept(ModItems.EXPANDER_INV_TIER_1_ITEM.get());
                        output.accept(ModItems.EXPANDER_INV_TIER_2_ITEM.get());
                        output.accept(ModItems.EXPANDER_INV_TIER_3_ITEM.get());
                        output.accept(ModItems.EXPANDER_INV_TIER_4_ITEM.get());
                        output.accept(ModItems.EXPANDER_INV_TIER_5_ITEM.get());
                        output.accept(ModItems.BULLET.get());
                        output.accept(ModItems.GRENADE.get());
                        output.accept(ModItems.BLAZING_CLAY.get());
                        output.accept(ModItems.FERRO_SLUG.get());
                        output.accept(ModItems.ROCKET.get());
                        output.accept(ModItems.UPGRADE_FIRE_RATE.get());
                        output.accept(ModItems.UPGRADE_EFFICIENCY.get());
                        output.accept(ModItems.UPGRADE_RANGE.get());
                        output.accept(ModItems.UPGRADE_ACCURACY.get());
                        output.accept(ModItems.UPGRADE_SCATTER_SHOT.get());
                        output.accept(ModItems.ADDON_DAMAGE_AMP.get());
                        output.accept(ModItems.ADDON_POTENTIA.get());
                        output.accept(ModItems.ADDON_RECYCLER.get());
                        output.accept(ModItems.ADDON_CONCEALER.get());
                        output.accept(ModItems.ADDON_FAKE_DROPS.get());
                        output.accept(ModItems.ADDON_SERIAL_PORT.get());
                        output.accept(ModItems.IO_BUS.get());
                        output.accept(ModItems.ENERGETIC_BARREL.get());
                        for (int tier = 1; tier <= 5; tier++) {
                            output.accept(component("sensor_tier_" + tier));
                            output.accept(component("chamber_tier_" + tier));
                            output.accept(component("barrel_tier_" + tier));
                        }
                    })
                    .build());

    private static ItemStack component(String name) {
        return ModItems.ITEMS.getEntries().stream()
                .filter(holder -> holder.getId().getPath().equals(name))
                .map(holder -> new ItemStack(holder.get()))
                .findFirst()
                .orElse(ItemStack.EMPTY);
    }

    private ModCreativeTabs() {}
}
