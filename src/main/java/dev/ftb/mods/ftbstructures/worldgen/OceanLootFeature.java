package dev.ftb.mods.ftbstructures.worldgen;

import dev.ftb.mods.ftbstructures.FTBStructuresData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;

import java.util.Random;

public class OceanLootFeature extends Feature<NoneFeatureConfiguration> {
	public OceanLootFeature() {
		super(NoneFeatureConfiguration.CODEC);
	}

	@Override
	public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, Random random, BlockPos pos, NoneFeatureConfiguration config) {
		FTBStructuresData.Structure structure = FTBStructuresData.oceanStructures.get(random);

		if (structure == null) {
			return false;
		}

		int x = pos.getX();
		int z = pos.getZ();
		int y = level.getHeight(structure.oceanFloor ? Heightmap.Types.OCEAN_FLOOR_WG : Heightmap.Types.WORLD_SURFACE_WG, x, z);

		StructureTemplate template = structure.getTemplate(level);
		int sx = template.getSize().getX();
		int sz = template.getSize().getZ();

		StructurePlaceSettings settings = new StructurePlaceSettings();
		settings.setRandom(random);
		settings.setRotationPivot(new BlockPos(sx / 2, 0, sz / 2));
		settings.setRotation(Rotation.getRandom(random));
		settings.setMirror(Mirror.values()[random.nextInt(3)]);

		BlockPos templatePosition = new BlockPos(x - sx / 2, y + structure.y, z - sz / 2);
		template.placeInWorld(level, templatePosition, settings, random);

		for (StructureBlockInfo info : template.filterBlocks(templatePosition, settings, Blocks.STRUCTURE_BLOCK)) {
			if (info.nbt != null && StructureMode.valueOf(info.nbt.getString("mode")) == StructureMode.DATA) {
				String id = info.nbt.getString("metadata");
				if (FTBStructuresData.palettes.containsKey(id)) {
					BlockState state = FTBStructuresData.palettes.get(id).getOne(random, Blocks.AIR).defaultBlockState();
					level.setBlock(pos, state, 3);
				}
			}
		}

		return true;
	}
}
