package dev.ftb.mods.ftbstructures.worldgen;

import dev.ftb.mods.ftbstructures.FTBStructuresData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

public class LootStructure extends StructureFeature<NoneFeatureConfiguration> {
	public final FTBStructuresData.StructureGroup group;

	public LootStructure(FTBStructuresData.StructureGroup g) {
		super(NoneFeatureConfiguration.CODEC);
		group = g;
	}

	@Override
	public StructureStartFactory<NoneFeatureConfiguration> getStartFactory() {
		return FeatureStart::new;
	}

	@Override
	public String getFeatureName() {
		return getRegistryName().toString();
	}

	@Override
	public GenerationStep.Decoration step() {
		return GenerationStep.Decoration.SURFACE_STRUCTURES;
	}

	public static class FeatureStart extends StructureStart<NoneFeatureConfiguration> {
		public FeatureStart(StructureFeature<NoneFeatureConfiguration> feature, int chunkX, int chunkZ, BoundingBox box, int references, long seed) {
			super(feature, chunkX, chunkZ, box, references, seed);
		}

		@Override
		public void generatePieces(RegistryAccess registryAccess, ChunkGenerator chunkGenerator, StructureManager structureManager, int chunkX, int chunkZ, Biome biome, NoneFeatureConfiguration conig) {
			FTBStructuresData.StructureGroup group = ((LootStructure) getFeature()).group;
			FTBStructuresData.Structure structure = group.get(random);

			if (structure == null) {
				return;
			}

			Rotation rotation = Rotation.getRandom(random);
			pieces.add(new FTBStructurePiece(structureManager, structure, new BlockPos(chunkX * 16, 90, chunkZ * 16), rotation));
			calculateBoundingBox();
		}
	}
}
