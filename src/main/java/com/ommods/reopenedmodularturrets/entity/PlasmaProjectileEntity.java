package com.ommods.reopenedmodularturrets.entity;

import com.ommods.reopenedmodularturrets.registry.ModEntityTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class PlasmaProjectileEntity extends Projectile {
    private float damage = 15.0F;
    private boolean directHit = true;

    public PlasmaProjectileEntity(EntityType<? extends PlasmaProjectileEntity> type, Level level) {
        super(type, level);
    }

    public PlasmaProjectileEntity(Level level) {
        super(ModEntityTypes.PLASMA_PROJECTILE.get(), level);
    }

    public void configure(float damage, boolean directHit) {
        this.damage = damage;
        this.directHit = directHit;
    }

    public void shoot(Vec3 direction, float speed, float inaccuracy) {
        Vec3 motion = direction.normalize()
                .add(
                        (random.nextDouble() - 0.5) * inaccuracy,
                        (random.nextDouble() - 0.5) * inaccuracy,
                        (random.nextDouble() - 0.5) * inaccuracy
                )
                .scale(speed);
        setDeltaMovement(motion);
        double horizontal = motion.horizontalDistance();
        setYRot((float) (Math.atan2(motion.x, motion.z) * (180.0 / Math.PI)));
        setXRot((float) (Math.atan2(motion.y, horizontal) * (180.0 / Math.PI)));
    }

    @Override
    protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 motion = getDeltaMovement();
        setDeltaMovement(motion.x, motion.y - 0.01, motion.z);
        HitResult hit = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hit.getType() != HitResult.Type.MISS) {
            onHit(hit);
        }
        motion = getDeltaMovement();
        setPos(getX() + motion.x, getY() + motion.y, getZ() + motion.z);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (level() instanceof ServerLevel serverLevel && result.getEntity() instanceof LivingEntity living) {
            living.hurt(serverLevel.damageSources().mobProjectile(this, null), damage);
        }
        if (directHit) {
            discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        discard();
    }

    @Override
    protected boolean canHitEntity(net.minecraft.world.entity.Entity entity) {
        return super.canHitEntity(entity) && entity instanceof LivingEntity;
    }

    public String getProjectileTexture() {
        return "plasma";
    }
}
