package dev.ftb.mods.ftbstructures.worldgen;

import dev.ftb.mods.ftbstructures.FTBStructuresData;
import dev.ftb.mods.ftbstructures.util.StructureUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.material.Fluids;

import java.util.Random;

public class NetherLootFeature extends Feature<NoneFeatureConfiguration> {
	public NetherLootFeature() {
		super(NoneFeatureConfiguration.CODEC);
	}

	@Override
	public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, Random random, BlockPos pos, NoneFeatureConfiguration config) {
		FTBStructuresData.Structure structure = FTBStructuresData.netherStructures.get(random);

		if (structure == null) {
			return false;
		}

		int x = pos.getX();
		int z = pos.getZ();
		int y = FTBStructuresData.netherLavaLevel;

		if (level.getFluidState(new BlockPos(x, y - 1, z)).getType().isSame(Fluids.LAVA)) {
			return false;
		}

		return StructureUtil.placeWithPlaceholders(level, structure, new BlockPos(x, y, z), random);
	}
}
