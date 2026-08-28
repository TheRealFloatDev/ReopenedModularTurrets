package com.ommods.reopenedmodularturrets.entity;

import com.ommods.reopenedmodularturrets.registry.ModEntityTypes;
import com.ommods.reopenedmodularturrets.registry.ModSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class BulletProjectileEntity extends Projectile {
    private float damage = 4.0F;
    private String texture = "ammo_bullet";

    public BulletProjectileEntity(EntityType<? extends BulletProjectileEntity> type, Level level) {
        super(type, level);
    }

    public BulletProjectileEntity(Level level) {
        super(ModEntityTypes.BULLET_PROJECTILE.get(), level);
    }

    public void configure(float damage, String texture) {
        this.damage = damage;
        this.texture = texture;
    }

    public String getProjectileTexture() {
        return texture;
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
        yRotO = getYRot();
        xRotO = getXRot();
    }

    @Override
    protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
    }

    @Override
    public void tick() {
        super.tick();
        HitResult hit = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
        if (hit.getType() != HitResult.Type.MISS) {
            onHit(hit);
        }
        Vec3 motion = getDeltaMovement();
        setPos(getX() + motion.x, getY() + motion.y, getZ() + motion.z);
        if (!level().isClientSide()) {
            setDeltaMovement(motion.scale(0.99));
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (level() instanceof ServerLevel serverLevel && result.getEntity() instanceof LivingEntity living) {
            living.hurt(serverLevel.damageSources().mobProjectile(this, null), damage);
            level().playSound(null, blockPosition(), ModSounds.BULLET_HIT.get(), SoundSource.BLOCKS, 0.5F, 1.0F);
        }
        discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!level().getBlockState(result.getBlockPos()).isSolidRender(level(), result.getBlockPos())) {
            return;
        }
        if (level() instanceof ServerLevel) {
            level().playSound(null, blockPosition(), ModSounds.BULLET_HIT.get(), SoundSource.BLOCKS, 0.5F, 1.0F);
        }
        discard();
    }

    @Override
    protected boolean canHitEntity(net.minecraft.world.entity.Entity entity) {
        return super.canHitEntity(entity) && entity instanceof LivingEntity;
    }
}
