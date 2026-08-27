package com.ommods.reopenedmodularturrets.blockentity;

import com.ommods.reopenedmodularturrets.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ExpanderInventoryBlockEntity extends BlockEntity implements Container {
    public static final int SLOTS = 9;
    private final int tier;
    private final ItemStack[] inventory = new ItemStack[SLOTS];
    @Nullable
    private TurretBaseBlockEntity base;

    public ExpanderInventoryBlockEntity(BlockPos pos, BlockState state, int tier) {
        super(ModBlockEntities.EXPANDER_INVENTORY.get(), pos, state);
        this.tier = tier;
        for (int i = 0; i < inventory.length; i++) {
            inventory[i] = ItemStack.EMPTY;
        }
    }

    public int getTier() {
        return tier;
    }

    public void bindBase(@Nullable TurretBaseBlockEntity base) {
        this.base = base;
    }

    @Nullable
    public TurretBaseBlockEntity getBase() {
        return base;
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, ExpanderInventoryBlockEntity expander) {
        // Inventory expander is passive; binding happens from the base refresh cycle.
    }

    @Override
    public int getContainerSize() {
        return inventory.length;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : inventory) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return inventory[slot];
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        inventory[slot] = stack;
        setChanged();
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (base != null) {
            base.setChanged();
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void clearContent() {
        for (int i = 0; i < inventory.length; i++) {
            inventory[i] = ItemStack.EMPTY;
        }
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack stack = inventory[slot];
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        }
        ItemStack result = stack.split(amount);
        setChanged();
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = inventory[slot];
        inventory[slot] = ItemStack.EMPTY;
        return stack;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        for (int i = 0; i < inventory.length; i++) {
            if (tag.contains("Item" + i)) {
                inventory[i] = ItemStack.parseOptional(registries, tag.getCompound("Item" + i));
            } else {
                inventory[i] = ItemStack.EMPTY;
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        for (int i = 0; i < inventory.length; i++) {
            if (!inventory[i].isEmpty()) {
                tag.put("Item" + i, inventory[i].saveOptional(registries));
            }
        }
    }
}
