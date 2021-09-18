package dev.ftb.mods.ftbstructures;

import com.tterrag.registrate.Registrate;
import dev.ftb.mods.ftbstructures.block.FTBStructuresBlocks;
import dev.ftb.mods.ftbstructures.client.FTBStructuresClient;
import dev.ftb.mods.ftbstructures.recipe.FTBStructuresRecipeSerializers;
import dev.ftb.mods.ftbstructures.util.FTBStructuresLang;
import dev.ftb.mods.ftbstructures.worldgen.FTBStructuresStructures;
import dev.ftb.mods.ftbstructures.worldgen.IslandGridBiomeSource;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author LatvianModder
 */
@Mod(FTBStructures.MOD_ID)
@Mod.EventBusSubscriber(modid = FTBStructures.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FTBStructures {
	public static final String MOD_ID = "ftbstructures";

	public static FTBStructuresCommon PROXY;

	public static CreativeModeTab group;

	public static final Logger LOGGER = LogManager.getLogger();
	public static final LazyLoadedValue<Registrate> REGISTRATE = new LazyLoadedValue<>(() -> Registrate.create(MOD_ID));

	public FTBStructures() {
		PROXY = DistExecutor.safeRunForDist(() -> FTBStructuresClient::new, () -> FTBStructuresCommon::new);

		FTBStructuresLang.init();
		FTBStructuresBlocks.init();
		FTBStructuresRecipeSerializers.init();
		FTBStructuresStructures.init();

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
		ResourceKey<Biome> key = event.getName() == null ? null : ResourceKey.create(Registry.BIOME_REGISTRY, event.getName());

		if (event.getCategory() == Biome.BiomeCategory.NETHER || key != null && BiomeDictionary.hasType(key, BiomeDictionary.Type.NETHER)) {
			event.getGeneration().addStructureStart(FTBStructuresStructures.NETHER_CONFIGURED_FEATURE);
		} else if (event.getCategory() == Biome.BiomeCategory.THEEND || key != null && BiomeDictionary.hasType(key, BiomeDictionary.Type.END)) {
			event.getGeneration().addStructureStart(FTBStructuresStructures.END_CONFIGURED_FEATURE);
		} else if (event.getCategory() == Biome.BiomeCategory.OCEAN || key != null && BiomeDictionary.hasType(key, BiomeDictionary.Type.OCEAN)) {
			event.getGeneration().addStructureStart(FTBStructuresStructures.OCEAN_CONFIGURED_FEATURE);

		}
	}

	public static Registrate reg() {
		return REGISTRATE.get();
	}
}