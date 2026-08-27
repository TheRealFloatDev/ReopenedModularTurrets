package com.ommods.reopenedmodularturrets.menu;

import com.ommods.reopenedmodularturrets.blockentity.ExpanderInventoryBlockEntity;
import com.ommods.reopenedmodularturrets.blockentity.TurretBaseBlockEntity;
import com.ommods.reopenedmodularturrets.core.addons.AddonItems;
import com.ommods.reopenedmodularturrets.item.AmmoItem;
import com.ommods.reopenedmodularturrets.item.AmmoType;
import com.ommods.reopenedmodularturrets.item.UpgradeItem;
import com.ommods.reopenedmodularturrets.menu.slot.FilteredSlot;
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

import java.util.ArrayList;
import java.util.List;

public class TurretBaseMenu extends AbstractContainerMenu {
    private static final int DATA_COUNT = 14;

    private final TurretBaseBlockEntity base;
    private final ContainerData data;
    private final List<ExpanderInventoryBlockEntity> expanders = new ArrayList<>();
    private final int expanderSlotCount;

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

        int expanderSlots = 0;
        if (blockEntity != null) {
            expanders.addAll(blockEntity.getInventoryExpanders());
            for (ExpanderInventoryBlockEntity expander : expanders) {
                expanderSlots += expander.getContainerSize();
            }
            addBaseSlots(blockEntity);
            addExpanderSlots();
        }
        this.expanderSlotCount = expanderSlots;
        addPlayerInventory(playerInventory, 8, 84 + (expanderSlots > 0 ? 18 : 0));
    }

    public TurretBaseMenu(int containerId, Inventory playerInventory, TurretBaseBlockEntity base) {
        super(ModMenus.TURRET_BASE.get(), containerId);
        this.base = base;
        this.data = createData(base);
        addDataSlots(data);
        expanders.addAll(base.getInventoryExpanders());
        int expanderSlots = 0;
        for (ExpanderInventoryBlockEntity expander : expanders) {
            expanderSlots += expander.getContainerSize();
        }
        addBaseSlots(base);
        addExpanderSlots();
        this.expanderSlotCount = expanderSlots;
        addPlayerInventory(playerInventory, 8, 84 + (expanderSlots > 0 ? 18 : 0));
    }

    private void addBaseSlots(TurretBaseBlockEntity blockEntity) {
        this.addSlot(new FilteredSlot(blockEntity, BaseSlotIndices.AMMO_START, 26, 20, stack -> stack.getItem() instanceof AmmoItem));
        this.addSlot(new FilteredSlot(blockEntity, BaseSlotIndices.AMMO_START + 1, 44, 20, stack -> stack.getItem() instanceof AmmoItem));
        this.addSlot(new FilteredSlot(blockEntity, BaseSlotIndices.UPGRADE_START, 26, 38, stack -> stack.getItem() instanceof UpgradeItem));
        this.addSlot(new FilteredSlot(blockEntity, BaseSlotIndices.UPGRADE_START + 1, 44, 38, stack -> stack.getItem() instanceof UpgradeItem));
        this.addSlot(new FilteredSlot(blockEntity, BaseSlotIndices.ADDON_START, 26, 56, AddonItems::isAddonItem));
        this.addSlot(new FilteredSlot(blockEntity, BaseSlotIndices.ADDON_START + 1, 44, 56, AddonItems::isAddonItem));
    }

    private void addExpanderSlots() {
        int slotIndex = BaseSlotIndices.BASE_SLOT_COUNT;
        int row = 0;
        int col = 0;
        for (ExpanderInventoryBlockEntity expander : expanders) {
            for (int i = 0; i < expander.getContainerSize(); i++) {
                this.addSlot(new Slot(expander, i, 62 + col * 18, 74 + row * 18));
                slotIndex++;
                col++;
                if (col >= 9) {
                    col = 0;
                    row++;
                }
            }
        }
    }

    private static ContainerData createData(@Nullable TurretBaseBlockEntity blockEntity) {
        return new ContainerData() {
            @Override
            public int get(int index) {
                if (blockEntity == null) {
                    return 0;
                }
                return switch (index) {
                    case 0 -> blockEntity.getEnergyStorage().getEnergyStored();
                    case 1 -> blockEntity.getEffectiveMaxEnergy();
                    case 2 -> blockEntity.isAttackMobs() ? 1 : 0;
                    case 3 -> blockEntity.isAttackPlayers() ? 1 : 0;
                    case 4 -> blockEntity.isAttackNeutral() ? 1 : 0;
                    case 5 -> blockEntity.getAmmoCount(AmmoType.BULLET);
                    case 6 -> blockEntity.getAmmoCount(AmmoType.GRENADE);
                    case 7 -> blockEntity.getAmmoCount(AmmoType.BLAZING_CLAY);
                    case 8 -> blockEntity.getAmmoCount(AmmoType.FERRO_SLUG);
                    case 9 -> blockEntity.getAmmoCount(AmmoType.ROCKET);
                    case 10 -> blockEntity.getAddonState().solar() ? 1 : 0;
                    case 11 -> blockEntity.getAddonState().redstoneReactor() ? 1 : 0;
                    case 12 -> blockEntity.getAddonState().lootDeleter() ? 1 : 0;
                    case 13 -> blockEntity.getAddonState().damageAmp() ? 1 : 0;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                // Target toggles are handled via network packets.
            }

            @Override
            public int getCount() {
                return DATA_COUNT;
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

    public int getData(int index) {
        return data.get(index);
    }

    private int baseSlotCount() {
        return BaseSlotIndices.BASE_SLOT_COUNT + expanderSlotCount;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }
        ItemStack stack = slot.getItem();
        result = stack.copy();
        int baseSlots = baseSlotCount();
        if (index < baseSlots) {
            if (!this.moveItemStackTo(stack, baseSlots, this.slots.size(), true)) {
                return ItemStack.EMPTY;
            }
        } else if (stack.getItem() instanceof AmmoItem) {
            if (!this.moveItemStackTo(stack, BaseSlotIndices.AMMO_START, BaseSlotIndices.AMMO_START + BaseSlotIndices.AMMO_COUNT, false)
                    && !this.moveItemStackTo(stack, BaseSlotIndices.BASE_SLOT_COUNT, baseSlots, false)) {
                return ItemStack.EMPTY;
            }
        } else if (stack.getItem() instanceof UpgradeItem) {
            if (!this.moveItemStackTo(stack, BaseSlotIndices.UPGRADE_START, BaseSlotIndices.UPGRADE_START + BaseSlotIndices.UPGRADE_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else if (AddonItems.isAddonItem(stack)) {
            if (!this.moveItemStackTo(stack, BaseSlotIndices.ADDON_START, BaseSlotIndices.ADDON_START + BaseSlotIndices.ADDON_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else if (!this.moveItemStackTo(stack, BaseSlotIndices.BASE_SLOT_COUNT, baseSlots, false)
                && !this.moveItemStackTo(stack, 0, BaseSlotIndices.BASE_SLOT_COUNT, false)) {
            return ItemStack.EMPTY;
        }
        if (stack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }
        return result;
    }

    @Override
    public boolean stillValid(Player player) {
        return base != null && base.stillValid(player);
    }
}
