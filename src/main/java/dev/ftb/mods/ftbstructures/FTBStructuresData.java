package dev.ftb.mods.ftbstructures;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

public class FTBStructuresData {
	public static class Structure {
		public String id = "";
		public int y = -1;
		public boolean oceanFloor = false;
		public int weight = 1;
		public int minY = 70;
		public int maxY = 100;

		private StructureTemplate template;

		public StructureTemplate getTemplate(WorldGenLevel level) {
			if (template == null) {
				template = level.getLevel().getStructureManager().getOrCreate(new ResourceLocation(id));
			}

			return template;
		}
	}

	public static class StructureGroup {
		private int totalWeight = 0;
		private final List<Structure> structures = new ArrayList<>();

		public void add(Consumer<Structure> consumer) {
			Structure s = new Structure();
			consumer.accept(s);
			totalWeight += s.weight;
			structures.add(s);
		}

		@Nullable
		public Structure get(Random random) {
			if (structures.isEmpty()) {
				return null;
			}

			int number = random.nextInt(totalWeight) + 1;
			int currentWeight = 0;

			for (Structure structure : structures) {
				if (structure.weight > 0) {
					currentWeight += structure.weight;

					if (currentWeight >= number) {
						return structure;
					}
				}
			}

			return structures.get(random.nextInt(structures.size()));
		}
	}

	public static class WeightedItem {
		public ItemStack item;
		public int weight;
	}

	public static class Loot {
		public Item item = Items.AIR;
		public int minRolls = 1;
		public int maxRolls = 1;
		public final List<WeightedItem> items = new ArrayList<>();
		public int totalWeight = 0;

		public void add(ItemStack item, int weight) {
			WeightedItem i = new WeightedItem();
			i.item = item;
			i.weight = Math.max(0, weight);
			totalWeight += i.weight;
			items.add(i);
		}

		public ItemStack randomItem(Random random) {
			if (totalWeight <= 0) {
				return ItemStack.EMPTY;
			}

			int number = random.nextInt(totalWeight) + 1;
			int currentWeight = 0;

			for (WeightedItem item : items) {
				if (item.weight > 0) {
					currentWeight += item.weight;

					if (currentWeight >= number) {
						return item.item;
					}
				}
			}

			return ItemStack.EMPTY;
		}
	}

	public static int oceanWorldgenChance = 1;
	public static int netherWorldgenChance = 1;
	public static int endWorldgenChance = 1;

	public static StructureGroup oceanStructures = new StructureGroup();
	public static StructureGroup netherStructures = new StructureGroup();
	public static StructureGroup endStructures = new StructureGroup();
	private static final Map<Item, Loot> lootMap = new HashMap<>();

	public static void reset() {
		oceanStructures = new StructureGroup();
		netherStructures = new StructureGroup();
		endStructures = new StructureGroup();
		lootMap.clear();
	}

	public static void setLoot(Item item, Consumer<Loot> consumer) {
		Loot loot = new Loot();
		loot.item = item;
		consumer.accept(loot);
		lootMap.put(item, loot);
	}

	public static List<ItemStack> getItems(Item item, Random random) {
		Loot loot = lootMap.get(item);

		if (loot != null) {
			List<ItemStack> stacks = new ArrayList<>();
			int rolls = loot.minRolls + random.nextInt(loot.maxRolls - loot.minRolls + 1);

			for (int r = 0; r < rolls; r++) {
				ItemStack stack = loot.randomItem(random);

				if (!stack.isEmpty()) {
					stacks.add(stack.copy());
				}
			}

			for (WeightedItem is : loot.items) {
				if (!is.item.isEmpty()) {
					if (is.weight == 0) {
						stacks.add(is.item.copy());
					}
				}
			}

			return stacks;
		}

		return Collections.emptyList();
	}
}
