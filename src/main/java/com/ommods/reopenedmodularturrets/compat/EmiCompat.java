package com.ommods.reopenedmodularturrets.compat;

import com.ommods.reopenedmodularturrets.ModConstants;
import com.ommods.reopenedmodularturrets.registry.ModItems;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiInfoRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.List;

@EmiEntrypoint
public class EmiCompat implements EmiPlugin {
    private static final TagKey<Item> IO_BUS_TAG = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "io_bus"));

    @Override
    public void register(EmiRegistry registry) {
        registry.addAlias(EmiIngredient.of(IO_BUS_TAG), Component.translatable("tag.reopenedmodularturrets.io_bus"));

        describe(registry, ModItems.GUN_TURRET_ITEM.get(), "gun_turret", "jei.reopenedmodularturrets.gun_turret");
        describe(registry, ModItems.GRENADE_TURRET_ITEM.get(), "grenade_turret", "jei.reopenedmodularturrets.grenade_turret");
        describe(registry, ModItems.ROCKET_TURRET_ITEM.get(), "rocket_turret", "jei.reopenedmodularturrets.rocket_turret");
        describe(registry, ModItems.LASER_TURRET_ITEM.get(), "laser_turret", "jei.reopenedmodularturrets.laser_turret");
        describe(registry, ModItems.RAIL_GUN_TURRET_ITEM.get(), "rail_gun_turret", "jei.reopenedmodularturrets.rail_gun_turret");
        describe(registry, ModItems.TURRET_BASE_TIER_1_ITEM.get(), "turret_base_tier_1", "jei.reopenedmodularturrets.turret_base");
        describe(registry, ModItems.SOLAR_ADDON_ITEM.get(), "addon_solar", "jei.reopenedmodularturrets.addon_solar");
        describe(registry, ModItems.REDSTONE_REACTOR_ADDON_ITEM.get(), "addon_redstone_reactor", "jei.reopenedmodularturrets.addon_redstone_reactor");
        describe(registry, ModItems.BASE_ADDON_LOOT_DELETER_ITEM.get(), "base_addon_loot_deleter", "jei.reopenedmodularturrets.base_addon_loot_deleter");
        describe(registry, ModItems.MEMORY_CARD.get(), "memory_card", "jei.reopenedmodularturrets.memory_card");
        describe(registry, ModItems.LEVER_BLOCK_ITEM.get(), "lever_block", "jei.reopenedmodularturrets.lever_block");
        describe(registry, ModItems.UPGRADE_FIRE_RATE.get(), "upgrade_fire_rate", "jei.reopenedmodularturrets.upgrade_fire_rate");
        describe(registry, ModItems.UPGRADE_EFFICIENCY.get(), "upgrade_efficiency", "jei.reopenedmodularturrets.upgrade_efficiency");
        describe(registry, ModItems.UPGRADE_RANGE.get(), "upgrade_range", "jei.reopenedmodularturrets.upgrade_range");
        describe(registry, ModItems.UPGRADE_ACCURACY.get(), "upgrade_accuracy", "jei.reopenedmodularturrets.upgrade_accuracy");
        describe(registry, ModItems.UPGRADE_SCATTER_SHOT.get(), "upgrade_scatter_shot", "jei.reopenedmodularturrets.upgrade_scatter_shot");
        describe(registry, ModItems.ADDON_DAMAGE_AMP.get(), "addon_damage_amp", "jei.reopenedmodularturrets.addon_damage_amp");
        describe(registry, ModItems.ADDON_POTENTIA.get(), "addon_potentia", "jei.reopenedmodularturrets.addon_potentia");
        describe(registry, ModItems.ADDON_RECYCLER.get(), "addon_recycler", "jei.reopenedmodularturrets.addon_recycler");
        describe(registry, ModItems.ADDON_CONCEALER.get(), "addon_concealer", "jei.reopenedmodularturrets.addon_concealer");
        describe(registry, ModItems.ADDON_FAKE_DROPS.get(), "addon_fake_drops", "jei.reopenedmodularturrets.addon_fake_drops");
        describe(registry, ModItems.ADDON_SERIAL_PORT.get(), "addon_serial_port", "jei.reopenedmodularturrets.addon_serial_port");
        describe(registry, ModItems.BULLET.get(), "bullet", "jei.reopenedmodularturrets.bullet");
        describe(registry, ModItems.GRENADE.get(), "grenade", "jei.reopenedmodularturrets.grenade");
        describe(registry, ModItems.ROCKET.get(), "rocket", "jei.reopenedmodularturrets.rocket");
        for (DeferredHolder<Item, ? extends Item> holder : ModItems.ITEMS.getEntries()) {
            String path = holder.getId().getPath();
            if (path.startsWith("expander_")) {
                describe(registry, holder.get(), path, "jei.reopenedmodularturrets.expander");
            }
        }
    }

    private static void describe(EmiRegistry registry, Item item, String path, String key) {
        registry.addRecipe(new EmiInfoRecipe(
                List.of(EmiStack.of(new ItemStack(item))),
                List.of(Component.translatable(key)),
                syntheticId("info/" + path)
        ));
    }

    private static ResourceLocation syntheticId(String path) {
        return ResourceLocation.parse(ModConstants.MOD_ID + ":/" + path);
    }
}
