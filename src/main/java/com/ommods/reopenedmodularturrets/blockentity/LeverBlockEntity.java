package com.ommods.reopenedmodularturrets.blockentity;

import com.ommods.reopenedmodularturrets.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class LeverBlockEntity extends BlockEntity {
    private float crankRotation;
    private boolean turning;

    public LeverBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.LEVER_BLOCK.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, LeverBlockEntity lever) {
        lever.tickAnimation();
    }

    public void startCrank() {
        if (turning) {
            return;
        }
        turning = true;
        crankRotation = 0.0F;
        setChanged();
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public void tickAnimation() {
        if (!turning) {
            return;
        }
        crankRotation += 30.0F;
        if (crankRotation >= 360.0F) {
            crankRotation = 0.0F;
            turning = false;
            setChanged();
            if (level != null && !level.isClientSide()) {
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            }
        }
    }

    public float getCrankRotation(float partialTick) {
        if (!turning) {
            return 0.0F;
        }
        return crankRotation + 30.0F * partialTick;
    }

    public boolean isTurning() {
        return turning;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putFloat("CrankRotation", crankRotation);
        tag.putBoolean("Turning", turning);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        crankRotation = tag.getFloat("CrankRotation");
        turning = tag.getBoolean("Turning");
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
