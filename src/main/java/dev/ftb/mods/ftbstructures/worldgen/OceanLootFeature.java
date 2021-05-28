package dev.ftb.mods.ftbstructures.worldgen;

import dev.ftb.mods.ftbstructures.FTBStructuresData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.Random;

public class OceanLootFeature extends Feature<OceanLootConfiguration> {
	public OceanLootFeature() {
		super(OceanLootConfiguration.CODEC);
	}

	@Override
	public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, Random random, BlockPos pos, OceanLootConfiguration config) {
		FTBStructuresData.OceanStructure structure = FTBStructuresData.getOceanStructure(random);

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

		template.placeInWorld(level, new BlockPos(x - sx / 2, y + structure.y, z - sz / 2), settings, random);
		return true;
	}
}
