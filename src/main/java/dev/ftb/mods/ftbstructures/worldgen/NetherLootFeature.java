package dev.ftb.mods.ftbstructures.worldgen;

import dev.ftb.mods.ftbstructures.FTBStructuresData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
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

		StructureTemplate template = structure.getTemplate(level);
		int sx = template.getSize().getX();
		int sz = template.getSize().getZ();

		StructurePlaceSettings settings = new StructurePlaceSettings();
		settings.setRandom(random);
		settings.setRotationPivot(new BlockPos(sx / 2, 0, sz / 2));
		settings.setRotation(Rotation.getRandom(random));
		settings.setMirror(Mirror.values()[random.nextInt(3)]);

		template.placeInWorld(level, new BlockPos(x - sx / 2, y + structure.y, z - sz / 2), settings, random);
		return true;
	}
}
