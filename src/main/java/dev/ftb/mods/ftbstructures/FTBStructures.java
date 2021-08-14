package dev.ftb.mods.ftbstructures;

import com.tterrag.registrate.Registrate;
import dev.ftb.mods.ftbstructures.block.FTBStructuresBlocks;
import dev.ftb.mods.ftbstructures.client.FTBStructuresClient;
import dev.ftb.mods.ftbstructures.recipe.FTBStructuresRecipeSerializers;
import dev.ftb.mods.ftbstructures.util.FTBStructuresLang;
import dev.ftb.mods.ftbstructures.worldgen.EndLootFeature;
import dev.ftb.mods.ftbstructures.worldgen.IslandGridBiomeSource;
import dev.ftb.mods.ftbstructures.worldgen.NetherLootFeature;
import dev.ftb.mods.ftbstructures.worldgen.OceanLootFeature;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;

/**
 * @author LatvianModder
 */
@Mod(FTBStructures.MOD_ID)
@Mod.EventBusSubscriber(modid = FTBStructures.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FTBStructures {
	public static final String MOD_ID = "ftbstructures";

	public static FTBStructuresCommon PROXY;

	public static CreativeModeTab group;

	public static final LazyLoadedValue<Registrate> REGISTRATE = new LazyLoadedValue<>(() -> Registrate.create(MOD_ID));

	public FTBStructures() {
		PROXY = DistExecutor.safeRunForDist(() -> FTBStructuresClient::new, () -> FTBStructuresCommon::new);

		FTBStructuresLang.init();
		FTBStructuresBlocks.init();
		FTBStructuresRecipeSerializers.init();

		group = new CreativeModeTab(MOD_ID) {
			@Override
			@OnlyIn(Dist.CLIENT)
			public ItemStack makeIcon() {
				return new ItemStack(FTBStructuresBlocks.RED_BARREL.get());
			}
		};

		PROXY.init();

		MinecraftForge.EVENT_BUS.addListener(this::worldgen);

		Registry.register(Registry.BIOME_SOURCE, new ResourceLocation("ftbstructures:island_grid"), IslandGridBiomeSource.CODEC);
	}

	private void worldgen(BiomeLoadingEvent event) {
		if (event.getCategory() == Biome.BiomeCategory.OCEAN) {
			event.getGeneration().addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, new OceanLootFeature()
					.configured(NoneFeatureConfiguration.INSTANCE)
					.chance(FTBStructuresData.oceanWorldgenChance)
					.squared()
			);
		} else if (event.getCategory() == Biome.BiomeCategory.NETHER) {
			event.getGeneration().addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, new NetherLootFeature()
					.configured(NoneFeatureConfiguration.INSTANCE)
					.chance(FTBStructuresData.netherWorldgenChance)
					.squared()
			);
		} else if (event.getCategory() == Biome.BiomeCategory.THEEND) {
			event.getGeneration().addFeature(GenerationStep.Decoration.TOP_LAYER_MODIFICATION, new EndLootFeature()
					.configured(NoneFeatureConfiguration.INSTANCE)
					.chance(FTBStructuresData.endWorldgenChance)
					.squared()
			);
		}
	}

	public static Registrate reg() {
		return REGISTRATE.get();
	}
}