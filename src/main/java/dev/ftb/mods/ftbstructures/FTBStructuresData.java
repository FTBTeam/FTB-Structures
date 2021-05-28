package dev.ftb.mods.ftbstructures;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FTBStructuresData {
	public static class OceanStructure {
		public String id = "";
		public int y = -1;
		public boolean oceanFloor = false;
		public int weight = 1;

		private StructureTemplate template;

		public StructureTemplate getTemplate(WorldGenLevel level) {
			if (template == null) {
				template = level.getLevel().getStructureManager().getOrCreate(new ResourceLocation(id));
			}

			return template;
		}
	}

	public static int worldgenChance = 1;
	public static final List<OceanStructure> oceanStructures = new ArrayList<>();

	public static void reset() {
		oceanStructures.clear();
	}

	public static void addOceanStructure(Consumer<OceanStructure> consumer) {
		OceanStructure s = new OceanStructure();
		consumer.accept(s);
		oceanStructures.add(s);
	}
}
