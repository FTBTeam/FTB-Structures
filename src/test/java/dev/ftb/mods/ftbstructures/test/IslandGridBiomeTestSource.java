package dev.ftb.mods.ftbstructures.test;

import dev.ftb.mods.ftblibrary.math.MathUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IslandGridBiomeTestSource {
	public final int regionRadius;
	public final int regionDistance;
	public final int islandBiome;
	public final List<BiomeTestEntry> biomes;
	private final BiomeTestEntry[] biomeArray;
	private final int defaultBiome;

	public IslandGridBiomeTestSource(int r, int island, BiomeTestEntry... e) {
		regionRadius = Math.max(r, 0);
		regionDistance = regionRadius * 2 + 1;
		islandBiome = island;
		biomes = new ArrayList<>(Arrays.asList(e));
		biomes.sort(null);
		biomeArray = biomes.toArray(new BiomeTestEntry[0]);
		defaultBiome = biomeArray.length == 0 ? islandBiome : biomeArray[0].biome;
	}

	public int getNoiseBiome(int sx, int sz) {
		int rx = sx >> 7;
		int rz = sz >> 7;

		if (rx >= -regionRadius && rz >= -regionRadius && rx <= regionRadius && rz <= regionRadius) {
			return defaultBiome;
		}

		int bx = MathUtils.mod((sx << 2) + regionRadius * 512, regionDistance * 512);
		int bz = MathUtils.mod((sz << 2) + regionRadius * 512, regionDistance * 512);

		int cx = regionRadius * 512 + 256;
		int cz = regionRadius * 512 + 256;

		double distSq = MathUtils.distSq(bx, bz, cx, cz);

		for (BiomeTestEntry entry : biomeArray) {
			if (distSq >= entry.distanceSq) {
				return entry.biome;
			}
		}

		return islandBiome;
	}
}
