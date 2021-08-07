package dev.ftb.mods.ftbstructures.block;

import dev.ftb.mods.ftbstructures.FTBStructuresData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class SmallCrateBlock extends Block {
	public static final VoxelShape SHAPE_EW = Shapes.or(
			box(2, 0, 0, 14, 7, 16),
			box(14, 0, 12, 15, 7, 14),
			box(14, 0, 2, 15, 7, 4),
			box(1, 0, 12, 2, 7, 14),
			box(1, 0, 2, 2, 7, 4),
			box(2, 7, 12, 14, 8, 14),
			box(2, 7, 2, 14, 8, 4)
	);

	public static final VoxelShape SHAPE_NS = Shapes.or(
			box(0, 0, 2, 16, 7, 14),
			box(12, 0, 1, 14, 7, 2),
			box(2, 0, 1, 4, 7, 2),
			box(12, 0, 14, 14, 7, 15),
			box(2, 0, 14, 4, 7, 15),
			box(12, 7, 2, 14, 8, 14),
			box(2, 7, 2, 4, 8, 14)
	);

	public SmallCrateBlock(Properties properties) {
		super(properties);
		registerDefaultState(defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH).setValue(BlockStateProperties.WATERLOGGED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> arg) {
		arg.add(BlockStateProperties.HORIZONTAL_FACING, BlockStateProperties.WATERLOGGED);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return state.getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis() == Direction.Axis.X ? SHAPE_EW : SHAPE_NS;
	}

	@Override
	@Deprecated
	public FluidState getFluidState(BlockState state) {
		return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext arg) {
		FluidState fluidState = arg.getLevel().getFluidState(arg.getClickedPos());
		return defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, arg.getHorizontalDirection()).setValue(BlockStateProperties.WATERLOGGED, fluidState.getType() == Fluids.WATER);
	}

	@Override
	@Deprecated
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos pos, BlockPos facingPos) {
		if (state.getValue(BlockStateProperties.WATERLOGGED)) {
			level.getLiquidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}

		return super.updateShape(state, facing, facingState, level, pos, facingPos);
	}

	// TODO: Change this to GUI

	@Override
	@Deprecated
	public void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack stack) {
		super.spawnAfterBreak(state, level, pos, stack);

		if (level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
			for (ItemStack is : FTBStructuresData.getItems(asItem(), level.random)) {
				popResource(level, pos, is);
			}
		}
	}
}
