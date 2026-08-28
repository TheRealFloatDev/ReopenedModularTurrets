package com.ommods.reopenedmodularturrets.turret;

import com.ommods.reopenedmodularturrets.blockentity.TurretBaseBlockEntity;
import com.ommods.reopenedmodularturrets.blockentity.TurretHeadBlockEntity;
import com.ommods.reopenedmodularturrets.config.ModConfig;
import com.ommods.reopenedmodularturrets.core.targeting.TargetingHelper;
import com.ommods.reopenedmodularturrets.core.targeting.TurretAimHelper;
import com.ommods.reopenedmodularturrets.entity.BulletProjectileEntity;
import com.ommods.reopenedmodularturrets.entity.GrenadeProjectileEntity;
import com.ommods.reopenedmodularturrets.entity.LaserBeamEntity;
import com.ommods.reopenedmodularturrets.entity.PlasmaProjectileEntity;
import com.ommods.reopenedmodularturrets.entity.TurretArrowEntity;
import com.ommods.reopenedmodularturrets.network.ModNetworking;
import com.ommods.reopenedmodularturrets.network.payload.SyncArcLightningPayload;
import com.ommods.reopenedmodularturrets.network.payload.SyncTurretRayPayload;
import com.ommods.reopenedmodularturrets.registry.ModBlocks;
import com.ommods.reopenedmodularturrets.item.AmmoType;
import com.ommods.reopenedmodularturrets.registry.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public enum TurretKind {
    DISPOSABLE_ITEM(1, false, new int[] {-5, -5, 10, -5, -5}),
    POTATO_CANNON(1, true, new int[] {-5, -5, 10, -5, -5}),
    GUN(2, true, new int[] {1, 10, -5, -5, 10}),
    GRENADE(2, true, new int[] {0, 5, 0, 0, 5}),
    INCENDIARY(3, true, new int[] {0, 5, 0, 0, 5}),
    ROCKET(3, true, new int[] {0, 5, 0, 0, 5}),
    RELATIVISTIC(4, true, new int[] {5, 10, 2, -10, 10}),
    TELEPORTER(4, false, new int[] {0, 0, 10, 0, 10}),
    LASER(5, true, new int[] {5, 10, 2, -10, 10}),
    RAIL_GUN(5, true, new int[] {5, 10, 2, -10, 10}),
    PLASMA(5, true, new int[] {0, 5, 0, 0, 5}),
    ARC(3, true, new int[] {5, 10, 2, -10, 10}),
    MELEE(2, false, new int[] {0, 0, 10, 0, 0}),
    CROSSBOW(2, true, new int[] {-5, -5, -5, 1, 10});

    private final int minTier;
    private final boolean directed;
    private final int[] defaultPriorities;

    TurretKind(int minTier, boolean directed, int[] defaultPriorities) {
        this.minTier = minTier;
        this.directed = directed;
        this.defaultPriorities = defaultPriorities;
    }

    public boolean isEnabled() {
        return this != MELEE;
    }

    public int[] getDefaultPriorities() {
        return defaultPriorities.clone();
    }

    public int getMinTier() {
        return minTier;
    }

    public boolean isDirected() {
        return directed;
    }

    @Nullable
    public static TurretKind fromItem(Item item) {
        if (!(item instanceof BlockItem blockItem)) {
            return null;
        }
        var block = blockItem.getBlock();
        if (block == ModBlocks.GUN_TURRET.get()) return GUN;
        if (block == ModBlocks.GRENADE_TURRET.get()) return GRENADE;
        if (block == ModBlocks.DISPOSABLE_ITEM_TURRET.get()) return DISPOSABLE_ITEM;
        if (block == ModBlocks.POTATO_CANNON_TURRET.get()) return POTATO_CANNON;
        if (block == ModBlocks.INCENDIARY_TURRET.get()) return INCENDIARY;
        if (block == ModBlocks.ROCKET_TURRET.get()) return ROCKET;
        if (block == ModBlocks.RELATIVISTIC_TURRET.get()) return RELATIVISTIC;
        if (block == ModBlocks.TELEPORTER_TURRET.get()) return TELEPORTER;
        if (block == ModBlocks.LASER_TURRET.get()) return LASER;
        if (block == ModBlocks.RAIL_GUN_TURRET.get()) return RAIL_GUN;
        if (block == ModBlocks.PLASMA_TURRET.get()) return PLASMA;
        if (block == ModBlocks.ARC_TURRET.get()) return ARC;
        if (block == ModBlocks.MELEE_TURRET.get()) return MELEE;
        if (block == ModBlocks.CROSSBOW_TURRET.get()) return CROSSBOW;
        return null;
    }

    public String textureName() {
        return switch (this) {
            case GUN -> "gun_turret";
            case GRENADE, PLASMA -> "grenade_turret";
            case DISPOSABLE_ITEM -> "disposable_item_turret";
            case POTATO_CANNON -> "potato_cannon_turret";
            case INCENDIARY -> "incendiary_turret";
            case ROCKET -> "rocket_turret";
            case RELATIVISTIC -> "relativistic_turret";
            case TELEPORTER -> "teleporter_turret";
            case LASER -> "laser_turret";
            case RAIL_GUN -> "rail_gun_turret";
            case ARC -> "arc_turret";
            case MELEE -> "melee_turret";
            case CROSSBOW -> "crossbow_turret";
        };
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

    public int getMachineGunCooldown(TurretBaseBlockEntity base) {
        return base.getUpgradeModifiers().applyCooldown(getCooldown());
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
            case LASER, PLASMA, ARC -> null;
            case CROSSBOW -> null;
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
        float inaccuracy = base.getUpgradeModifiers().applySpread(1.0F);
        switch (this) {
            case GUN -> spawnBullet(level, origin, target, damage, 2.5F, inaccuracy);
            case POTATO_CANNON, DISPOSABLE_ITEM -> applyHitscan(level, target, damage);
            case CROSSBOW -> spawnArrow(level, turret, target, damage, 2.0F, inaccuracy);
            case LASER -> applyLaserHit(level, target, damage, turret);
            case RAIL_GUN -> applyRailGunHit(level, target, damage, turret);
            case MELEE -> target.hurt(level.damageSources().mobAttack(null), damage);
            case ARC -> { /* chain handled below */ }
            case INCENDIARY -> {
                target.hurt(level.damageSources().mobAttack(null), damage);
                target.igniteForSeconds(4);
                spawnProjectile(level, origin, target, damage, 0.9F, inaccuracy, false, "ammo_blazing_clay", true);
            }
            case RELATIVISTIC -> {
                target.hurt(level.damageSources().mobAttack(null), damage);
                target.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                        net.minecraft.world.effect.MobEffects.MOVEMENT_SLOWDOWN, 100, 1));
                target.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                        net.minecraft.world.effect.MobEffects.WEAKNESS, 100, 0));
            }
            case TELEPORTER -> {
                if (origin.distanceToSqr(target.position()) >= 9.0) {
                    target.teleportTo(target.getX(), target.getY() + 8.0, target.getZ());
                }
            }
            case GRENADE -> spawnProjectile(level, origin, target, damage, 0.8F, inaccuracy, false, "ammo_grenade", true);
            case ROCKET -> spawnProjectile(level, origin, target, damage, 1.4F, inaccuracy, false, "ammo_rocket", true);
            case PLASMA -> spawnPlasma(level, origin, target, damage, 1.0F, inaccuracy);
        }
        if (this == ARC) {
            target.hurt(level.damageSources().mobAttack(null), damage);
            fireArcChain(level, base, turret, target, damage);
        }
    }

    public void fireArcChain(ServerLevel level, TurretBaseBlockEntity base, TurretHeadBlockEntity turret, LivingEntity primary, float damage) {
        List<Integer> hitIds = new ArrayList<>();
        hitIds.add(primary.getId());
        Vec3 from = primary.getEyePosition();
        chainArcHit(level, base, turret, primary, damage, 2, hitIds);
        ModNetworking.sendArcLightning(level, turret.getBlockPos(), from, primary.getEyePosition());
    }

    private void chainArcHit(
            ServerLevel level,
            TurretBaseBlockEntity base,
            TurretHeadBlockEntity turret,
            LivingEntity source,
            float damage,
            int hopsLeft,
            List<Integer> hitIds
    ) {
        if (hopsLeft <= 0) {
            return;
        }
        int range = 3 * hopsLeft;
        AABB box = source.getBoundingBox().inflate(range);
        UUID ownerUuid = base.getOwnedData().getOwnerUuid().orElse(null);
        List<LivingEntity> nearby = level.getEntitiesOfClass(LivingEntity.class, box, entity -> {
            if (hitIds.contains(entity.getId())) {
                return false;
            }
            return TargetingHelper.isValidTarget(
                    entity,
                    base.isAttackMobs(),
                    base.isAttackPlayers(),
                    base.isAttackNeutral(),
                    ownerUuid,
                    base.getTrustedPlayers().getNames()
            );
        });
        for (LivingEntity next : nearby) {
            float hopDamage = damage / (3 - hopsLeft + 1);
            next.hurt(level.damageSources().mobAttack(null), hopDamage);
            hitIds.add(next.getId());
            ModNetworking.sendArcLightning(level, turret.getBlockPos(), source.getEyePosition(), next.getEyePosition());
            chainArcHit(level, base, turret, next, damage, hopsLeft - 1, hitIds);
            break;
        }
    }

    private static void spawnBullet(ServerLevel level, Vec3 origin, LivingEntity target, float damage, float speed, float inaccuracy) {
        Vec3 targetPos = target.position().add(0, target.getBbHeight() * 0.5, 0);
        Vec3 direction = targetPos.subtract(origin);
        BulletProjectileEntity projectile = new BulletProjectileEntity(level);
        projectile.setPos(origin.x, origin.y, origin.z);
        projectile.configure(damage, "ammo_bullet");
        projectile.shoot(direction, speed, inaccuracy);
        level.addFreshEntity(projectile);
    }

    private static void spawnPlasma(ServerLevel level, Vec3 origin, LivingEntity target, float damage, float speed, float inaccuracy) {
        Vec3 targetPos = target.position().add(0, target.getBbHeight() * 0.5, 0);
        PlasmaProjectileEntity projectile = new PlasmaProjectileEntity(level);
        projectile.setPos(origin.x, origin.y, origin.z);
        projectile.configure(damage, true);
        projectile.shoot(targetPos.subtract(origin), speed, inaccuracy);
        level.addFreshEntity(projectile);
        level.playSound(null, BlockPos.containing(origin), ModSounds.PLASMA_LAUNCH.get(), SoundSource.BLOCKS, 0.8F, 1.0F);
    }

    private static void spawnArrow(ServerLevel level, TurretHeadBlockEntity turret, LivingEntity target, float damage, float speed, float inaccuracy) {
        Vec3 origin = Vec3.atCenterOf(turret.getBlockPos()).add(0, 0.4, 0);
        Vec3 targetPos = target.position().add(0, target.getBbHeight() * 0.5, 0);
        TurretArrowEntity arrow = new TurretArrowEntity(level);
        arrow.setPos(origin.x, origin.y, origin.z);
        arrow.setTurretDamage(damage);
        arrow.shoot(targetPos.x - origin.x, targetPos.y - origin.y, targetPos.z - origin.z, speed, inaccuracy);
        level.addFreshEntity(arrow);
    }

    private static void applyHitscan(ServerLevel level, LivingEntity target, float damage) {
        target.hurt(level.damageSources().mobAttack(null), damage);
        level.playSound(null, target.blockPosition(), ModSounds.BULLET_HIT.get(), SoundSource.BLOCKS, 0.5F, 1.0F);
    }

    private static void applyLaserHit(ServerLevel level, LivingEntity target, float damage, TurretHeadBlockEntity turret) {
        Vec3 origin = TurretAimHelper.getLaserOrigin(turret.getBlockPos(), turret.getYaw(), turret.getPitch());
        Vec3 targetPos = target.position().add(0, target.getBbHeight() * 0.5, 0);
        target.hurt(level.damageSources().mobAttack(null), damage);
        level.addFreshEntity(new LaserBeamEntity(level, origin, targetPos));
        level.playSound(null, target.blockPosition(), ModSounds.LASER_HIT.get(), SoundSource.BLOCKS, 0.6F, 1.0F);
    }

    private static void applyRailGunHit(ServerLevel level, LivingEntity target, float damage, TurretHeadBlockEntity turret) {
        Vec3 origin = TurretAimHelper.getLaserOrigin(turret.getBlockPos(), turret.getYaw(), turret.getPitch());
        Vec3 targetPos = target.position().add(0, target.getBbHeight() * 0.5, 0);
        target.hurt(level.damageSources().magic(), damage);
        ModNetworking.sendTurretRay(level, turret.getBlockPos(), origin, targetPos, 255, 128, 0);
        level.playSound(null, target.blockPosition(), ModSounds.RAIL_GUN_HIT.get(), SoundSource.BLOCKS, 0.7F, 1.0F);
    }

    private static void spawnProjectile(
            ServerLevel level,
            Vec3 origin,
            LivingEntity target,
            float damage,
            float speed,
            float inaccuracy,
            boolean directHit,
            String texture,
            boolean explosive
    ) {
        Vec3 targetPos = target.position().add(0, target.getBbHeight() * 0.5, 0);
        Vec3 direction = targetPos.subtract(origin).normalize();
        GrenadeProjectileEntity projectile = new GrenadeProjectileEntity(level);
        projectile.setPos(origin.x, origin.y, origin.z);
        projectile.shoot(direction.x, direction.y, direction.z, speed, inaccuracy);
        projectile.setDamage(damage);
        projectile.setDirectHit(directHit);
        projectile.setProjectileTexture(texture);
        projectile.setExplosive(explosive);
        level.addFreshEntity(projectile);
    }
}
