package com.ommods.reopenedmodularturrets.blockentity;

import com.ommods.reopenedmodularturrets.core.targeting.TurretAimHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public abstract class DirectedTurretBlockEntity extends BlockEntity {
    private float yaw = 0.0F;
    private float pitch = 180.0F;

    protected DirectedTurretBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void updateAim(ServerLevel level, TurretBaseBlockEntity base, double range) {
        Vec3 origin = Vec3.atCenterOf(worldPosition);
        TurretBaseBlockEntity.OptionalTarget target = base.findTarget(level, origin, range);
        if (target == null) {
            return;
        }
        float newYaw = TurretAimHelper.getAimYaw(target.entity(), worldPosition);
        float newPitch = TurretAimHelper.getAimPitch(target.entity(), worldPosition);
        if (newYaw != yaw || newPitch != pitch) {
            yaw = newYaw;
            pitch = newPitch;
            setChanged();
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        yaw = tag.getFloat("Yaw");
        pitch = tag.getFloat("Pitch");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putFloat("Yaw", yaw);
        tag.putFloat("Pitch", pitch);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
