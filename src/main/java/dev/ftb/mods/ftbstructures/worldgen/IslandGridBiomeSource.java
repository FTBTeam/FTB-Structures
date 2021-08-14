package dev.ftb.mods.ftbstructures.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.ftb.mods.ftblibrary.math.MathUtils;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class IslandGridBiomeSource extends BiomeSource {
	public static class BiomeEntry implements Comparable<BiomeEntry> {
		public static final Codec<BiomeEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
				Biome.CODEC.fieldOf("biome").forGetter(o -> o.biome),
				Codec.INT.fieldOf("distance").forGetter(o -> o.distance)
		).apply(instance, BiomeEntry::new));

		public final Supplier<Biome> biome;
		public final int distance;
		public final double distanceSq;

		public BiomeEntry(Supplier<Biome> b, int d) {
			biome = b;
			distance = d;
			distanceSq = distance * distance;
		}

		@Override
		public int compareTo(@NotNull BiomeEntry o) {
			return Integer.compare(o.distance, distance);
		}
	}

	public static final Codec<IslandGridBiomeSource> CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codec.INT.fieldOf("regionDistance").forGetter(o -> o.regionDistance),
			Biome.CODEC.fieldOf("islandBiome").forGetter(o -> o.islandBiome),
			BiomeEntry.CODEC.listOf().fieldOf("biomes").forGetter(o -> o.biomes)
	).apply(instance, IslandGridBiomeSource::new));

	private static Stream<Supplier<Biome>> joinBiomes(Supplier<Biome> i, List<BiomeEntry> b) {
		ArrayList<Supplier<Biome>> list = new ArrayList<>(b.size() + 1);
		list.add(i);

		for (BiomeEntry entry : b) {
			list.add(entry.biome);
		}

		return list.stream();
	}

	public final int regionDistance;
	public final Supplier<Biome> islandBiome;
	public final List<BiomeEntry> biomes;

	protected IslandGridBiomeSource(int r, Supplier<Biome> i, List<BiomeEntry> b) {
		super(joinBiomes(i, b));
		regionDistance = r;
		islandBiome = i;
		biomes = new ArrayList<>(b);
		biomes.sort(null);
	}

	@Override
	protected Codec<? extends BiomeSource> codec() {
		return CODEC;
	}

	@Override
	public BiomeSource withSeed(long seed) {
		return this;
	}

	@Override
	public Biome getNoiseBiome(int sx, int sy, int sz) {
		int bx = sx << 2;
		int bz = sz << 2;

		if (((sx >> 7) % regionDistance == 0) && ((sz >> 7) % regionDistance == 0)) {
			double distSq = MathUtils.distSq(bx & 511, bz & 511, 256, 256);

			for (BiomeEntry entry : biomes) {
				if (distSq >= entry.distanceSq) {
					return entry.biome.get();
				}
			}
		}

		return islandBiome.get();
	}
}
