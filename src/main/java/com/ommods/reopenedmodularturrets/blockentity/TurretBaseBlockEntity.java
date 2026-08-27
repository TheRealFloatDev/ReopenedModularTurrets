package com.ommods.reopenedmodularturrets.blockentity;

import com.ommods.reopenedmodularturrets.config.ModConfig;
import com.ommods.reopenedmodularturrets.core.ammo.AmmoStorage;
import com.ommods.reopenedmodularturrets.core.ownership.OwnedData;
import com.ommods.reopenedmodularturrets.item.AmmoType;
import com.ommods.reopenedmodularturrets.menu.TurretBaseMenu;
import com.ommods.reopenedmodularturrets.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.energy.EnergyStorage;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TurretBaseBlockEntity extends BlockEntity implements Container {
    private final int tier;
    private final OwnedData ownedData = new OwnedData();
    private final AmmoStorage ammoStorage;
    private final EnergyStorage energyStorage;
    private final ItemStack[] inventory = new ItemStack[6];
    private boolean attackMobs = true;
    private boolean attackPlayers = false;
    private boolean attackNeutral = false;

    private final List<TurretHeadBlockEntity> turretHeads = new ArrayList<>();
    private final List<SolarAddonBlockEntity> solarAddons = new ArrayList<>();

    public TurretBaseBlockEntity(BlockPos pos, BlockState state, int tier) {
        super(ModBlockEntities.TURRET_BASE.get(), pos, state);
        this.tier = tier;
        int capacity = 20000 * tier * tier;
        int transfer = 200 * tier * tier;
        this.energyStorage = new EnergyStorage(capacity, transfer, 0) {
            @Override
            public int receiveEnergy(int maxReceive, boolean simulate) {
                int received = super.receiveEnergy(maxReceive, simulate);
                if (!simulate && received > 0) {
                    TurretBaseBlockEntity.this.setChanged();
                }
                return received;
            }
        };
        this.ammoStorage = new AmmoStorage((int) Math.round(Math.pow(2, tier + 2)) * 128);
        for (int i = 0; i < inventory.length; i++) {
            inventory[i] = ItemStack.EMPTY;
        }
    }

    public int getTier() {
        return tier;
    }

    public OwnedData getOwnedData() {
        return ownedData;
    }

    public boolean canAccess(Player player) {
        return ownedData.canAccess(player);
    }

    public EnergyStorage getEnergyStorage() {
        return energyStorage;
    }

    public AmmoStorage getAmmoStorage() {
        return ammoStorage;
    }

    public boolean isAttackMobs() {
        return attackMobs;
    }

    public boolean isAttackPlayers() {
        return attackPlayers;
    }

    public boolean isAttackNeutral() {
        return attackNeutral;
    }

    public void toggleFilter(com.ommods.reopenedmodularturrets.core.targeting.TargetFilter filter) {
        switch (filter) {
            case MOBS -> attackMobs = !attackMobs;
            case PLAYERS -> attackPlayers = !attackPlayers;
            case NEUTRAL -> attackNeutral = !attackNeutral;
        }
        setChanged();
    }

    public void refreshNeighbors() {
        if (level == null) {
            return;
        }
        turretHeads.clear();
        solarAddons.clear();
        for (Direction direction : Direction.values()) {
            BlockEntity neighbor = level.getBlockEntity(worldPosition.relative(direction));
            if (neighbor instanceof TurretHeadBlockEntity head) {
                turretHeads.add(head);
                head.bindBase(this);
            } else if (neighbor instanceof SolarAddonBlockEntity solar) {
                solarAddons.add(solar);
                solar.bindBase(this);
            }
        }
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, TurretBaseBlockEntity base) {
        if (level.isClientSide()) {
            return;
        }
        base.tickTurrets((ServerLevel) level);
    }

    private void tickTurrets(ServerLevel level) {
        if (turretHeads.isEmpty()) {
            refreshNeighbors();
        }
        for (SolarAddonBlockEntity solar : solarAddons) {
            solar.tickGeneration(level);
        }
        for (TurretHeadBlockEntity head : turretHeads) {
            head.tickCombat(level, this);
        }
    }

    public boolean consumeEnergy(int amount) {
        if (energyStorage.getEnergyStored() < amount) {
            return false;
        }
        energyStorage.extractEnergy(amount, false);
        setChanged();
        return true;
    }

    public boolean consumeAmmo(AmmoType type, int amount) {
        if (ammoStorage.tryConsume(type, amount)) {
            setChanged();
            return true;
        }
        return false;
    }

    @Nullable
    public OptionalTarget findTarget(Level level, Vec3 origin, double range) {
        return com.ommods.reopenedmodularturrets.core.targeting.TargetingHelper.findTarget(
                level, origin, range, attackMobs, attackPlayers, attackNeutral
        ).map(OptionalTarget::new).orElse(null);
    }

    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory) {
        return new TurretBaseMenu(containerId, playerInventory, this);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        ownedData.load(tag);
        ammoStorage.load(tag);
        attackMobs = tag.getBoolean("AttackMobs");
        attackPlayers = tag.getBoolean("AttackPlayers");
        attackNeutral = tag.getBoolean("AttackNeutral");
        if (tag.contains("Energy")) {
            energyStorage.deserializeNBT(registries, tag.get("Energy"));
        }
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
        ownedData.save(tag);
        ammoStorage.save(tag);
        tag.putBoolean("AttackMobs", attackMobs);
        tag.putBoolean("AttackPlayers", attackPlayers);
        tag.putBoolean("AttackNeutral", attackNeutral);
        tag.put("Energy", energyStorage.serializeNBT(registries));
        for (int i = 0; i < inventory.length; i++) {
            if (!inventory[i].isEmpty()) {
                tag.put("Item" + i, inventory[i].saveOptional(registries));
            }
        }
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
        if (!stack.isEmpty()) {
            ItemStack working = stack.copy();
            if (ammoStorage.tryInsertStack(working)) {
                inventory[slot] = working;
                setChanged();
                return;
            }
        }
        inventory[slot] = stack;
        setChanged();
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    public boolean stillValid(Player player) {
        if (level == null || level.getBlockEntity(worldPosition) != this) {
            return false;
        }
        return player.distanceToSqr(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5) <= 64.0;
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
        int removed = Math.min(amount, stack.getCount());
        ItemStack result = stack.split(removed);
        setChanged();
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = inventory[slot];
        inventory[slot] = ItemStack.EMPTY;
        return stack;
    }

    public record OptionalTarget(LivingEntity entity) {}
}
