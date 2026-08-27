package com.ommods.reopenedmodularturrets.registry;

import com.ommods.reopenedmodularturrets.ModConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
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
                        for (var holder : ModItems.ITEMS.getEntries()) {
                            Item item = holder.get();
                            if (item instanceof net.minecraft.world.item.BlockItem blockItem
                                    && blockItem.getBlock() instanceof com.ommods.reopenedmodularturrets.block.TurretHeadBlock) {
                                continue;
                            }
                            output.accept(item);
                        }
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
                        output.accept(ModItems.PLASMA_TURRET_ITEM.get());
                        output.accept(ModItems.ARC_TURRET_ITEM.get());
                        output.accept(ModItems.MELEE_TURRET_ITEM.get());
                        output.accept(ModItems.CROSSBOW_TURRET_ITEM.get());
                    })
                    .build());

    private ModCreativeTabs() {}
}
