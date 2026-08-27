package com.ommods.reopenedmodularturrets.turret;

import com.ommods.reopenedmodularturrets.blockentity.TurretBaseBlockEntity;
import com.ommods.reopenedmodularturrets.blockentity.TurretHeadBlockEntity;
import com.ommods.reopenedmodularturrets.config.ModConfig;
import com.ommods.reopenedmodularturrets.entity.GrenadeProjectileEntity;
import com.ommods.reopenedmodularturrets.item.AmmoType;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
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
    PLASMA(5, true);

    private final int minTier;
    private final boolean directed;

    TurretKind(int minTier, boolean directed) {
        this.minTier = minTier;
        this.directed = directed;
    }

    public int getMinTier() {
        return minTier;
    }

    public boolean isDirected() {
        return directed;
    }

    public static TurretKind fromTextureName(String name) {
        for (TurretKind kind : values()) {
            if (kind.textureName().equals(name)) {
                return kind;
            }
        }
        return GUN;
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
        };
    }

    public boolean usesGrenadeModel() {
        return this == GRENADE;
    }

    public double getRange() {
        return switch (this) {
            case GUN -> ModConfig.GUN_RANGE.get();
            case GRENADE -> ModConfig.GRENADE_RANGE.get();
            case DISPOSABLE_ITEM, POTATO_CANNON -> 12.0;
            case INCENDIARY, ROCKET -> 16.0;
            case RELATIVISTIC, TELEPORTER -> 20.0;
            case LASER, RAIL_GUN, PLASMA -> 24.0;
        };
    }

    public int getEnergyPerShot() {
        return switch (this) {
            case GUN -> ModConfig.GUN_ENERGY_PER_SHOT.get();
            case GRENADE -> ModConfig.GRENADE_ENERGY_PER_SHOT.get();
            case DISPOSABLE_ITEM, POTATO_CANNON -> 60;
            case INCENDIARY, ROCKET -> 100;
            case RELATIVISTIC, TELEPORTER -> 150;
            case LASER, RAIL_GUN, PLASMA -> 200;
        };
    }

    public int getCooldown() {
        return switch (this) {
            case GUN -> ModConfig.GUN_COOLDOWN.get();
            case GRENADE -> ModConfig.GRENADE_COOLDOWN.get();
            case DISPOSABLE_ITEM, POTATO_CANNON -> 20;
            case INCENDIARY -> 30;
            case ROCKET -> 50;
            case RELATIVISTIC, TELEPORTER -> 40;
            case LASER -> 15;
            case RAIL_GUN -> 60;
            case PLASMA -> 25;
        };
    }

    public float getDamage() {
        return switch (this) {
            case GUN -> ModConfig.GUN_DAMAGE.get();
            case GRENADE -> ModConfig.GRENADE_DAMAGE.get();
            case DISPOSABLE_ITEM, POTATO_CANNON -> 3.0F;
            case INCENDIARY -> 4.0F;
            case ROCKET -> 8.0F;
            case RELATIVISTIC, TELEPORTER -> 10.0F;
            case LASER -> 12.0F;
            case RAIL_GUN -> 20.0F;
            case PLASMA -> 15.0F;
        };
    }

    public AmmoType getAmmoType() {
        return switch (this) {
            case GUN, LASER, RAIL_GUN, PLASMA, RELATIVISTIC -> AmmoType.BULLET;
            case GRENADE, ROCKET, INCENDIARY -> AmmoType.GRENADE;
            case DISPOSABLE_ITEM, POTATO_CANNON, TELEPORTER -> null;
        };
    }

    public void fire(TurretHeadBlockEntity turret, ServerLevel level, TurretBaseBlockEntity base, LivingEntity target) {
        Vec3 origin = Vec3.atCenterOf(turret.getBlockPos());
        DamageSource source = level.damageSources().mobAttack(null);
        switch (this) {
            case GUN, LASER, RAIL_GUN, PLASMA, RELATIVISTIC, INCENDIARY, POTATO_CANNON, DISPOSABLE_ITEM, TELEPORTER ->
                    target.hurtServer(level, source, getDamage());
            case GRENADE, ROCKET -> {
                Vec3 targetPos = target.position().add(0, target.getBbHeight() * 0.5, 0);
                Vec3 direction = targetPos.subtract(origin).normalize();
                GrenadeProjectileEntity projectile = new GrenadeProjectileEntity(level);
                projectile.setPos(origin.x, origin.y, origin.z);
                projectile.shoot(direction.x, direction.y, direction.z, 0.8F, 1.0F);
                projectile.setDamage(getDamage());
                level.addFreshEntity(projectile);
            }
        }
    }
}
