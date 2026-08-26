package com.ommods.reopenedmodularturrets.registry;

import com.ommods.reopenedmodularturrets.ModConstants;
import com.ommods.reopenedmodularturrets.menu.TurretBaseMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, ModConstants.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<TurretBaseMenu>> TURRET_BASE =
            MENUS.register("turret_base", () -> IMenuTypeExtension.create(TurretBaseMenu::new));

    private ModMenus() {}
}
