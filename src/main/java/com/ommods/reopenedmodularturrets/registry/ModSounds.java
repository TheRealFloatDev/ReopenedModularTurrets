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

    public static final DeferredHolder<SoundEvent, SoundEvent> MACHINE_GUN =
            register("machine_gun");
    public static final DeferredHolder<SoundEvent, SoundEvent> GRENADE =
            register("grenade");
    public static final DeferredHolder<SoundEvent, SoundEvent> ROCKET =
            register("rocket");
    public static final DeferredHolder<SoundEvent, SoundEvent> LASER =
            register("laser");
    public static final DeferredHolder<SoundEvent, SoundEvent> BULLET_HIT =
            register("bullet_hit");

    private static DeferredHolder<SoundEvent, SoundEvent> register(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, name);
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(id));
    }

    private ModSounds() {}
}
