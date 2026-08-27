package com.ommods.reopenedmodularturrets.blockentity;

import com.ommods.reopenedmodularturrets.config.ModConfig;
import com.ommods.reopenedmodularturrets.entity.GrenadeProjectileEntity;
import com.ommods.reopenedmodularturrets.item.AmmoType;
import com.ommods.reopenedmodularturrets.registry.ModBlockEntities;
import com.ommods.reopenedmodularturrets.registry.ModEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class GrenadeTurretBlockEntity extends DirectedTurretBlockEntity {
    private int cooldown = 0;
    @Nullable
    private TurretBaseBlockEntity base;

    public GrenadeTurretBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.GRENADE_TURRET.get(), pos, state);
    }

    public void bindBase(TurretBaseBlockEntity base) {
        this.base = base;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, GrenadeTurretBlockEntity turret) {
        if (turret.cooldown > 0) {
            turret.cooldown--;
        }
    }

    public void tickCombat(ServerLevel level, TurretBaseBlockEntity baseEntity) {
        updateAim(level, baseEntity, ModConfig.GRENADE_RANGE.get());
        tryFire(level, baseEntity);
    }

    public void tryFire(ServerLevel level, TurretBaseBlockEntity baseEntity) {
        if (cooldown > 0) {
            return;
        }
        Vec3 origin = Vec3.atCenterOf(worldPosition);
        TurretBaseBlockEntity.OptionalTarget target = baseEntity.findTarget(level, origin, ModConfig.GRENADE_RANGE.get());
        if (target == null) {
            return;
        }
        LivingEntity entity = target.entity();
        int energyCost = ModConfig.GRENADE_ENERGY_PER_SHOT.get();
        if (!baseEntity.consumeEnergy(energyCost) || !baseEntity.consumeAmmo(AmmoType.GRENADE, 1)) {
            return;
        }
        Vec3 targetPos = entity.position().add(0, entity.getBbHeight() * 0.5, 0);
        Vec3 direction = targetPos.subtract(origin).normalize();
        GrenadeProjectileEntity projectile = new GrenadeProjectileEntity(level);
        projectile.setPos(origin.x, origin.y, origin.z);
        projectile.shoot(direction.x, direction.y, direction.z, 0.8F, 1.0F);
        projectile.setDamage(ModConfig.GRENADE_DAMAGE.get());
        level.addFreshEntity(projectile);
        cooldown = ModConfig.GRENADE_COOLDOWN.get();
        setChanged();
    }
}
