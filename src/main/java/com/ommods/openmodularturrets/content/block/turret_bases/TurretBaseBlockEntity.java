package com.ommods.openmodularturrets.content.block.turret_bases;

import com.ommods.omlib.block.BlockEntityStorageEnergy;
import com.ommods.openmodularturrets.content.item.ammo.AmmoDropEvent;
import com.ommods.openmodularturrets.content.block.turrets.Turret;
import com.ommods.openmodularturrets.content.block.turrets.TurretBlockEntity;
import com.ommods.openmodularturrets.content.item.upgrades.AbstractUpgradeItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.energy.EnergyStorage;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TurretBaseBlockEntity extends BlockEntityStorageEnergy {
    // Two slots for ammo input, two for ammo output, the remaining two are for upgrades for the base
    private static final int[] SLOT_CAPACITY = new int[]{64, 64, 64, 64, 4, 4};
    private final TurretBaseLogic logic;

    public TurretBaseBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState, int tier) {
        super(pType, pPos, pBlockState, new EnergyStorage(20000 * tier * tier, 200 * tier * tier, 0));

    // capacity is 2 ^ (tier + 2) * 128, ammo storage has 2 slots for different ammo types.
    // tier 1: 1024 (16 stacks), tier 2: 2048 (32 stacks), tier 3: 4096 (64 stacks), tier 4: 8192 (128 stacks), tier 5: 16384 (256 stacks)
    this.logic = new TurretBaseLogic(tier);

    }

    public void updateTurrets() {
        if (getLevel() == null || getLevel().isClientSide()) {
            return;
        }
        List<Turret> turrets = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            BlockEntity blockEntity = getLevel().getBlockEntity(this.getBlockPos().relative(direction));
            if (blockEntity instanceof TurretBlockEntity) {
                turrets.add(((TurretBlockEntity) blockEntity).getTurret());
            }
        }
        logic.setTurrets(turrets);
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T be) {
        TurretBaseBlockEntity b = (TurretBaseBlockEntity) be;

        //  TODO: Write update method
    }

    public void handleAmmoDropEvent(AmmoDropEvent event) {
        // TODO: write
    }

    @Override
    public int getSlots() {
        return SLOT_CAPACITY.length;
    }

    @Override
    public int getSlotLimit(int slot) {
        return SLOT_CAPACITY[slot];
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        if (slot > 2) {
            return stack.getItem() instanceof AbstractUpgradeItem;
        }
        return true;
    }

    public int getTier() {
        return logic.getTier();
    }
}
