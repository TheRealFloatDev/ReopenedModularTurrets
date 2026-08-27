package com.ommods.reopenedmodularturrets.registry;

import com.ommods.reopenedmodularturrets.ModConstants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(Registries.SOUND_EVENT, ModConstants.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> MACHINE_GUN = register("machine_gun");
    public static final DeferredHolder<SoundEvent, SoundEvent> GRENADE = register("grenade");
    public static final DeferredHolder<SoundEvent, SoundEvent> ROCKET = register("rocket");
    public static final DeferredHolder<SoundEvent, SoundEvent> LASER = register("laser");
    public static final DeferredHolder<SoundEvent, SoundEvent> LASER_HIT = register("laser_hit");
    public static final DeferredHolder<SoundEvent, SoundEvent> BULLET_HIT = register("bullet_hit");
    public static final DeferredHolder<SoundEvent, SoundEvent> DISPOSABLE = register("disposable");
    public static final DeferredHolder<SoundEvent, SoundEvent> POTATO = register("potato");
    public static final DeferredHolder<SoundEvent, SoundEvent> INCENDIARY = register("incendiary");
    public static final DeferredHolder<SoundEvent, SoundEvent> RELATIVISTIC = register("relativistic");
    public static final DeferredHolder<SoundEvent, SoundEvent> TELEPORT = register("teleport");
    public static final DeferredHolder<SoundEvent, SoundEvent> RAIL_GUN = register("rail_gun");
    public static final DeferredHolder<SoundEvent, SoundEvent> RAIL_GUN_HIT = register("rail_gun_hit");
    public static final DeferredHolder<SoundEvent, SoundEvent> PLASMA_LAUNCH = register("plasma_launch");
    public static final DeferredHolder<SoundEvent, SoundEvent> WINDUP = register("windup");

    private static DeferredHolder<SoundEvent, SoundEvent> register(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    private ModSounds() {}
}
