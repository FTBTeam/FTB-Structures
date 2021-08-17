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
			Codec.INT.fieldOf("regionRadius").forGetter(o -> o.regionRadius),
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

	public final int regionRadius;
	public final int regionDistance;
	public final Supplier<Biome> islandBiome;
	public final List<BiomeEntry> biomes;
	private final BiomeEntry[] biomeArray;
	private final Supplier<Biome> defaultBiome;

	protected IslandGridBiomeSource(int r, Supplier<Biome> i, List<BiomeEntry> b) {
		super(joinBiomes(i, b));
		regionRadius = r;
		regionDistance = regionRadius * 2 + 1;
		islandBiome = i;
		biomes = new ArrayList<>(b);
		biomes.sort(null);
		biomeArray = biomes.toArray(new BiomeEntry[0]);
		defaultBiome = biomeArray.length == 0 ? islandBiome : biomeArray[0].biome;
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
		int rx = sx >> 7;
		int rz = sz >> 7;

		if (rx >= -regionRadius && rz >= -regionRadius && rx <= regionRadius && rz <= regionRadius) {
			return defaultBiome.get();
		}

		int bx = MathUtils.mod((sx << 2) + regionRadius * 512, regionDistance * 512);
		int bz = MathUtils.mod((sz << 2) + regionRadius * 512, regionDistance * 512);

		int cx = regionRadius * 512 + 256;
		int cz = regionRadius * 512 + 256;

		double distSq = MathUtils.distSq(bx, bz, cx, cz);

		for (BiomeEntry entry : biomeArray) {
			if (distSq >= entry.distanceSq) {
				return entry.biome.get();
			}
		}

		return islandBiome.get();
	}
}
