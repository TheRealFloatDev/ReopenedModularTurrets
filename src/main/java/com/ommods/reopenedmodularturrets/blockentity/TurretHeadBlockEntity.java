package com.ommods.reopenedmodularturrets.blockentity;

import com.ommods.reopenedmodularturrets.item.AmmoType;
import com.ommods.reopenedmodularturrets.block.TurretHeadBlock;
import com.ommods.reopenedmodularturrets.registry.ModBlockEntities;
import com.ommods.reopenedmodularturrets.turret.TurretKind;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class TurretHeadBlockEntity extends DirectedTurretBlockEntity {
    private int cooldown = 0;
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

    public static void serverTick(Level level, BlockPos pos, BlockState state, TurretHeadBlockEntity turret) {
        if (turret.cooldown > 0) {
            turret.cooldown--;
        }
    }

    public void tickCombat(ServerLevel level, TurretBaseBlockEntity baseEntity) {
        TurretKind kind = getKind();
        if (baseEntity.getTier() < kind.getMinTier()) {
            return;
        }
        if (kind.isDirected()) {
            updateAim(level, baseEntity, kind.getRange());
        }
        tryFire(level, baseEntity);
    }

    public void tryFire(ServerLevel level, TurretBaseBlockEntity baseEntity) {
        if (cooldown > 0) {
            return;
        }
        TurretKind kind = getKind();
        Vec3 origin = Vec3.atCenterOf(worldPosition);
        TurretBaseBlockEntity.OptionalTarget target = baseEntity.findTarget(level, origin, kind.getRange());
        if (target == null) {
            return;
        }
        AmmoType ammo = kind.getAmmoType();
        if (ammo != null && !baseEntity.consumeAmmo(ammo, 1)) {
            return;
        }
        if (!baseEntity.consumeEnergy(kind.getEnergyPerShot())) {
            return;
        }
        kind.fire(this, level, baseEntity, target.entity());
        cooldown = kind.getCooldown();
        setChanged();
    }
}
