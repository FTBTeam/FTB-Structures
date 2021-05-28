package dev.ftb.mods.ftbstructures.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class OceanLootConfiguration implements FeatureConfiguration {
	public static final Codec<OceanLootConfiguration> CODEC = RecordCodecBuilder.create(i -> i.group(BlockState.CODEC.fieldOf("barrel").forGetter(c -> c.barrel)).apply(i, OceanLootConfiguration::new));

	public final BlockState barrel;

	public OceanLootConfiguration(BlockState s) {
		barrel = s;
	}
}
