package com.ommods.reopenedmodularturrets.registry;

import com.ommods.reopenedmodularturrets.ModConstants;
import com.ommods.reopenedmodularturrets.entity.GrenadeProjectileEntity;
import com.ommods.reopenedmodularturrets.entity.LaserBeamEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(Registries.ENTITY_TYPE, ModConstants.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<GrenadeProjectileEntity>> GRENADE_PROJECTILE =
            ENTITY_TYPES.register("grenade_projectile", () -> EntityType.Builder.<GrenadeProjectileEntity>of(
                    GrenadeProjectileEntity::new,
                    MobCategory.MISC
            ).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(10).build("grenade_projectile"));

    public static final DeferredHolder<EntityType<?>, EntityType<LaserBeamEntity>> LASER_BEAM =
            ENTITY_TYPES.register("laser_beam", () -> EntityType.Builder.<LaserBeamEntity>of(
                    LaserBeamEntity::new,
                    MobCategory.MISC
            ).sized(0.1F, 0.1F).clientTrackingRange(8).updateInterval(1).build("laser_beam"));

    private ModEntityTypes() {}
}
