package com.ommods.reopenedmodularturrets.registry;

import com.ommods.reopenedmodularturrets.ModConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
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
                        output.accept(ModItems.GUN_TURRET_ITEM.get());
                        output.accept(ModItems.GRENADE_TURRET_ITEM.get());
                        output.accept(ModItems.SOLAR_ADDON_ITEM.get());
                        output.accept(ModItems.BULLET.get());
                        output.accept(ModItems.GRENADE.get());
                    })
                    .build());

    private ModCreativeTabs() {}
}
