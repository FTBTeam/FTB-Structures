package dev.ftb.mods.ftbstructures.test;

public class BiomeTestEntry implements Comparable<BiomeTestEntry> {
	public final int biome;
	public final double distance;
	public final double distanceSq;

	public BiomeTestEntry(int b, double d) {
		biome = b;
		distance = d;
		distanceSq = distance * distance;
	}

	@Override
	public int compareTo(BiomeTestEntry o) {
		return Double.compare(o.distance, distance);
	}
}
