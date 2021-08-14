package dev.ftb.mods.ftbstructures;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
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

	public static class WeightedList<U> {
		public final List<Entry> entries = new ArrayList<>();
		public int totalWeight = 0;

		public WeightedList<U> add(U object, int i) {
			this.entries.add(new Entry(object, i));
			totalWeight += i;
			return this;
		}

		public class Entry {
			public U result;
			public int weight;

			public Entry(U result, int weight) {
				this.result = result;
				this.weight = weight;
			}
		}

		public U getOne(Random random, U def) {
			if (totalWeight <= 0) {
				return def;
			}

			int number = random.nextInt(totalWeight) + 1;
			int currentWeight = 0;

			for (Entry entry : entries) {
				if (entry.weight > 0) {
					currentWeight += entry.weight;

					if (currentWeight >= number) {
						return entry.result;
					}
				}
			}

			return def;
		}
	}

	public static class Loot {
		public Item item = Items.AIR;
		public int minRolls = 1;
		public int maxRolls = 1;
		public final WeightedList<ItemStack> items = new WeightedList<>();
		public int totalWeight = 0;

		public Loot() {
		}

		public Loot(FriendlyByteBuf buffer) {
			item = Item.byId(buffer.readVarInt());
			minRolls = buffer.readVarInt();
			maxRolls = buffer.readVarInt();

			int s = buffer.readVarInt();

			for (int i = 0; i < s; i++) {
				add(buffer.readItem(), buffer.readVarInt());
			}
		}

		public void add(ItemStack item, int weight) {
			items.add(item, weight);
		}

		public ItemStack randomItem(Random random) {
			return items.getOne(random, ItemStack.EMPTY);
		}

		public void write(FriendlyByteBuf buffer) {
			buffer.writeVarInt(Item.getId(item));
			buffer.writeVarInt(minRolls);
			buffer.writeVarInt(maxRolls);
			buffer.writeVarInt(items.entries.size());

			for (WeightedList<ItemStack>.Entry entry : items.entries) {
				buffer.writeItem(entry.result);
				buffer.writeVarInt(entry.weight);
			}
		}
	}

	public static int oceanWorldgenChance = 1;
	public static int netherWorldgenChance = 1;
	public static int endWorldgenChance = 1;
	public static int netherLavaLevel = 32;

	public static StructureGroup oceanStructures = new StructureGroup();
	public static StructureGroup netherStructures = new StructureGroup();
	public static StructureGroup endStructures = new StructureGroup();
	public static final Map<Item, Loot> lootMap = new LinkedHashMap<>();
	public static final Map<String, WeightedList<Block>> palettes = new LinkedHashMap<>();

	public static void reset() {
		oceanStructures = new StructureGroup();
		netherStructures = new StructureGroup();
		endStructures = new StructureGroup();
		lootMap.clear();
		palettes.clear();
	}

	public static void setLoot(Item item, Consumer<Loot> consumer) {
		Loot loot = new Loot();
		loot.item = item;
		consumer.accept(loot);
		lootMap.put(item, loot);
	}

	public static void addPalette(String name, Consumer<WeightedList<Block>> consumer) {
		WeightedList<Block> blocks = new WeightedList<>();
		consumer.accept(blocks);
		palettes.put(name, blocks);
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

			for (WeightedList<ItemStack>.Entry is : loot.items.entries) {
				if (!is.result.isEmpty()) {
					if (is.weight == 0) {
						stacks.add(is.result.copy());
					}
				}
			}

			return stacks;
		}

		return Collections.emptyList();
	}
}
