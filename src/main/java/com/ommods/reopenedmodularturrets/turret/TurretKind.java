package com.ommods.reopenedmodularturrets.turret;

import com.ommods.reopenedmodularturrets.blockentity.TurretBaseBlockEntity;
import com.ommods.reopenedmodularturrets.blockentity.TurretHeadBlockEntity;
import com.ommods.reopenedmodularturrets.config.ModConfig;
import com.ommods.reopenedmodularturrets.entity.GrenadeProjectileEntity;
import com.ommods.reopenedmodularturrets.item.AmmoType;
import com.ommods.reopenedmodularturrets.entity.GrenadeProjectileEntity;
import com.ommods.reopenedmodularturrets.entity.LaserBeamEntity;
import com.ommods.reopenedmodularturrets.registry.ModSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public enum TurretKind {
    DISPOSABLE_ITEM(1, false),
    POTATO_CANNON(1, true),
    GUN(2, true),
    GRENADE(2, true),
    INCENDIARY(3, true),
    ROCKET(3, true),
    RELATIVISTIC(4, true),
    TELEPORTER(4, false),
    LASER(5, true),
    RAIL_GUN(5, true),
    PLASMA(5, true),
    ARC(3, true),
    MELEE(2, false),
    CROSSBOW(2, true);

    private final int minTier;
    private final boolean directed;

    TurretKind(int minTier, boolean directed) {
        this.minTier = minTier;
        this.directed = directed;
    }

    public boolean isEnabled() {
        return this != ARC && this != MELEE && this != CROSSBOW && this != PLASMA;
    }

    public int getMinTier() {
        return minTier;
    }

    public boolean isDirected() {
        return directed;
    }

    public String textureName() {
        return switch (this) {
            case GUN -> "gun_turret";
            case GRENADE -> "grenade_turret";
            case DISPOSABLE_ITEM -> "disposable_item_turret";
            case POTATO_CANNON -> "potato_cannon_turret";
            case INCENDIARY -> "incendiary_turret";
            case ROCKET -> "rocket_turret";
            case RELATIVISTIC -> "relativistic_turret";
            case TELEPORTER -> "teleporter_turret";
            case LASER -> "laser_turret";
            case RAIL_GUN -> "rail_gun_turret";
            case PLASMA -> "plasma_turret";
            case ARC -> "arc_turret";
            case MELEE -> "melee_turret";
            case CROSSBOW -> "crossbow_turret";
        };
    }

    public boolean usesGrenadeModel() {
        return this == GRENADE || this == ROCKET || this == PLASMA;
    }

    public double getRange() {
        return switch (this) {
            case GUN -> ModConfig.GUN_RANGE.get();
            case GRENADE -> ModConfig.GRENADE_RANGE.get();
            case DISPOSABLE_ITEM, POTATO_CANNON, MELEE, CROSSBOW -> 12.0;
            case INCENDIARY, ROCKET, ARC -> 16.0;
            case RELATIVISTIC, TELEPORTER -> 20.0;
            case LASER, RAIL_GUN, PLASMA -> 24.0;
        };
    }

    public int getEnergyPerShot() {
        return switch (this) {
            case GUN -> ModConfig.GUN_ENERGY_PER_SHOT.get();
            case GRENADE -> ModConfig.GRENADE_ENERGY_PER_SHOT.get();
            case DISPOSABLE_ITEM, POTATO_CANNON, MELEE, CROSSBOW -> 60;
            case INCENDIARY, ROCKET, ARC -> 100;
            case RELATIVISTIC, TELEPORTER -> 150;
            case LASER, RAIL_GUN, PLASMA -> 200;
        };
    }

    public int getCooldown() {
        return switch (this) {
            case GUN -> ModConfig.GUN_COOLDOWN.get();
            case GRENADE -> ModConfig.GRENADE_COOLDOWN.get();
            case DISPOSABLE_ITEM, POTATO_CANNON -> 20;
            case INCENDIARY, ARC -> 30;
            case ROCKET, CROSSBOW -> 50;
            case RELATIVISTIC, TELEPORTER -> 40;
            case LASER -> 15;
            case RAIL_GUN -> 60;
            case PLASMA -> 25;
            case MELEE -> 10;
        };
    }

    public float getDamage() {
        return switch (this) {
            case GUN -> ModConfig.GUN_DAMAGE.get();
            case GRENADE -> ModConfig.GRENADE_DAMAGE.get();
            case DISPOSABLE_ITEM, POTATO_CANNON -> 3.0F;
            case INCENDIARY, CROSSBOW -> 4.0F;
            case ROCKET -> 8.0F;
            case RELATIVISTIC, TELEPORTER, ARC -> 10.0F;
            case LASER -> 12.0F;
            case RAIL_GUN -> 20.0F;
            case PLASMA -> 15.0F;
            case MELEE -> 6.0F;
        };
    }

    public AmmoType getAmmoType() {
        return switch (this) {
            case GUN, RELATIVISTIC -> AmmoType.BULLET;
            case GRENADE -> AmmoType.GRENADE;
            case INCENDIARY -> AmmoType.BLAZING_CLAY;
            case ROCKET -> AmmoType.ROCKET;
            case RAIL_GUN -> AmmoType.FERRO_SLUG;
            case LASER, PLASMA, ARC, CROSSBOW -> null;
            case DISPOSABLE_ITEM, POTATO_CANNON, TELEPORTER, MELEE -> null;
        };
    }

    public void fire(
            TurretHeadBlockEntity turret,
            ServerLevel level,
            TurretBaseBlockEntity base,
            LivingEntity target,
            float damage
    ) {
        Vec3 origin = Vec3.atCenterOf(turret.getBlockPos());
        switch (this) {
            case GUN, POTATO_CANNON, DISPOSABLE_ITEM, CROSSBOW -> applyHitscan(level, target, damage);
            case LASER -> applyLaserHit(level, target, damage, origin);
            case RAIL_GUN -> applyArmorPiercingHit(level, target, damage);
            case MELEE, ARC -> target.hurt(level.damageSources().mobAttack(null), damage);
            case INCENDIARY -> {
                target.hurt(level.damageSources().mobAttack(null), damage);
                target.igniteForSeconds(4);
                spawnProjectile(level, origin, target, damage, 0.9F, false, "ammo_blazing_clay", true);
            }
            case RELATIVISTIC -> {
                target.hurt(level.damageSources().mobAttack(null), damage);
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1));
                target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 0));
            }
            case TELEPORTER -> {
                if (origin.distanceToSqr(target.position()) >= 9.0) {
                    target.teleportTo(target.getX(), target.getY() + 8.0, target.getZ());
                }
            }
            case GRENADE -> spawnProjectile(level, origin, target, damage, 0.8F, false, "ammo_grenade", true);
            case ROCKET -> spawnProjectile(level, origin, target, damage, 1.4F, false, "ammo_rocket", true);
            case PLASMA -> spawnProjectile(level, origin, target, damage, 1.0F, true, "plasma", false);
        }
    }

    private static void applyHitscan(ServerLevel level, LivingEntity target, float damage) {
        target.hurt(level.damageSources().mobAttack(null), damage);
        level.playSound(null, target.blockPosition(), ModSounds.BULLET_HIT.get(), SoundSource.BLOCKS, 0.5F, 1.0F);
    }

    private static void applyLaserHit(ServerLevel level, LivingEntity target, float damage, Vec3 origin) {
        Vec3 targetPos = target.position().add(0, target.getBbHeight() * 0.5, 0);
        target.hurt(level.damageSources().mobAttack(null), damage);
        level.addFreshEntity(new LaserBeamEntity(level, origin, targetPos));
        level.playSound(null, target.blockPosition(), ModSounds.LASER_HIT.get(), SoundSource.BLOCKS, 0.6F, 1.0F);
    }

    private static void applyArmorPiercingHit(ServerLevel level, LivingEntity target, float damage) {
        target.hurt(level.damageSources().magic(), damage);
    }

    private static void spawnProjectile(
            ServerLevel level,
            Vec3 origin,
            LivingEntity target,
            float damage,
            float speed,
            boolean directHit,
            String texture,
            boolean explosive
    ) {
        Vec3 targetPos = target.position().add(0, target.getBbHeight() * 0.5, 0);
        Vec3 direction = targetPos.subtract(origin).normalize();
        GrenadeProjectileEntity projectile = new GrenadeProjectileEntity(level);
        projectile.setPos(origin.x, origin.y, origin.z);
        projectile.shoot(direction.x, direction.y, direction.z, speed, 1.0F);
        projectile.setDamage(damage);
        projectile.setDirectHit(directHit);
        projectile.setProjectileTexture(texture);
        projectile.setExplosive(explosive);
        level.addFreshEntity(projectile);
    }
}
