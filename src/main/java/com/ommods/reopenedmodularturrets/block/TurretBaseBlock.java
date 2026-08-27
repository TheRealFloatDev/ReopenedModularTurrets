package com.ommods.reopenedmodularturrets.block;

import com.mojang.serialization.MapCodec;
import com.ommods.reopenedmodularturrets.blockentity.TurretBaseBlockEntity;
import com.ommods.reopenedmodularturrets.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class TurretBaseBlock extends BaseEntityBlock {
    public static final BooleanProperty CAMOUFLAGED = BooleanProperty.create("camouflaged");
    private final int tier;
    private final MapCodec<? extends TurretBaseBlock> codec;

    public TurretBaseBlock(int tier, Properties properties, MapCodec<? extends TurretBaseBlock> codec) {
        super(properties);
        this.tier = tier;
        this.codec = codec;
        registerDefaultState(stateDefinition.any().setValue(CAMOUFLAGED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CAMOUFLAGED);
    }

    public int getTier() {
        return tier;
    }

    @Override
    protected MapCodec<? extends TurretBaseBlock> codec() {
        return codec;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return state.getValue(CAMOUFLAGED) ? RenderShape.INVISIBLE : RenderShape.MODEL;
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof TurretBaseBlockEntity base && base.hasCamo()) {
            return base.getLightValue();
        }
        return super.getLightEmission(state, level, pos);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntities.TURRET_BASE.get().create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide()) {
            return null;
        }
        return createTickerHelper(type, ModBlockEntities.TURRET_BASE.get(), TurretBaseBlockEntity::serverTick);
    }

    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof TurretBaseBlockEntity base) {
            return new SimpleMenuProvider(
                    (id, inventory, player) -> base.createMenu(id, inventory),
                    Component.translatable("menu.reopenedmodularturrets.turret_base")
            );
        }
        return null;
    }

    @Override
    protected ItemInteractionResult useItemOn(
            ItemStack stack,
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            InteractionHand hand,
            BlockHitResult hitResult
    ) {
        if (!level.isClientSide() && !stack.isEmpty() && stack.getItem() instanceof BlockItem blockItem) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof TurretBaseBlockEntity base && base.canAccess(player)) {
                BlockState camoState = blockItem.getBlock().defaultBlockState();
                if (!camoState.isAir()) {
                    base.setCamoState(camoState);
                    return ItemInteractionResult.SUCCESS;
                }
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof TurretBaseBlockEntity base) {
            if (player.isShiftKeyDown() && base.hasCamo()) {
                if (!level.isClientSide() && base.canAccess(player)) {
                    base.clearCamo();
                }
                return InteractionResult.SUCCESS;
            }
            if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer && base.canAccess(player)) {
                MenuProvider provider = getMenuProvider(state, level, pos);
                if (provider != null) {
                    serverPlayer.openMenu(provider, pos);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected void neighborChanged(
            BlockState state,
            Level level,
            BlockPos pos,
            Block block,
            BlockPos fromPos,
            boolean isMoving
    ) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof TurretBaseBlockEntity base) {
            base.refreshNeighbors();
        }
    }

    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(
            BlockEntityType<A> actualType,
            BlockEntityType<E> expectedType,
            BlockEntityTicker<? super E> ticker
    ) {
        return expectedType == actualType ? (BlockEntityTicker<A>) ticker : null;
    }

    public static final class Tier1 extends TurretBaseBlock {
        public static final MapCodec<Tier1> CODEC = simpleCodec(Tier1::new);

        public Tier1(Properties properties) {
            super(1, properties, CODEC);
        }
    }

    public static final class Tier2 extends TurretBaseBlock {
        public static final MapCodec<Tier2> CODEC = simpleCodec(Tier2::new);

        public Tier2(Properties properties) {
            super(2, properties, CODEC);
        }
    }

    public static final class Tier3 extends TurretBaseBlock {
        public static final MapCodec<Tier3> CODEC = simpleCodec(Tier3::new);

        public Tier3(Properties properties) {
            super(3, properties, CODEC);
        }
    }

    public static final class Tier4 extends TurretBaseBlock {
        public static final MapCodec<Tier4> CODEC = simpleCodec(Tier4::new);

        public Tier4(Properties properties) {
            super(4, properties, CODEC);
        }
    }

    public static final class Tier5 extends TurretBaseBlock {
        public static final MapCodec<Tier5> CODEC = simpleCodec(Tier5::new);

        public Tier5(Properties properties) {
            super(5, properties, CODEC);
        }
    }
}
