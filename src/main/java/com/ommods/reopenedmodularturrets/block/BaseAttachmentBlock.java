package com.ommods.reopenedmodularturrets.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public abstract class BaseAttachmentBlock extends BaseEntityBlock {
    public static final net.minecraft.world.level.block.state.properties.EnumProperty<Direction> FACING =
            net.minecraft.world.level.block.state.properties.BlockStateProperties.FACING;

    protected BaseAttachmentBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected abstract MapCodec<? extends BaseAttachmentBlock> codec();

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return attachmentShape(state.getValue(FACING));
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return attachmentShape(state.getValue(FACING));
    }

    public static VoxelShape attachmentShape(Direction facing) {
        return SHAPES.getOrDefault(facing, Shapes.block());
    }

    private static final java.util.Map<Direction, VoxelShape> SHAPES = createShapes();

    private static java.util.Map<Direction, VoxelShape> createShapes() {
        java.util.EnumMap<Direction, VoxelShape> shapes = new java.util.EnumMap<>(Direction.class);
        VoxelShape box = Shapes.box(-0.375D, -0.375D, -0.1875D, 0.375D, 0.375D, 0.1875D);
        for (Direction facing : Direction.values()) {
            shapes.put(facing, offsetShape(box, facing));
        }
        return shapes;
    }

    private static VoxelShape offsetShape(VoxelShape shape, Direction facing) {
        return shape.move(
                facing.getStepX() * 0.325D,
                facing.getStepY() * 0.325D,
                facing.getStepZ() * 0.325D
        );
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return AddonAttachmentHelper.getAttachmentStateForPlacement(context, defaultBlockState());
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        BlockPos basePos = pos.relative(facing);
        return level.getBlockState(basePos).getBlock() instanceof TurretBaseBlock;
    }

    @Override
    protected BlockState updateShape(
            BlockState state,
            Direction direction,
            BlockState neighborState,
            LevelAccessor level,
            BlockPos pos,
            BlockPos neighborPos
    ) {
        return canSurvive(state, level, pos) ? state : net.minecraft.world.level.block.Blocks.AIR.defaultBlockState();
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.onPlace(state, level, pos, oldState, isMoving);
        AddonAttachmentHelper.refreshAdjacentBases(level, pos);
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
        if (!canSurvive(state, level, pos)) {
            level.destroyBlock(pos, true);
            return;
        }
        AddonAttachmentHelper.refreshAdjacentBases(level, pos);
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, net.minecraft.world.entity.player.Player player) {
        AddonAttachmentHelper.refreshAdjacentBases(level, pos);
        return super.playerWillDestroy(level, pos, state, player);
    }
}
