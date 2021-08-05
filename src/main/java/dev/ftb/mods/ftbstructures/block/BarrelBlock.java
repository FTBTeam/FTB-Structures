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
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class BarrelBlock extends Block implements SimpleWaterloggedBlock {
	public static final VoxelShape SHAPE = Shapes.or(
			box(1, 1, 1, 15, 15, 15),
			box(0, 0, 1, 16, 1, 15),
			box(0, 15, 1, 1, 16, 15),
			box(15, 15, 1, 16, 16, 15),
			// box(11, 15, 3, 13, 16, 5),
			box(1, 0, 15, 15, 1, 16),
			box(1, 15, 15, 15, 16, 16),
			box(1, 0, 0, 15, 1, 1),
			box(1, 15, 0, 15, 16, 1),
			box(0.5, 10, 1, 1.5, 11, 15),
			box(14.5, 10, 1, 15.5, 11, 15),
			box(0.5, 5, 1, 1.5, 6, 15),
			box(14.5, 5, 1, 15.5, 6, 15),
			box(1, 5, 0.5, 15, 6, 1.5),
			box(1, 10, 0.5, 15, 11, 1.5),
			box(1, 10, 14.5, 15, 11, 15.5),
			box(1, 5, 14.5, 15, 6, 15.5)
	);

	public BarrelBlock() {
		super(Properties.of(Material.METAL).strength(5F, 6F).sound(SoundType.NETHERITE_BLOCK).noDrops().noOcclusion().harvestTool(ToolType.PICKAXE));
		registerDefaultState(defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> arg) {
		arg.add(BlockStateProperties.WATERLOGGED);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPE;
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
		return defaultBlockState().setValue(BlockStateProperties.WATERLOGGED, fluidState.getType() == Fluids.WATER);
	}

	@Override
	@Nullable
	public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor level, BlockPos pos, BlockPos facingPos) {
		if (state.getValue(BlockStateProperties.WATERLOGGED)) {
			level.getLiquidTicks().scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
		}

		return super.updateShape(state, facing, facingState, level, pos, facingPos);
	}

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

	@Override
	@Deprecated
	public VoxelShape getBlockSupportShape(BlockState arg, BlockGetter arg2, BlockPos arg3) {
		return Shapes.block();
	}
}
