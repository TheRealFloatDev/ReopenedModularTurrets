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
    public static final int DATA_COUNT = 14;

    private final TurretBaseBlockEntity base;
    private final int[] syncedData = new int[DATA_COUNT];
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
        this.data = createData();
        addDataSlots(data);
        refreshData();

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
        addPlayerInventory(playerInventory, 8, 84);
    }

    public TurretBaseMenu(int containerId, Inventory playerInventory, TurretBaseBlockEntity base) {
        super(ModMenus.TURRET_BASE.get(), containerId);
        this.base = base;
        this.data = createData();
        addDataSlots(data);
        refreshData();
        expanders.addAll(base.getInventoryExpanders());
        int expanderSlots = 0;
        for (ExpanderInventoryBlockEntity expander : expanders) {
            expanderSlots += expander.getContainerSize();
        }
        addBaseSlots(base);
        addExpanderSlots();
        this.expanderSlotCount = expanderSlots;
        addPlayerInventory(playerInventory, 8, 84);
    }

    private void addBaseSlots(TurretBaseBlockEntity blockEntity) {
        int tier = blockEntity.getTier();
        if (tier >= 2) {
            this.addSlot(new FilteredSlot(blockEntity, BaseSlotIndices.AMMO_START, 8, 17, stack -> stack.getItem() instanceof AmmoItem));
            this.addSlot(new FilteredSlot(blockEntity, BaseSlotIndices.AMMO_START + 1, 26, 17, stack -> stack.getItem() instanceof AmmoItem));
            this.addSlot(new FilteredSlot(blockEntity, BaseSlotIndices.ADDON_START, 72, 18, AddonItems::isAddonItem));
            this.addSlot(new FilteredSlot(blockEntity, BaseSlotIndices.ADDON_START + 1, 92, 18, AddonItems::isAddonItem));
            this.addSlot(new FilteredSlot(blockEntity, BaseSlotIndices.UPGRADE_START, 72, 52, stack -> stack.getItem() instanceof UpgradeItem));
            if (tier >= 5) {
                this.addSlot(new FilteredSlot(blockEntity, BaseSlotIndices.UPGRADE_START + 1, 92, 52, stack -> stack.getItem() instanceof UpgradeItem));
            } else {
                this.addSlot(new FilteredSlot(blockEntity, BaseSlotIndices.UPGRADE_START + 1, 92, 52, stack -> stack.getItem() instanceof UpgradeItem));
            }
        } else {
            this.addSlot(new FilteredSlot(blockEntity, BaseSlotIndices.AMMO_START, 8, 17, stack -> stack.getItem() instanceof AmmoItem));
            this.addSlot(new FilteredSlot(blockEntity, BaseSlotIndices.AMMO_START + 1, 26, 17, stack -> stack.getItem() instanceof AmmoItem));
            this.addSlot(new FilteredSlot(blockEntity, BaseSlotIndices.UPGRADE_START, 44, 17, stack -> stack.getItem() instanceof UpgradeItem));
            this.addSlot(new FilteredSlot(blockEntity, BaseSlotIndices.UPGRADE_START + 1, 8, 35, stack -> stack.getItem() instanceof UpgradeItem));
            this.addSlot(new FilteredSlot(blockEntity, BaseSlotIndices.ADDON_START, 26, 35, AddonItems::isAddonItem));
            this.addSlot(new FilteredSlot(blockEntity, BaseSlotIndices.ADDON_START + 1, 44, 35, AddonItems::isAddonItem));
        }
    }

    private void addExpanderSlots() {
        int col = 0;
        int row = 0;
        for (ExpanderInventoryBlockEntity expander : expanders) {
            for (int i = 0; i < expander.getContainerSize(); i++) {
                this.addSlot(new Slot(expander, i, 62 + col * 18, 74 + row * 18));
                col++;
                if (col >= 3) {
                    col = 0;
                    row++;
                }
            }
        }
    }

    private ContainerData createData() {
        return new ContainerData() {
            @Override
            public int get(int index) {
                return syncedData[index];
            }

            @Override
            public void set(int index, int value) {
                syncedData[index] = value;
            }

            @Override
            public int getCount() {
                return DATA_COUNT;
            }
        };
    }

    public void refreshData() {
        if (base == null) {
            return;
        }
        syncedData[0] = base.getEnergyStorage().getEnergyStored();
        syncedData[1] = base.getEffectiveMaxEnergy();
        syncedData[2] = base.isAttackMobs() ? 1 : 0;
        syncedData[3] = base.isAttackPlayers() ? 1 : 0;
        syncedData[4] = base.isAttackNeutral() ? 1 : 0;
        syncedData[5] = base.getAmmoCount(AmmoType.BULLET);
        syncedData[6] = base.getAmmoCount(AmmoType.GRENADE);
        syncedData[7] = base.getAmmoCount(AmmoType.BLAZING_CLAY);
        syncedData[8] = base.getAmmoCount(AmmoType.FERRO_SLUG);
        syncedData[9] = base.getAmmoCount(AmmoType.ROCKET);
        syncedData[10] = base.getAddonState().solar() ? 1 : 0;
        syncedData[11] = base.getAddonState().redstoneReactor() ? 1 : 0;
        syncedData[12] = base.getAddonState().lootDeleter() ? 1 : 0;
        syncedData[13] = base.getAddonState().damageAmp() ? 1 : 0;
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
        return syncedData[0];
    }

    public int getMaxEnergy() {
        return syncedData[1];
    }

    public int getData(int index) {
        return syncedData[index];
    }

    private int baseSlotCount() {
        return BaseSlotIndices.BASE_SLOT_COUNT + expanderSlotCount;
    }

    @Override
    public void broadcastChanges() {
        if (base != null && base.getLevel() != null && !base.getLevel().isClientSide()) {
            refreshData();
        }
        super.broadcastChanges();
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
