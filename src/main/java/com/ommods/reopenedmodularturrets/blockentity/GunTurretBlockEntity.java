package com.ommods.reopenedmodularturrets.blockentity;

import com.ommods.reopenedmodularturrets.config.ModConfig;
import com.ommods.reopenedmodularturrets.item.AmmoType;
import com.ommods.reopenedmodularturrets.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class GunTurretBlockEntity extends DirectedTurretBlockEntity {
    private int cooldown = 0;
    @Nullable
    private TurretBaseBlockEntity base;

    public GunTurretBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.GUN_TURRET.get(), pos, state);
    }

    public void bindBase(TurretBaseBlockEntity base) {
        this.base = base;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, GunTurretBlockEntity turret) {
        if (turret.cooldown > 0) {
            turret.cooldown--;
        }
    }

    public void tickCombat(ServerLevel level, TurretBaseBlockEntity baseEntity) {
        updateAim(level, baseEntity, ModConfig.GUN_RANGE.get());
        tryFire(level, baseEntity);
    }

    public void tryFire(ServerLevel level, TurretBaseBlockEntity baseEntity) {
        if (cooldown > 0) {
            return;
        }
        Vec3 origin = Vec3.atCenterOf(worldPosition);
        TurretBaseBlockEntity.OptionalTarget target = baseEntity.findTarget(level, origin, ModConfig.GUN_RANGE.get());
        if (target == null) {
            return;
        }
        LivingEntity entity = target.entity();
        int energyCost = ModConfig.GUN_ENERGY_PER_SHOT.get();
        if (!baseEntity.consumeEnergy(energyCost) || !baseEntity.consumeAmmo(AmmoType.BULLET, 1)) {
            return;
        }
        DamageSource source = level.damageSources().mobAttack(null);
        entity.hurtServer(level, source, ModConfig.GUN_DAMAGE.get());
        cooldown = ModConfig.GUN_COOLDOWN.get();
        setChanged();
    }
}
