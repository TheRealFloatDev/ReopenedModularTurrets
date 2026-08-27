package com.ommods.reopenedmodularturrets.entity;

import com.ommods.reopenedmodularturrets.registry.ModEntityTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class GrenadeProjectileEntity extends ThrowableItemProjectile {
    private float damage = 6.0F;
    private boolean directHit = false;

    public GrenadeProjectileEntity(EntityType<? extends GrenadeProjectileEntity> type, Level level) {
        super(type, level);
    }

    public GrenadeProjectileEntity(Level level) {
        super(ModEntityTypes.GRENADE_PROJECTILE.get(), level);
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public void setDirectHit(boolean directHit) {
        this.directHit = directHit;
    }

    @Override
    protected Item getDefaultItem() {
        return Items.TNT;
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (level() instanceof ServerLevel serverLevel) {
            serverLevel.explode(this, getX(), getY(), getZ(), damage, Level.ExplosionInteraction.MOB);
            discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (level() instanceof ServerLevel serverLevel) {
            if (result.getEntity() instanceof LivingEntity living) {
                living.hurt(serverLevel.damageSources().mobProjectile(this, null), damage);
            }
            if (directHit) {
                discard();
                return;
            }
            serverLevel.explode(this, getX(), getY(), getZ(), damage, Level.ExplosionInteraction.MOB);
            discard();
        }
    }
}
