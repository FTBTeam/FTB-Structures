package dev.ftb.mods.ftbstructures.worldgen;

import dev.ftb.mods.ftbstructures.FTBStructures;
import dev.ftb.mods.ftbstructures.FTBStructuresData;
import dev.ftb.mods.ftbstructures.worldgen.processor.DeWaterloggingProcessor;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author LatvianModder
 */
public class FTBStructuresStructures {
	public static final DeferredRegister<StructureFeature<?>> STRUCTURE_FEATURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, FTBStructures.MOD_ID);

	private static <T extends StructureFeature<?>> RegistryObject<T> registerFeature(String name, T structure) {
		return STRUCTURE_FEATURES.register(name, () -> structure);
	}

	private static void addStructure(StructureFeature<NoneFeatureConfiguration> feature) {
		StructureFeature.STRUCTURES_REGISTRY.put(feature.getRegistryName().toString(), feature);
	}

	private static <FC extends FeatureConfiguration, F extends StructureFeature<FC>> ConfiguredStructureFeature<FC, F> registerConfiguredFeature(String name, ConfiguredStructureFeature<FC, F> structureFeature) {
		return BuiltinRegistries.register(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, FTBStructures.MOD_ID + ":" + name, structureFeature);
	}

	private static void registerSettings(StructureFeature<NoneFeatureConfiguration> feature) {
		/*
		StructureFeatureConfiguration config = new StructureFeatureConfiguration(7, 2, feature.getRegistryName().hashCode() & 0xFFFFFF);

		NoiseGeneratorSettings.bootstrap().structureSettings().structureConfig().put(feature, config);

		NoiseGeneratorSettings aSettings = BuiltinRegistries.NOISE_GENERATOR_SETTINGS.get(NoiseGeneratorSettings.AMPLIFIED);

		if (aSettings != null) {
			aSettings.structureSettings().structureConfig().put(feature, config);
		}

		NoiseGeneratorSettings fSettings = BuiltinRegistries.NOISE_GENERATOR_SETTINGS.get(NoiseGeneratorSettings.FLOATING_ISLANDS);

		if (fSettings != null) {
			fSettings.structureSettings().structureConfig().put(feature, config);
		}
		 */
	}

	public static final RegistryObject<StructureFeature<NoneFeatureConfiguration>> OCEAN_FEATURE = registerFeature("ocean", new LootStructure(FTBStructuresData.oceanStructures));
	public static final RegistryObject<StructureFeature<NoneFeatureConfiguration>> NETHER_FEATURE = registerFeature("nether", new LootStructure(FTBStructuresData.netherStructures));
	public static final RegistryObject<StructureFeature<NoneFeatureConfiguration>> END_FEATURE = registerFeature("end", new LootStructure(FTBStructuresData.endStructures));

	public static ConfiguredStructureFeature<NoneFeatureConfiguration, ? extends StructureFeature<NoneFeatureConfiguration>> OCEAN_CONFIGURED_FEATURE;
	public static ConfiguredStructureFeature<NoneFeatureConfiguration, ? extends StructureFeature<NoneFeatureConfiguration>> NETHER_CONFIGURED_FEATURE;
	public static ConfiguredStructureFeature<NoneFeatureConfiguration, ? extends StructureFeature<NoneFeatureConfiguration>> END_CONFIGURED_FEATURE;

	public static final StructurePieceType PIECE_TYPE = Registry.register(Registry.STRUCTURE_PIECE, FTBStructures.MOD_ID + ":piece", FTBStructurePiece::new);
	public static final StructureProcessorType<DeWaterloggingProcessor> WATERLOG_B_GONE = StructureProcessorType.register(FTBStructures.MOD_ID + ":waterlog_b_gone", DeWaterloggingProcessor.CODEC);

	public static void init() {
		FMLJavaModLoadingContext.get().getModEventBus().register(FTBStructuresStructures.class);
		STRUCTURE_FEATURES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

	@SubscribeEvent
	public static void commonSetup(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			addStructure(OCEAN_FEATURE.get());
			addStructure(NETHER_FEATURE.get());
			addStructure(END_FEATURE.get());

			OCEAN_CONFIGURED_FEATURE = registerConfiguredFeature("ocean", OCEAN_FEATURE.get().configured(NoneFeatureConfiguration.INSTANCE));
			NETHER_CONFIGURED_FEATURE = registerConfiguredFeature("nether", NETHER_FEATURE.get().configured(NoneFeatureConfiguration.INSTANCE));
			END_CONFIGURED_FEATURE = registerConfiguredFeature("end", END_FEATURE.get().configured(NoneFeatureConfiguration.INSTANCE));

			registerSettings(OCEAN_FEATURE.get());
			registerSettings(NETHER_FEATURE.get());
			registerSettings(END_FEATURE.get());
		});
	}
}
