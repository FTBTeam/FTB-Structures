package dev.ftb.mods.ftbstructures;

import com.mojang.brigadier.StringReader;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.TreeSet;
import java.util.function.Consumer;

public class FTBStructuresData {
	public static class Structure {
		public final StructureGroup group;
		public ResourceLocation id = new ResourceLocation("minecraft", "unknown");
		public int y = -1;
		public boolean oceanFloor = false;
		public int weight = 1;
		public int minY = 70;
		public int maxY = 100;

		public Structure(StructureGroup g) {
			group = g;
		}

		public Structure(CompoundTag tag) {
			int tp = tag.getByte("SGType");
			group = tp == 0 ? FTBStructuresData.oceanStructures : tp == 1 ? FTBStructuresData.netherStructures : FTBStructuresData.endStructures;
			id = new ResourceLocation(tag.getString("Template"));

			if (tag.contains("SY")) {
				y = tag.getInt("SY");
			}

			oceanFloor = tag.getBoolean("OceanFloor");

			if (tag.contains("SMinY")) {
				minY = tag.getInt("SminY");
			}

			if (tag.contains("SMaxY")) {
				maxY = tag.getInt("SMaxY");
			}
		}

		public void write(CompoundTag tag) {
			tag.putByte("SGType", (byte) group.index);
			tag.putString("Template", id.toString());

			if (y != -1) {
				tag.putInt("SY", y);
			}

			if (oceanFloor) {
				tag.putBoolean("OceanFloor", true);
			}

			if (minY != 70) {
				tag.putInt("SMinY", minY);
			}

			if (maxY != 100) {
				tag.putInt("SMaxY", maxY);
			}
		}
	}

	public static abstract class StructureGroup {
		public final int index;
		private int totalWeight;
		private final List<Structure> structures;

		public StructureGroup(int i) {
			index = i;
			totalWeight = 0;
			structures = new ArrayList<>();
		}

		public void add(Consumer<Structure> consumer) {
			Structure s = new Structure(this);
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

		public void reset() {
			totalWeight = 0;
			structures.clear();
		}

		public abstract int getY(WorldGenLevel level, int x, int z, Random random, Structure structure);
	}

	public static class WeightedSet<U> extends TreeSet<WeightedSet.Entry<U>> {
		public int totalWeight = 0;

		public WeightedSet<U> add(U object, int i) {
			add(new Entry<>(object, i));
			totalWeight += i;
			return this;
		}

		public static class Entry<U> implements Comparable<Entry<U>> {
			public U result;
			public int weight;

			public Entry(U result, int weight) {
				this.result = result;
				this.weight = weight;
			}

			@Override
			public int compareTo(Entry<U> o) {
				int w = weight - o.weight;
				return w == 0 ? Integer.compare(result.hashCode(), o.result.hashCode()) : w;
			}
		}

		public U getOne(Random random, U def) {
			if (totalWeight <= 0) {
				return def;
			}

			int number = random.nextInt(totalWeight) + 1;
			int currentWeight = 0;

			for (Entry<U> entry : this) {
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
		public final WeightedSet<ItemStack> items = new WeightedSet<>();

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
			buffer.writeVarInt(items.size());

			for (WeightedSet.Entry<ItemStack> entry : items) {
				buffer.writeItem(entry.result);
				buffer.writeVarInt(entry.weight);
			}
		}
	}

	public static int netherLavaLevel = 32;

	public static final StructureGroup oceanStructures = new StructureGroup(0) {
		@Override
		public int getY(WorldGenLevel level, int x, int z, Random random, Structure structure) {
			return level.getHeight(structure.oceanFloor ? Heightmap.Types.OCEAN_FLOOR_WG : Heightmap.Types.WORLD_SURFACE_WG, x, z) + structure.y;
		}
	};

	public static final StructureGroup netherStructures = new StructureGroup(1) {
		@Override
		public int getY(WorldGenLevel level, int x, int z, Random random, Structure structure) {
			return FTBStructuresData.netherLavaLevel + structure.y;
		}
	};

	public static final StructureGroup endStructures = new StructureGroup(2) {
		@Override
		public int getY(WorldGenLevel level, int x, int z, Random random, Structure structure) {
			return structure.minY + random.nextInt(structure.maxY - structure.minY + 1);
		}
	};

	public static final Map<Item, Loot> lootMap = new LinkedHashMap<>();
	public static final Map<String, WeightedSet<BlockState>> palettes = new LinkedHashMap<>();

	public static void reset() {
		oceanStructures.reset();
		netherStructures.reset();
		endStructures.reset();
		lootMap.clear();
		palettes.clear();
	}

	public static void setLoot(Item item, Consumer<Loot> consumer) {
		Loot loot = new Loot();
		loot.item = item;
		consumer.accept(loot);
		lootMap.put(item, loot);
	}

	public static void addPalette(String name, Consumer<WeightedSet<String>> consumer) {
		WeightedSet<String> blocks = new WeightedSet<>();
		consumer.accept(blocks);
		WeightedSet<BlockState> states = new WeightedSet<>();

		for (WeightedSet.Entry<String> entry : blocks) {
			try {
				BlockStateParser parser = new BlockStateParser(new StringReader(entry.result), false);
				parser.parse(false);
				states.add(Objects.requireNonNull(parser.getState()), entry.weight);
			} catch (Exception ex) {
				throw new RuntimeException("Invalid state: " + entry.result);
			}
		}

		palettes.put(name, states);
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

			for (WeightedSet.Entry<ItemStack> is : loot.items) {
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
