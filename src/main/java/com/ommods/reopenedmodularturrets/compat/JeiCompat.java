package com.ommods.reopenedmodularturrets.compat;

import com.ommods.reopenedmodularturrets.ModConstants;
import com.ommods.reopenedmodularturrets.registry.ModItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;

@JeiPlugin
public class JeiCompat implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "jei");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        info(registration, ModItems.GUN_TURRET_ITEM.get(), "jei.reopenedmodularturrets.gun_turret");
        info(registration, ModItems.GRENADE_TURRET_ITEM.get(), "jei.reopenedmodularturrets.grenade_turret");
        info(registration, ModItems.ROCKET_TURRET_ITEM.get(), "jei.reopenedmodularturrets.rocket_turret");
        info(registration, ModItems.LASER_TURRET_ITEM.get(), "jei.reopenedmodularturrets.laser_turret");
        info(registration, ModItems.RAIL_GUN_TURRET_ITEM.get(), "jei.reopenedmodularturrets.rail_gun_turret");
        info(registration, ModItems.TURRET_BASE_TIER_1_ITEM.get(), "jei.reopenedmodularturrets.turret_base");
        info(registration, ModItems.SOLAR_ADDON_ITEM.get(), "jei.reopenedmodularturrets.addon_solar");
        info(registration, ModItems.REDSTONE_REACTOR_ADDON_ITEM.get(), "jei.reopenedmodularturrets.addon_redstone_reactor");
        info(registration, ModItems.BASE_ADDON_LOOT_DELETER_ITEM.get(), "jei.reopenedmodularturrets.base_addon_loot_deleter");
        info(registration, ModItems.MEMORY_CARD.get(), "jei.reopenedmodularturrets.memory_card");
        info(registration, ModItems.LEVER_BLOCK_ITEM.get(), "jei.reopenedmodularturrets.lever_block");
        info(registration, ModItems.UPGRADE_FIRE_RATE.get(), "jei.reopenedmodularturrets.upgrade_fire_rate");
        info(registration, ModItems.UPGRADE_EFFICIENCY.get(), "jei.reopenedmodularturrets.upgrade_efficiency");
        info(registration, ModItems.UPGRADE_RANGE.get(), "jei.reopenedmodularturrets.upgrade_range");
        info(registration, ModItems.UPGRADE_ACCURACY.get(), "jei.reopenedmodularturrets.upgrade_accuracy");
        info(registration, ModItems.UPGRADE_SCATTER_SHOT.get(), "jei.reopenedmodularturrets.upgrade_scatter_shot");
        info(registration, ModItems.ADDON_DAMAGE_AMP.get(), "jei.reopenedmodularturrets.addon_damage_amp");
        info(registration, ModItems.ADDON_POTENTIA.get(), "jei.reopenedmodularturrets.addon_potentia");
        info(registration, ModItems.ADDON_RECYCLER.get(), "jei.reopenedmodularturrets.addon_recycler");
        info(registration, ModItems.ADDON_CONCEALER.get(), "jei.reopenedmodularturrets.addon_concealer");
        info(registration, ModItems.ADDON_FAKE_DROPS.get(), "jei.reopenedmodularturrets.addon_fake_drops");
        info(registration, ModItems.ADDON_SERIAL_PORT.get(), "jei.reopenedmodularturrets.addon_serial_port");
        info(registration, ModItems.BULLET.get(), "jei.reopenedmodularturrets.bullet");
        info(registration, ModItems.GRENADE.get(), "jei.reopenedmodularturrets.grenade");
        info(registration, ModItems.ROCKET.get(), "jei.reopenedmodularturrets.rocket");
        for (DeferredHolder<Item, ? extends Item> holder : ModItems.ITEMS.getEntries()) {
            String path = holder.getId().getPath();
            if (path.startsWith("expander_")) {
                info(registration, holder.get(), "jei.reopenedmodularturrets.expander");
            }
        }
    }

    private static void info(IRecipeRegistration registration, Item item, String key) {
        registration.addIngredientInfo(
                new ItemStack(item),
                VanillaTypes.ITEM_STACK,
                Component.translatable(key)
        );
    }
}
