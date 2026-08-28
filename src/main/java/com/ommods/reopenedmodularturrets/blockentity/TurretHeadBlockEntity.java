package com.ommods.reopenedmodularturrets.blockentity;

import com.ommods.reopenedmodularturrets.item.AmmoType;
import com.ommods.reopenedmodularturrets.block.TurretHeadBlock;
import com.ommods.reopenedmodularturrets.registry.ModBlockEntities;
import com.ommods.reopenedmodularturrets.registry.ModSounds;
import com.ommods.reopenedmodularturrets.turret.TurretKind;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class TurretHeadBlockEntity extends DirectedTurretBlockEntity {
    private int cooldown = 0;
    private boolean concealed = false;
    @Nullable
    private TurretBaseBlockEntity base;

    public TurretHeadBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TURRET_HEAD.get(), pos, state);
    }

    public TurretKind getKind() {
        if (getBlockState().getBlock() instanceof TurretHeadBlock headBlock) {
            return headBlock.getKind();
        }
        return TurretKind.GUN;
    }

    public void bindBase(TurretBaseBlockEntity base) {
        this.base = base;
    }

    @Nullable
    public TurretBaseBlockEntity getBase() {
        return base != null ? base : resolveBase();
    }

    @Nullable
    public TurretBaseBlockEntity resolveBase() {
        if (level == null) {
            return null;
        }
        for (Direction direction : Direction.values()) {
            if (level.getBlockEntity(worldPosition.relative(direction)) instanceof TurretBaseBlockEntity found) {
                return found;
            }
        }
        return null;
    }

    public boolean isConcealed() {
        return concealed;
    }

    public void setConcealed(boolean concealed) {
        if (this.concealed != concealed) {
            this.concealed = concealed;
            setChanged();
            if (level != null && !level.isClientSide()) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
            }
        }
    }

    public int getCooldown() {
        return cooldown;
    }

    @Nullable
    public Direction getBaseDirection() {
        if (level == null) {
            return null;
        }
        for (Direction direction : Direction.values()) {
            if (level.getBlockEntity(worldPosition.relative(direction)) instanceof TurretBaseBlockEntity) {
                return direction;
            }
        }
        return null;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, TurretHeadBlockEntity turret) {
        if (turret.cooldown > 0) {
            turret.cooldown--;
        }
    }

    public void tickCombat(ServerLevel level, TurretBaseBlockEntity baseEntity) {
        TurretKind kind = getKind();
        if (!kind.isEnabled() || !baseEntity.isActive()) {
            return;
        }
        if (baseEntity.getTier() < kind.getMinTier()) {
            return;
        }
        double range = baseEntity.getEffectiveRange(kind.getRange());
        if (kind.isDirected()) {
            updateAim(level, baseEntity, range);
        }
        tryFire(level, baseEntity);
    }

    public void tryFire(ServerLevel level, TurretBaseBlockEntity baseEntity) {
        if (cooldown > 0) {
            return;
        }
        TurretKind kind = getKind();
        Vec3 origin = Vec3.atCenterOf(worldPosition);
        double range = baseEntity.getEffectiveRange(kind.getRange());
        TurretBaseBlockEntity.OptionalTarget target = baseEntity.findTargetForTurret(
                level,
                origin,
                kind.getRange(),
                baseEntity.getSharedTarget()
        );
        if (target == null) {
            return;
        }

        int energyCost = baseEntity.getUpgradeModifiers().applyEnergyCost(kind.getEnergyPerShot());
        if (energyCost > 0 && baseEntity.getEnergyStorage().getEnergyStored() < energyCost) {
            return;
        }

        if (!consumeAmmoForKind(kind, baseEntity)) {
            return;
        }

        if (energyCost > 0 && !baseEntity.consumeEnergy(energyCost)) {
            return;
        }

        float damage = kind.getDamage() * baseEntity.getDamageMultiplier();
        kind.fire(this, level, baseEntity, target.entity(), damage);
        if (baseEntity.getUpgradeModifiers().scatterShot()) {
            LivingEntity scatterTarget = findScatterTarget(level, baseEntity, origin, range, target.entity());
            if (scatterTarget != null && consumeAmmoForKind(kind, baseEntity)) {
                kind.fire(this, level, baseEntity, scatterTarget, damage * 0.75F);
            }
        }

        playFireSound(level, kind);
        if (!target.entity().isAlive()) {
            baseEntity.handleKill(target.entity());
        }
        cooldown = baseEntity.getUpgradeModifiers().applyCooldown(kind.getCooldown());
        setChanged();
    }

    private static boolean consumeAmmoForKind(TurretKind kind, TurretBaseBlockEntity baseEntity) {
        return switch (kind) {
            case DISPOSABLE_ITEM -> baseEntity.consumeDisposableAmmo();
            case POTATO_CANNON -> baseEntity.consumePotatoAmmo();
            default -> {
                AmmoType ammo = kind.getAmmoType();
                yield baseEntity.consumeAmmo(ammo, ammo != null ? 1 : 0);
            }
        };
    }

    @Nullable
    private LivingEntity findScatterTarget(
            ServerLevel level,
            TurretBaseBlockEntity baseEntity,
            Vec3 origin,
            double range,
            LivingEntity primary
    ) {
        TurretBaseBlockEntity.OptionalTarget secondary = baseEntity.findTargetForTurret(
                level,
                origin,
                getKind().getRange(),
                primary
        );
        if (secondary != null && secondary.entity() != primary) {
            return secondary.entity();
        }
        return null;
    }

    private void playFireSound(ServerLevel level, TurretKind kind) {
        var sound = switch (kind) {
            case GUN, DISPOSABLE_ITEM, POTATO_CANNON, CROSSBOW -> ModSounds.MACHINE_GUN.get();
            case GRENADE, INCENDIARY -> ModSounds.GRENADE.get();
            case ROCKET, PLASMA -> ModSounds.ROCKET.get();
            case LASER -> ModSounds.LASER.get();
            default -> null;
        };
        if (sound != null) {
            level.playSound(null, worldPosition, sound, SoundSource.BLOCKS, 0.8F, 1.0F);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        concealed = tag.getBoolean("Concealed");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putBoolean("Concealed", concealed);
    }
}
