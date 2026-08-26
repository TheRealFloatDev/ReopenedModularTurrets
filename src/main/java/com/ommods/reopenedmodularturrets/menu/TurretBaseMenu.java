package com.ommods.reopenedmodularturrets.menu;

import com.ommods.reopenedmodularturrets.blockentity.TurretBaseBlockEntity;
import com.ommods.reopenedmodularturrets.registry.ModMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class TurretBaseMenu extends AbstractContainerMenu {
    private final TurretBaseBlockEntity base;
    private final ContainerData data;

    public TurretBaseMenu(int containerId, Inventory playerInventory, FriendlyByteBuf extraData) {
        this(containerId, playerInventory, extraData.readBlockPos());
    }

    public TurretBaseMenu(int containerId, Inventory playerInventory, BlockPos pos) {
        super(ModMenus.TURRET_BASE.get(), containerId);
        TurretBaseBlockEntity blockEntity = null;
        if (playerInventory.player.level().getBlockEntity(pos) instanceof TurretBaseBlockEntity baseEntity) {
            blockEntity = baseEntity;
        }
        this.base = blockEntity;
        this.data = createData(blockEntity);
        addDataSlots(data);

        if (blockEntity != null) {
            for (int row = 0; row < 2; row++) {
                for (int col = 0; col < 3; col++) {
                    int index = row * 3 + col;
                    this.addSlot(new Slot(blockEntity, index, 44 + col * 18, 20 + row * 18));
                }
            }
        }

        addPlayerInventory(playerInventory, 8, 84);
    }

    public TurretBaseMenu(int containerId, Inventory playerInventory, TurretBaseBlockEntity base) {
        super(ModMenus.TURRET_BASE.get(), containerId);
        this.base = base;
        this.data = createData(base);
        addDataSlots(data);
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 3; col++) {
                int index = row * 3 + col;
                this.addSlot(new Slot(base, index, 44 + col * 18, 20 + row * 18));
            }
        }
        addPlayerInventory(playerInventory, 8, 84);
    }

    private static ContainerData createData(@Nullable TurretBaseBlockEntity blockEntity) {
        return new ContainerData() {
            @Override
            public int get(int index) {
                if (blockEntity == null) {
                    return 0;
                }
                switch (index) {
                    case 0:
                        return blockEntity.getEnergyHandler().getAmountAsInt();
                    case 1:
                        return (int) blockEntity.getEnergyHandler().getCapacityAsLong();
                    case 2:
                        return blockEntity.isAttackMobs() ? 1 : 0;
                    case 3:
                        return blockEntity.isAttackPlayers() ? 1 : 0;
                    default:
                        return 0;
                }
            }

            @Override
            public void set(int index, int value) {
                // Target toggles are handled via network packets.
            }

            @Override
            public int getCount() {
                return 4;
            }
        };
    }

    private void addPlayerInventory(Inventory playerInventory, int left, int top) {
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, left + col * 18, top + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, left + col * 18, top + 58));
        }
    }

    @Nullable
    public TurretBaseBlockEntity getBase() {
        return base;
    }

    public int getEnergyStored() {
        return data.get(0);
    }

    public int getMaxEnergy() {
        return data.get(1);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return base != null && base.stillValid(player);
    }
}
