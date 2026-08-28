package com.ommods.reopenedmodularturrets.entity;

import com.ommods.reopenedmodularturrets.registry.ModEntityTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class TurretArrowEntity extends AbstractArrow {
    private float turretDamage = 4.0F;

    public TurretArrowEntity(EntityType<? extends TurretArrowEntity> type, Level level) {
        super(type, level);
    }

    public TurretArrowEntity(Level level) {
        super(ModEntityTypes.TURRET_ARROW.get(), level);
    }

    public void setTurretDamage(float turretDamage) {
        this.turretDamage = turretDamage;
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(Items.ARROW);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (level() instanceof ServerLevel serverLevel && result.getEntity() instanceof LivingEntity living) {
            living.hurt(serverLevel.damageSources().mobProjectile(this, null), turretDamage);
        }
    }
}
