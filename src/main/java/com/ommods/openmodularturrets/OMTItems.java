package com.ommods.openmodularturrets;

import com.ommods.openmodularturrets.content.item.ammo.*;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.entry.RegistryEntry;

import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import org.jetbrains.annotations.NotNull;


public class OMTItems {

    public static RegistryEntry<BulletItem> ammoItemBullet;
    public static RegistryEntry<RocketSmallItem> ammoItemRocketSmall;
    public static RegistryEntry<RocketLargeItem> ammoItemRocketLarge;
    public static RegistryEntry<RailgunSlugItem> ammoItemRailgunSlug;
    public static RegistryEntry<CrossbowBoltItem> ammoItemCrossbowBolt;
    public static RegistryEntry<FireChargeItem> ammoFireCharge;
    public static RegistryEntry<GrenadeItem> ammoItemGrenade;
    public static RegistryEntry<ArtilleryShellItem> ammoItemArtilleryShell;

    public static void register(Registrate REGISTRATE) {
        ammoItemBullet = REGISTRATE.item(BulletItem.NAME, BulletItem::new)
                .tab(OpenModularTurrets.creativeModeTabNonNullSupplier)
                .register();
        ammoItemRocketSmall = REGISTRATE.item(RocketSmallItem.NAME, RocketSmallItem::new)
                .tab(OpenModularTurrets.creativeModeTabNonNullSupplier)
                .register();
        ammoItemRocketLarge = REGISTRATE.item(RocketLargeItem.NAME, RocketLargeItem::new)
                .tab(OpenModularTurrets.creativeModeTabNonNullSupplier)
                .register();
        ammoItemRailgunSlug = REGISTRATE.item(RailgunSlugItem.NAME, RailgunSlugItem::new)
                .tab(OpenModularTurrets.creativeModeTabNonNullSupplier)
                .register();
        ammoItemCrossbowBolt = REGISTRATE.item(CrossbowBoltItem.NAME, CrossbowBoltItem::new)
                .tab(OpenModularTurrets.creativeModeTabNonNullSupplier)
                .register();
        ammoFireCharge = REGISTRATE.item(FireChargeItem.NAME, FireChargeItem::new)
                .tab(OpenModularTurrets.creativeModeTabNonNullSupplier)
                .register();
        ammoItemGrenade = REGISTRATE.item(GrenadeItem.NAME, GrenadeItem::new)
                .tab(OpenModularTurrets.creativeModeTabNonNullSupplier)
                .register();
        ammoItemArtilleryShell = REGISTRATE.item(ArtilleryShellItem.NAME, ArtilleryShellItem::new)
                .tab(OpenModularTurrets.creativeModeTabNonNullSupplier)
                .register();

    }
}
