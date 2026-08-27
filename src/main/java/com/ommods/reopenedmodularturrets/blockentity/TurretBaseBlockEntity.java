package com.ommods.reopenedmodularturrets.blockentity;

import com.ommods.reopenedmodularturrets.block.ExpanderPowerBlock;
import com.ommods.reopenedmodularturrets.config.ModConfig;
import com.ommods.reopenedmodularturrets.core.addons.AddonItems;
import com.ommods.reopenedmodularturrets.core.addons.AddonState;
import com.ommods.reopenedmodularturrets.core.ammo.AmmoStorage;
import com.ommods.reopenedmodularturrets.core.ownership.OwnedData;
import com.ommods.reopenedmodularturrets.core.ownership.TrustedPlayers;
import com.ommods.reopenedmodularturrets.core.upgrades.UpgradeModifiers;
import com.ommods.reopenedmodularturrets.item.AmmoItem;
import com.ommods.reopenedmodularturrets.item.AmmoType;
import com.ommods.reopenedmodularturrets.item.UpgradeItem;
import com.ommods.reopenedmodularturrets.menu.BaseSlotIndices;
import com.ommods.reopenedmodularturrets.menu.TurretBaseMenu;
import com.ommods.reopenedmodularturrets.registry.ModBlockEntities;
import com.ommods.reopenedmodularturrets.block.TurretBaseBlock;
import com.ommods.reopenedmodularturrets.block.TurretHeadBlock;
import com.ommods.reopenedmodularturrets.registry.ModBlocks;
import com.ommods.reopenedmodularturrets.registry.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.energy.EnergyStorage;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TurretBaseBlockEntity extends BlockEntity implements Container {
    private static final int POWER_EXPANDER_BONUS_PER_TIER = 10_000;

    private final int tier;
    private final int baseEnergyCapacity;
    private final OwnedData ownedData = new OwnedData();
    private final TrustedPlayers trustedPlayers = new TrustedPlayers();
    private final AmmoStorage ammoStorage;
    private final EnergyStorage energyStorage;
    private final ItemStack[] inventory = new ItemStack[BaseSlotIndices.BASE_SLOT_COUNT];

    private boolean attackMobs = true;
    private boolean attackPlayers = false;
    private boolean attackNeutral = false;
    private boolean multiTargeting = false;
    private boolean active = true;
    private int targetRange = 16;
    private int kills = 0;
    private int playerKills = 0;
    private int lightValue = 0;
    private int lightOpacity = 0;
    @Nullable
    private BlockState camoState;
    @Nullable
    private LivingEntity sharedTarget;

    private AddonState addonState = AddonState.EMPTY;
    private UpgradeModifiers upgradeModifiers = UpgradeModifiers.NONE;
    private int powerExpanderBonus = 0;

    private final List<TurretHeadBlockEntity> turretHeads = new ArrayList<>();
    private final List<ExpanderInventoryBlockEntity> inventoryExpanders = new ArrayList<>();

    public TurretBaseBlockEntity(BlockPos pos, BlockState state, int tier) {
        super(ModBlockEntities.TURRET_BASE.get(), pos, state);
        this.tier = tier;
        this.baseEnergyCapacity = 20000 * tier * tier;
        int transfer = 200 * tier * tier;
        this.energyStorage = new EnergyStorage(baseEnergyCapacity, transfer, transfer) {
            @Override
            public int getMaxEnergyStored() {
                return TurretBaseBlockEntity.this.getEffectiveMaxEnergy();
            }

            @Override
            public int receiveEnergy(int maxReceive, boolean simulate) {
                int received = super.receiveEnergy(maxReceive, simulate);
                if (!simulate && received > 0) {
                    TurretBaseBlockEntity.this.setChanged();
                }
                return received;
            }

            @Override
            public int extractEnergy(int maxExtract, boolean simulate) {
                int extracted = super.extractEnergy(maxExtract, simulate);
                if (!simulate && extracted > 0) {
                    TurretBaseBlockEntity.this.setChanged();
                }
                return extracted;
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

    public TrustedPlayers getTrustedPlayers() {
        return trustedPlayers;
    }

    public boolean addTrustedPlayer(String name) {
        if (trustedPlayers.add(name)) {
            setChanged();
            syncToClients();
            return true;
        }
        return false;
    }

    public boolean removeTrustedPlayer(String name) {
        if (trustedPlayers.remove(name)) {
            setChanged();
            syncToClients();
            return true;
        }
        return false;
    }

    private void syncToClients() {
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
        }
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

    public AddonState getAddonState() {
        return addonState;
    }

    public UpgradeModifiers getUpgradeModifiers() {
        return upgradeModifiers;
    }

    public List<ExpanderInventoryBlockEntity> getInventoryExpanders() {
        return inventoryExpanders;
    }

    public int getEffectiveMaxEnergy() {
        return baseEnergyCapacity + powerExpanderBonus;
    }

    public int getAmmoCount(AmmoType type) {
        int count = ammoStorage.getCount(type);
        int perItem = type.getAmmoPerItem();
        for (int slot = BaseSlotIndices.AMMO_START; slot < BaseSlotIndices.AMMO_START + BaseSlotIndices.AMMO_COUNT; slot++) {
            ItemStack stack = inventory[slot];
            if (stack.getItem() instanceof AmmoItem ammoItem && ammoItem.getAmmoType() == type) {
                count += stack.getCount() * perItem;
            }
        }
        return count;
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

    public boolean isMultiTargeting() {
        return multiTargeting;
    }

    public void setMultiTargeting(boolean multiTargeting) {
        this.multiTargeting = multiTargeting;
        setChanged();
        syncToClients();
    }

    public boolean isActive() {
        return active;
    }

    public void toggleActive() {
        this.active = !active;
        setChanged();
        syncToClients();
    }

    public int getTargetRange() {
        return targetRange;
    }

    public void adjustTargetRange(int delta) {
        int max = getMaxAllowedRange();
        targetRange = Math.max(1, Math.min(max, targetRange + delta));
        setChanged();
        syncToClients();
    }

    public int getMaxAllowedRange() {
        int max = 1;
        for (TurretHeadBlockEntity head : turretHeads) {
            double upgraded = getUpgradeModifiers().applyRange(head.getKind().getRange());
            max = Math.max(max, (int) Math.ceil(upgraded));
        }
        if (turretHeads.isEmpty()) {
            max = 32;
        }
        return max;
    }

    public int getKills() {
        return kills;
    }

    public int getPlayerKills() {
        return playerKills;
    }

    public int getLightValue() {
        return lightValue;
    }

    public int getLightOpacity() {
        return lightOpacity;
    }

    public void setLightValue(int value) {
        lightValue = Math.max(0, Math.min(15, value));
        setChanged();
        syncToClients();
    }

    public void setLightOpacity(int value) {
        lightOpacity = Math.max(0, Math.min(15, value));
        setChanged();
        syncToClients();
    }

    @Nullable
    public BlockState getCamoState() {
        return camoState;
    }

    public boolean hasCamo() {
        return camoState != null;
    }

    public void setCamoState(@Nullable BlockState state) {
        camoState = state;
        syncCamoBlockState();
        setChanged();
        syncToClients();
    }

    private void syncCamoBlockState() {
        if (level != null && !level.isClientSide()) {
            BlockState current = getBlockState();
            if (current.getBlock() instanceof TurretBaseBlock) {
                level.setBlock(worldPosition, current.setValue(TurretBaseBlock.CAMOUFLAGED, camoState != null), Block.UPDATE_ALL);
            }
        }
    }

    public void clearCamo() {
        setCamoState(null);
    }

    @Nullable
    public LivingEntity getSharedTarget() {
        return sharedTarget;
    }

    public void setSharedTarget(@Nullable LivingEntity sharedTarget) {
        this.sharedTarget = sharedTarget;
    }

    public double getEffectiveRange(double turretRange) {
        double upgraded = getUpgradeModifiers().applyRange(turretRange);
        return Math.min(upgraded, targetRange);
    }

    public void toggleFilter(com.ommods.reopenedmodularturrets.core.targeting.TargetFilter filter) {
        switch (filter) {
            case MOBS -> attackMobs = !attackMobs;
            case PLAYERS -> attackPlayers = !attackPlayers;
            case NEUTRAL -> attackNeutral = !attackNeutral;
        }
        setChanged();
        syncToClients();
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        tag.putBoolean("AttackMobs", attackMobs);
        tag.putBoolean("AttackPlayers", attackPlayers);
        tag.putBoolean("AttackNeutral", attackNeutral);
        tag.putBoolean("MultiTargeting", multiTargeting);
        tag.putBoolean("Active", active);
        tag.putInt("TargetRange", targetRange);
        tag.putInt("Kills", kills);
        tag.putInt("PlayerKills", playerKills);
        tag.putInt("LightValue", lightValue);
        tag.putInt("LightOpacity", lightOpacity);
        if (camoState != null) {
            tag.put("CamoState", net.minecraft.nbt.NbtUtils.writeBlockState(camoState));
        }
        trustedPlayers.save(tag);
        return tag;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider registries) {
        loadAdditional(tag, registries);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        refreshNeighbors();
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        if (level != null && !level.isClientSide()) {
            refreshNeighbors();
        }
    }

    public void refreshNeighbors() {
        if (level == null) {
            return;
        }
        turretHeads.clear();
        inventoryExpanders.clear();
        powerExpanderBonus = 0;

        for (Direction direction : Direction.values()) {
            BlockPos neighborPos = worldPosition.relative(direction);
            BlockEntity neighbor = level.getBlockEntity(neighborPos);
            BlockState neighborState = level.getBlockState(neighborPos);

            if (neighbor instanceof TurretHeadBlockEntity head) {
                turretHeads.add(head);
                head.bindBase(this);
            } else if (neighbor instanceof ExpanderInventoryBlockEntity expander) {
                inventoryExpanders.add(expander);
                expander.bindBase(this);
            } else if (neighborState.getBlock() instanceof ExpanderPowerBlock powerExpander) {
                powerExpanderBonus += POWER_EXPANDER_BONUS_PER_TIER * powerExpander.getTier();
            }
        }

        addonState = scanAddonState();
        upgradeModifiers = scanUpgrades();
        clampEnergy();
        setChanged();
    }

    private void clampEnergy() {
        int max = getEffectiveMaxEnergy();
        if (energyStorage.getEnergyStored() > max) {
            energyStorage.extractEnergy(energyStorage.getEnergyStored() - max, false);
        }
    }

    private AddonState scanAddonState() {
        boolean damageAmp = hasAddonItem(ModItems.ADDON_DAMAGE_AMP.get());
        boolean potentia = hasAddonItem(ModItems.ADDON_POTENTIA.get());
        boolean recycler = hasAddonItem(ModItems.ADDON_RECYCLER.get());
        boolean concealer = hasAddonItem(ModItems.ADDON_CONCEALER.get());
        boolean fakeDrops = hasAddonItem(ModItems.ADDON_FAKE_DROPS.get());
        boolean serialPort = hasAddonItem(ModItems.ADDON_SERIAL_PORT.get());
        return new AddonState(
                tier >= 2 && hasAddonItem(ModItems.SOLAR_ADDON_ITEM.get()),
                hasAddonItem(ModItems.REDSTONE_REACTOR_ADDON_ITEM.get()),
                hasAddonItem(ModItems.BASE_ADDON_LOOT_DELETER_ITEM.get()),
                damageAmp,
                potentia,
                recycler,
                concealer,
                fakeDrops,
                serialPort
        );
    }

    private boolean hasAddonItem(Item item) {
        for (int slot = BaseSlotIndices.ADDON_START; slot < BaseSlotIndices.ADDON_START + BaseSlotIndices.ADDON_COUNT; slot++) {
            if (inventory[slot].is(item)) {
                return true;
            }
        }
        return false;
    }

    public UpgradeModifiers scanUpgrades() {
        float fireRate = 1.0F;
        float energy = 1.0F;
        float range = 1.0F;
        float accuracy = 1.0F;
        boolean scatter = false;

        for (int slot = BaseSlotIndices.UPGRADE_START; slot < BaseSlotIndices.UPGRADE_START + BaseSlotIndices.UPGRADE_COUNT; slot++) {
            ItemStack stack = inventory[slot];
            if (stack.getItem() instanceof UpgradeItem upgrade) {
                switch (upgrade.getType()) {
                    case FIRE_RATE -> fireRate += 0.25F;
                    case EFFICIENCY -> energy *= 0.85F;
                    case RANGE -> range += 0.20F;
                    case ACCURACY -> accuracy += 0.25F;
                    case SCATTER_SHOT -> scatter = true;
                }
            }
        }
        return new UpgradeModifiers(fireRate, energy, range, accuracy, scatter);
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
        if (!active) {
            sharedTarget = null;
            return;
        }
        tickInventoryAddons(level);
        if (!multiTargeting) {
            sharedTarget = findSharedTarget(level);
        } else {
            sharedTarget = null;
        }
        for (TurretHeadBlockEntity head : turretHeads) {
            head.tickCombat(level, this);
        }
    }

    @Nullable
    private LivingEntity findSharedTarget(ServerLevel level) {
        if (turretHeads.isEmpty()) {
            return null;
        }
        TurretHeadBlockEntity first = turretHeads.getFirst();
        double range = getEffectiveRange(first.getKind().getRange());
        Vec3 origin = Vec3.atCenterOf(first.getBlockPos());
        OptionalTarget target = findTarget(level, origin, range);
        return target != null ? target.entity() : null;
    }

    private void tickInventoryAddons(ServerLevel level) {
        if (addonState.solar() && level.canSeeSky(worldPosition.above())) {
            int generation = ModConfig.SOLAR_GENERATION.get();
            int current = energyStorage.getEnergyStored();
            int capacity = getEffectiveMaxEnergy();
            energyStorage.receiveEnergy(Math.min(capacity - current, generation), false);
            setChanged();
        }
        if (addonState.redstoneReactor()) {
            int signal = level.getBestNeighborSignal(worldPosition);
            if (signal > 0) {
                int generation = signal * 4;
                int current = energyStorage.getEnergyStored();
                int capacity = getEffectiveMaxEnergy();
                energyStorage.receiveEnergy(Math.min(capacity - current, generation), false);
                setChanged();
            }
        }
    }

    public boolean consumeEnergy(int amount) {
        if (amount <= 0) {
            return true;
        }
        int adjusted = Math.max(1, Math.round(amount * addonState.energyMultiplier()));
        if (energyStorage.getEnergyStored() < adjusted) {
            return false;
        }
        int extracted = energyStorage.extractEnergy(adjusted, false);
        if (extracted < adjusted) {
            return false;
        }
        setChanged();
        return true;
    }

    public boolean consumeAmmo(AmmoType type, int amount) {
        if (type == null) {
            return true;
        }
        if (addonState.recycleChance() > 0.0F && level != null && level.random.nextFloat() < addonState.recycleChance()) {
            return true;
        }
        for (int slot = BaseSlotIndices.AMMO_START; slot < BaseSlotIndices.AMMO_START + BaseSlotIndices.AMMO_COUNT; slot++) {
            ItemStack stack = inventory[slot];
            if (stack.getItem() instanceof AmmoItem ammoItem && ammoItem.getAmmoType() == type && !stack.isEmpty()) {
                int perItem = type.getAmmoPerItem();
                int neededItems = (amount + perItem - 1) / perItem;
                int consumed = Math.min(neededItems, stack.getCount());
                stack.shrink(consumed);
                if (stack.isEmpty()) {
                    inventory[slot] = ItemStack.EMPTY;
                }
                setChanged();
                return true;
            }
        }
        if (ammoStorage.tryConsume(type, amount)) {
            setChanged();
            return true;
        }
        return false;
    }

    public boolean consumeDisposableAmmo() {
        for (int slot = BaseSlotIndices.AMMO_START; slot < BaseSlotIndices.AMMO_START + BaseSlotIndices.AMMO_COUNT; slot++) {
            ItemStack stack = inventory[slot];
            if (!stack.isEmpty()) {
                stack.shrink(1);
                if (stack.isEmpty()) {
                    inventory[slot] = ItemStack.EMPTY;
                }
                setChanged();
                return true;
            }
        }
        for (ExpanderInventoryBlockEntity expander : inventoryExpanders) {
            for (int slot = 0; slot < expander.getContainerSize(); slot++) {
                ItemStack stack = expander.getItem(slot);
                if (!stack.isEmpty()) {
                    stack.shrink(1);
                    if (stack.isEmpty()) {
                        expander.setItem(slot, ItemStack.EMPTY);
                    }
                    expander.setChanged();
                    setChanged();
                    return true;
                }
            }
        }
        return false;
    }

    public boolean consumePotatoAmmo() {
        for (int slot = BaseSlotIndices.AMMO_START; slot < BaseSlotIndices.AMMO_START + BaseSlotIndices.AMMO_COUNT; slot++) {
            if (tryConsumePotato(inventory[slot], slot, this)) {
                return true;
            }
        }
        for (ExpanderInventoryBlockEntity expander : inventoryExpanders) {
            for (int slot = 0; slot < expander.getContainerSize(); slot++) {
                ItemStack stack = expander.getItem(slot);
                if (isPotato(stack)) {
                    stack.shrink(1);
                    if (stack.isEmpty()) {
                        expander.setItem(slot, ItemStack.EMPTY);
                    }
                    expander.setChanged();
                    setChanged();
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean tryConsumePotato(ItemStack stack, int slot, TurretBaseBlockEntity base) {
        if (isPotato(stack)) {
            stack.shrink(1);
            if (stack.isEmpty()) {
                base.inventory[slot] = ItemStack.EMPTY;
            }
            base.setChanged();
            return true;
        }
        return false;
    }

    private static boolean isPotato(ItemStack stack) {
        return stack.is(Items.POTATO) || stack.is(Items.BAKED_POTATO) || stack.is(Items.POISONOUS_POTATO);
    }

    @Nullable
    public OptionalTarget findTarget(Level level, Vec3 origin, double range) {
        UUID ownerUuid = ownedData.getOwnerUuid().orElse(null);
        return com.ommods.reopenedmodularturrets.core.targeting.TargetingHelper.findTarget(
                level,
                origin,
                range,
                ModConfig.TARGETING_DOWN_RANGE.get(),
                attackMobs,
                attackPlayers,
                attackNeutral,
                ownerUuid,
                trustedPlayers.getNames()
        ).map(OptionalTarget::new).orElse(null);
    }

    public float getDamageMultiplier() {
        return addonState.damageMultiplier();
    }

    public void handleKill(LivingEntity target) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }
        kills++;
        if (target instanceof net.minecraft.world.entity.player.Player) {
            playerKills++;
        }
        setChanged();
        syncToClients();
        if (addonState.fakeDrops()) {
            ItemStack fake = new ItemStack(Items.ROTTEN_FLESH, 1 + serverLevel.random.nextInt(3));
            ItemEntity drop = new ItemEntity(serverLevel, target.getX(), target.getY(), target.getZ(), fake);
            serverLevel.addFreshEntity(drop);
        }
    }

    public boolean shouldDeleteLoot() {
        return addonState.lootDeleter();
    }

    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory) {
        return new TurretBaseMenu(containerId, playerInventory, this);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        ownedData.load(tag);
        trustedPlayers.load(tag);
        ammoStorage.load(tag);
        attackMobs = tag.getBoolean("AttackMobs");
        attackPlayers = tag.getBoolean("AttackPlayers");
        attackNeutral = tag.getBoolean("AttackNeutral");
        multiTargeting = tag.getBoolean("MultiTargeting");
        active = !tag.contains("Active") || tag.getBoolean("Active");
        targetRange = tag.contains("TargetRange") ? tag.getInt("TargetRange") : 16;
        kills = tag.getInt("Kills");
        playerKills = tag.getInt("PlayerKills");
        lightValue = tag.getInt("LightValue");
        lightOpacity = tag.getInt("LightOpacity");
        if (tag.contains("CamoState")) {
            camoState = net.minecraft.nbt.NbtUtils.readBlockState(registries.lookupOrThrow(net.minecraft.core.registries.Registries.BLOCK), tag.getCompound("CamoState"));
        } else {
            camoState = null;
        }
        syncCamoBlockState();
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
        trustedPlayers.save(tag);
        ammoStorage.save(tag);
        tag.putBoolean("AttackMobs", attackMobs);
        tag.putBoolean("AttackPlayers", attackPlayers);
        tag.putBoolean("AttackNeutral", attackNeutral);
        tag.putBoolean("MultiTargeting", multiTargeting);
        tag.putBoolean("Active", active);
        tag.putInt("TargetRange", targetRange);
        tag.putInt("Kills", kills);
        tag.putInt("PlayerKills", playerKills);
        tag.putInt("LightValue", lightValue);
        tag.putInt("LightOpacity", lightOpacity);
        if (camoState != null) {
            tag.put("CamoState", net.minecraft.nbt.NbtUtils.writeBlockState(camoState));
        }
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
        inventory[slot] = stack;
        setChanged();
        refreshItemDerivedState();
    }

    private void refreshItemDerivedState() {
        addonState = scanAddonState();
        upgradeModifiers = scanUpgrades();
    }

    private static boolean isAmmoSlot(int slot) {
        return slot >= BaseSlotIndices.AMMO_START && slot < BaseSlotIndices.AMMO_START + BaseSlotIndices.AMMO_COUNT;
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
        refreshItemDerivedState();
        return result;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack stack = inventory[slot];
        inventory[slot] = ItemStack.EMPTY;
        refreshItemDerivedState();
        return stack;
    }

    public void dropTurrets(ServerPlayer player) {
        if (!(level instanceof ServerLevel serverLevel) || !canAccess(player)) {
            return;
        }
        refreshNeighbors();
        for (TurretHeadBlockEntity head : new ArrayList<>(turretHeads)) {
            BlockPos headPos = head.getBlockPos();
            BlockState headState = serverLevel.getBlockState(headPos);
            if (headState.getBlock() instanceof TurretHeadBlock turretBlock) {
                Block.popResource(serverLevel, headPos, new ItemStack(turretBlock));
                serverLevel.removeBlock(headPos, false);
            }
        }
        refreshNeighbors();
    }

    public void dropBase(ServerPlayer player) {
        if (!(level instanceof ServerLevel serverLevel) || !canAccess(player)) {
            return;
        }
        dropTurrets(player);
        BlockState state = getBlockState();
        ItemStack baseStack = switch (tier) {
            case 1 -> new ItemStack(ModBlocks.TURRET_BASE_TIER_1.get());
            case 2 -> new ItemStack(ModBlocks.TURRET_BASE_TIER_2.get());
            case 3 -> new ItemStack(ModBlocks.TURRET_BASE_TIER_3.get());
            case 4 -> new ItemStack(ModBlocks.TURRET_BASE_TIER_4.get());
            case 5 -> new ItemStack(ModBlocks.TURRET_BASE_TIER_5.get());
            default -> new ItemStack(ModBlocks.TURRET_BASE_TIER_1.get());
        };
        Block.popResource(serverLevel, worldPosition, baseStack);
        serverLevel.removeBlock(worldPosition, false);
    }

    @Nullable
    public OptionalTarget findTargetForTurret(ServerLevel level, Vec3 origin, double turretRange, @Nullable LivingEntity preferred) {
        if (!multiTargeting && preferred != null && preferred.isAlive()) {
            return new OptionalTarget(preferred);
        }
        return findTarget(level, origin, getEffectiveRange(turretRange));
    }

    public record OptionalTarget(LivingEntity entity) {}
}
