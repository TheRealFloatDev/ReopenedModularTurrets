package com.ommods.reopenedmodularturrets.item;

import com.ommods.reopenedmodularturrets.block.TurretBaseBlock;
import com.ommods.reopenedmodularturrets.blockentity.TurretBaseBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class MemoryCardItem extends Item {
    public MemoryCardItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        Player player = context.getPlayer();
        ItemStack card = context.getItemInHand();
        if (player == null || !(state.getBlock() instanceof TurretBaseBlock)) {
            return InteractionResult.PASS;
        }
        if (!(level.getBlockEntity(pos) instanceof TurretBaseBlockEntity base)) {
            return InteractionResult.PASS;
        }
        if (!base.canAccess(player)) {
            return InteractionResult.FAIL;
        }
        if (!level.isClientSide()) {
            if (player.isShiftKeyDown()) {
                writeBaseToCard(card, base);
                player.displayClientMessage(Component.translatable("item.reopenedmodularturrets.memory_card.saved"), true);
            } else if (hasStoredData(card)) {
                applyCardToBase(card, base);
                player.displayClientMessage(Component.translatable("item.reopenedmodularturrets.memory_card.applied"), true);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, net.minecraft.world.InteractionHand hand) {
        ItemStack card = player.getItemInHand(hand);
        if (player.isShiftKeyDown() && hasStoredData(card) && !level.isClientSide()) {
            clearCard(card);
            player.displayClientMessage(Component.translatable("item.reopenedmodularturrets.memory_card.cleared"), true);
            return InteractionResultHolder.success(card);
        }
        return InteractionResultHolder.pass(card);
    }

    public static boolean hasStoredData(ItemStack card) {
        return card.has(DataComponents.CUSTOM_DATA) && card.get(DataComponents.CUSTOM_DATA).copyTag().getBoolean("HasData");
    }

    private static void clearCard(ItemStack card) {
        card.set(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
    }

    private static void writeBaseToCard(ItemStack card, TurretBaseBlockEntity base) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("HasData", true);
        tag.putBoolean("AttackMobs", base.isAttackMobs());
        tag.putBoolean("AttackPlayers", base.isAttackPlayers());
        tag.putBoolean("AttackNeutral", base.isAttackNeutral());
        base.getOwnedData().save(tag);
        card.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }

    private static void applyCardToBase(ItemStack card, TurretBaseBlockEntity base) {
        CompoundTag tag = card.get(DataComponents.CUSTOM_DATA).copyTag();
        if (tag.getBoolean("AttackMobs")) {
            if (!base.isAttackMobs()) {
                base.toggleFilter(com.ommods.reopenedmodularturrets.core.targeting.TargetFilter.MOBS);
            }
        } else if (base.isAttackMobs()) {
            base.toggleFilter(com.ommods.reopenedmodularturrets.core.targeting.TargetFilter.MOBS);
        }
        if (tag.getBoolean("AttackPlayers")) {
            if (!base.isAttackPlayers()) {
                base.toggleFilter(com.ommods.reopenedmodularturrets.core.targeting.TargetFilter.PLAYERS);
            }
        } else if (base.isAttackPlayers()) {
            base.toggleFilter(com.ommods.reopenedmodularturrets.core.targeting.TargetFilter.PLAYERS);
        }
        if (tag.getBoolean("AttackNeutral")) {
            if (!base.isAttackNeutral()) {
                base.toggleFilter(com.ommods.reopenedmodularturrets.core.targeting.TargetFilter.NEUTRAL);
            }
        } else if (base.isAttackNeutral()) {
            base.toggleFilter(com.ommods.reopenedmodularturrets.core.targeting.TargetFilter.NEUTRAL);
        }
        base.getOwnedData().load(tag);
        base.setChanged();
    }
}
