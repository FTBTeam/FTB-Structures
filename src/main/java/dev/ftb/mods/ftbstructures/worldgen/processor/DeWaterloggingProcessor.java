package dev.ftb.mods.ftbstructures.worldgen.processor;

import com.mojang.serialization.Codec;
import dev.ftb.mods.ftbstructures.worldgen.FTBStructuresStructures;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;

public class DeWaterloggingProcessor extends StructureProcessor {

	public static final Codec<DeWaterloggingProcessor> CODEC = Codec.unit(DeWaterloggingProcessor::new);

	@Override
	public StructureBlockInfo process(LevelReader level, BlockPos pos, BlockPos pos2, StructureBlockInfo info, StructureBlockInfo info2, StructurePlaceSettings settings, StructureTemplate template) {
		// thanks to TelepathicGrunt for making me aware of this; apparently there is a vanilla bug
		// that makes it so that water sources in both the structure and the world will spread into
		// waterloggable blocks even if `shouldKeepLiquids` is false in the structure settings
		if (info2.state.hasProperty(BlockStateProperties.WATERLOGGED) && !info2.state.getValue(BlockStateProperties.WATERLOGGED)) {

			BlockPos actualPos = info2.pos;

			// technically this can be any non-waterloggable block,
			// i'm just going with clay for the meme here really
			BlockState tempReplacer = Blocks.CLAY.defaultBlockState();

			if (level.isWaterAt(actualPos)) {
				// need to use the chunk because we need to update the state in the world
				// and don't have access to the LevelWriter for level.setBlockState here
				level.getChunk(actualPos).setBlockState(actualPos, tempReplacer, false);
			}

			// remove adjacent water blocks as well
			for (Direction direction : Direction.values()) {
				BlockPos adj = actualPos.relative(direction);
				if (level.isWaterAt(adj)) {
					level.getChunk(adj).setBlockState(adj, tempReplacer, false);
				}
			}
		}

		return info2;
	}

	@Override
	protected StructureProcessorType<?> getType() {
		return FTBStructuresStructures.WATERLOG_B_GONE;
	}
}
