package com.ommods.reopenedmodularturrets.menu;

import com.ommods.reopenedmodularturrets.blockentity.ExpanderInventoryBlockEntity;
import com.ommods.reopenedmodularturrets.blockentity.TurretBaseBlockEntity;
import com.ommods.reopenedmodularturrets.config.ModConfig;
import com.ommods.reopenedmodularturrets.core.addons.AddonItems;
import com.ommods.reopenedmodularturrets.core.targeting.TargetFilter;
import com.ommods.reopenedmodularturrets.item.AmmoItem;
import com.ommods.reopenedmodularturrets.item.AmmoType;
import com.ommods.reopenedmodularturrets.item.UpgradeItem;
import com.ommods.reopenedmodularturrets.menu.slot.LimitedStackSlot;
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
    public static final int DATA_COUNT = 23;

    private static final int ENERGY_STORED = 0;
    private static final int MAX_ENERGY = 1;
    private static final int FILTER_MOBS = 2;
    private static final int FILTER_PLAYERS = 3;
    private static final int FILTER_NEUTRAL = 4;
    private static final int AMMO_BULLET = 5;
    private static final int AMMO_GRENADE = 6;
    private static final int AMMO_BLAZING = 7;
    private static final int AMMO_FERRO = 8;
    private static final int AMMO_ROCKET = 9;
    private static final int ADDON_SOLAR = 10;
    private static final int ADDON_REACTOR = 11;
    private static final int ADDON_LOOT = 12;
    private static final int ADDON_DAMAGE = 13;
    private static final int TARGET_RANGE = 14;
    private static final int MAX_RANGE = 15;
    private static final int MULTI_TARGET = 16;
    private static final int ACTIVE = 17;
    private static final int KILLS = 18;
    private static final int PLAYER_KILLS = 19;
    private static final int LIGHT_VALUE = 20;
    private static final int LIGHT_OPACITY = 21;
    private static final int MACHINE_MODE = 22;

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
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int slot = BaseSlotIndices.AMMO_START + col + row * 3;
                this.addSlot(new LimitedStackSlot(blockEntity, slot, 8 + col * 18, 17 + row * 18, TurretBaseMenu::isAmmoSlotItem, 64));
            }
        }
        if (tier >= 2) {
            this.addSlot(new LimitedStackSlot(blockEntity, BaseSlotIndices.ADDON_START, 72, 18, AddonItems::isAddonItem, 1));
            this.addSlot(new LimitedStackSlot(blockEntity, BaseSlotIndices.ADDON_START + 1, 92, 18, AddonItems::isAddonItem, 1));
            this.addSlot(new LimitedStackSlot(blockEntity, BaseSlotIndices.UPGRADE_START, 72, 52, stack -> stack.getItem() instanceof UpgradeItem, ModConfig.UPGRADE_MAX_STACK.get()));
            if (tier >= 5) {
                this.addSlot(new LimitedStackSlot(blockEntity, BaseSlotIndices.UPGRADE_START + 1, 92, 52, stack -> stack.getItem() instanceof UpgradeItem, ModConfig.UPGRADE_MAX_STACK.get()));
            }
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
        syncedData[ENERGY_STORED] = base.getEnergyStorage().getEnergyStored();
        syncedData[MAX_ENERGY] = base.getEffectiveMaxEnergy();
        syncedData[FILTER_MOBS] = base.isAttackMobs() ? 1 : 0;
        syncedData[FILTER_PLAYERS] = base.isAttackPlayers() ? 1 : 0;
        syncedData[FILTER_NEUTRAL] = base.isAttackNeutral() ? 1 : 0;
        syncedData[AMMO_BULLET] = base.getAmmoCount(AmmoType.BULLET);
        syncedData[AMMO_GRENADE] = base.getAmmoCount(AmmoType.GRENADE);
        syncedData[AMMO_BLAZING] = base.getAmmoCount(AmmoType.BLAZING_CLAY);
        syncedData[AMMO_FERRO] = base.getAmmoCount(AmmoType.FERRO_SLUG);
        syncedData[AMMO_ROCKET] = base.getAmmoCount(AmmoType.ROCKET);
        syncedData[ADDON_SOLAR] = base.getAddonState().solar() ? 1 : 0;
        syncedData[ADDON_REACTOR] = base.getAddonState().redstoneReactor() ? 1 : 0;
        syncedData[ADDON_LOOT] = base.getAddonState().lootDeleter() ? 1 : 0;
        syncedData[ADDON_DAMAGE] = base.getAddonState().damageAmp() ? 1 : 0;
        syncedData[TARGET_RANGE] = base.getTargetRange();
        syncedData[MAX_RANGE] = base.getMaxAllowedRange();
        syncedData[MULTI_TARGET] = base.isMultiTargeting() ? 1 : 0;
        syncedData[ACTIVE] = base.isActive() ? 1 : 0;
        syncedData[KILLS] = base.getKills();
        syncedData[PLAYER_KILLS] = base.getPlayerKills();
        syncedData[LIGHT_VALUE] = base.getLightValue();
        syncedData[LIGHT_OPACITY] = base.getLightOpacity();
        syncedData[MACHINE_MODE] = base.getMachineMode().ordinal();
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
        return syncedData[ENERGY_STORED];
    }

    public int getMaxEnergy() {
        return syncedData[MAX_ENERGY];
    }

    public int getData(int index) {
        return syncedData[index];
    }

    public int getTargetRange() {
        return syncedData[TARGET_RANGE];
    }

    public int getMaxAllowedRange() {
        return syncedData[MAX_RANGE];
    }

    public boolean isMultiTargeting() {
        return syncedData[MULTI_TARGET] == 1;
    }

    public boolean isActive() {
        return syncedData[ACTIVE] == 1;
    }

    public int getMachineModeOrdinal() {
        return syncedData[MACHINE_MODE];
    }

    public void toggleFilterClient(TargetFilter filter) {
        int index = switch (filter) {
            case MOBS -> FILTER_MOBS;
            case PLAYERS -> FILTER_PLAYERS;
            case NEUTRAL -> FILTER_NEUTRAL;
        };
        syncedData[index] = syncedData[index] == 1 ? 0 : 1;
    }

    public void refreshFilterData() {
        if (base == null) {
            return;
        }
        syncedData[FILTER_MOBS] = base.isAttackMobs() ? 1 : 0;
        syncedData[FILTER_PLAYERS] = base.isAttackPlayers() ? 1 : 0;
        syncedData[FILTER_NEUTRAL] = base.isAttackNeutral() ? 1 : 0;
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
        } else if (stack.getItem() instanceof AmmoItem || stack.is(net.minecraft.world.item.Items.REDSTONE)) {
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

    private static boolean isAmmoSlotItem(ItemStack stack) {
        return stack.isEmpty()
                || stack.getItem() instanceof AmmoItem
                || stack.is(net.minecraft.world.item.Items.REDSTONE)
                || stack.is(net.minecraft.world.item.Items.POTATO)
                || stack.is(net.minecraft.world.item.Items.BAKED_POTATO)
                || stack.is(net.minecraft.world.item.Items.POISONOUS_POTATO);
    }
}
