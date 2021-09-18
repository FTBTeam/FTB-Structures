package dev.ftb.mods.ftbstructures.worldgen;

import com.google.common.collect.ImmutableList;
import dev.ftb.mods.ftbstructures.FTBStructures;
import dev.ftb.mods.ftbstructures.FTBStructuresData;
import dev.ftb.mods.ftbstructures.worldgen.processor.DeWaterloggingProcessor;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.Random;

public class FTBStructurePiece extends TemplateStructurePiece {
	public static final BlockIgnoreProcessor IGNORE_PROCESSOR = new BlockIgnoreProcessor(ImmutableList.of(Blocks.STRUCTURE_VOID, Blocks.STRUCTURE_BLOCK));

	private final FTBStructuresData.Structure structure;
	private final Rotation rotation;

	public FTBStructurePiece(StructureManager manager, FTBStructuresData.Structure s, BlockPos pos, Rotation r) {
		super(FTBStructuresStructures.PIECE_TYPE, 0);
		structure = s;
		templatePosition = pos;
		rotation = r;
		loadTemplate(manager);
	}

	public FTBStructurePiece(StructureManager manager, CompoundTag tag) {
		super(FTBStructuresStructures.PIECE_TYPE, tag);
		structure = new FTBStructuresData.Structure(tag);
		rotation = Rotation.valueOf(tag.getString("Rot"));
		loadTemplate(manager);
	}

	private void loadTemplate(StructureManager arg) {
		StructureTemplate t = arg.getOrCreate(structure.id);
		StructurePlaceSettings settings = new StructurePlaceSettings();
		settings.setIgnoreEntities(true);
		settings.addProcessor(IGNORE_PROCESSOR);
		settings.setRotationPivot(new BlockPos(t.getSize().getX() / 2, 0, t.getSize().getZ() / 2));
		settings.setRotation(rotation);

		if (structure.oceanFloor) {
			settings.addProcessor(new DeWaterloggingProcessor());
		}

		setup(t, templatePosition, settings);
	}

	@Override
	protected void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		structure.write(tag);
		tag.putString("Rot", rotation.name());
	}

	@Override
	protected void handleDataMarker(String id, BlockPos pos, ServerLevelAccessor level, Random random, BoundingBox box) {
		if (FTBStructuresData.palettes.containsKey(id)) {
			BlockState state = FTBStructuresData.palettes.get(id).getOne(random, Blocks.AIR.defaultBlockState());

			if (state != Blocks.AIR.defaultBlockState()) {
				level.setBlock(pos, state, 2);
			}
		} else {
			level.setBlock(pos, Blocks.STONE.defaultBlockState(), 2);
			FTBStructures.LOGGER.warn("Unknown palette ID '" + id + "' in '" + structure.id + "' at " + pos);
		}
	}

	@Override
	public boolean postProcess(WorldGenLevel level, StructureFeatureManager manager, ChunkGenerator chunkGenerator, Random random, BoundingBox box, ChunkPos chunkPos, BlockPos pos) {
		int y = structure.group.getY(level, templatePosition.getX(), templatePosition.getZ(), random, structure);

		if (y == Integer.MIN_VALUE) {
			return false;
		}

		templatePosition = new BlockPos(templatePosition.getX(), y, templatePosition.getZ());
		return super.postProcess(level, manager, chunkGenerator, random, box, chunkPos, pos);
	}
}
