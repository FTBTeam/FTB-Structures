package dev.ftb.mods.ftbstructures;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import dev.ftb.mods.ftbstructures.block.FTBStructuresBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeLootTableProvider;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(modid = FTBStructures.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FTBStructuresDataGenHandler {
	public static final String MODID = FTBStructures.MOD_ID;
	//private static Gson GSON;

	@SubscribeEvent
	public static void dataGenEvent(GatherDataEvent event) {
		/*
		GSON = new GsonBuilder()
				.registerTypeAdapter(Variant.class, new Variant.Deserializer())
				.registerTypeAdapter(ItemCameraTransforms.class, new ItemCameraTransforms.Deserializer())
				.registerTypeAdapter(ItemTransformVec3f.class, new ItemTransformVec3f.Deserializer())
				.create();
		 */

		DataGenerator gen = event.getGenerator();
		ExistingFileHelper efh = event.getExistingFileHelper();

		if (event.includeClient()) {
			gen.addProvider(new JMLang(gen, MODID, "en_us"));
			gen.addProvider(new JMBlockStates(gen, MODID, event.getExistingFileHelper()));
			gen.addProvider(new JMBlockModels(gen, MODID, event.getExistingFileHelper()));
			gen.addProvider(new JMItemModels(gen, MODID, event.getExistingFileHelper()));
		}

		if (event.includeServer()) {
			JMBlockTags blockTags = new JMBlockTags(gen, MODID, efh);
			gen.addProvider(blockTags);
			gen.addProvider(new JMItemTags(gen, blockTags, MODID, efh));
			gen.addProvider(new JMRecipes(gen));
			gen.addProvider(new JMLootTableProvider(gen));
		}
	}

	private static class JMLang extends LanguageProvider {
		public JMLang(DataGenerator gen, String modid, String locale) {
			super(gen, modid, locale);
		}

		@Override
		protected void addTranslations() {
			add("itemGroup.ftbstructures", "FTB Structures");
			add("jei." + FTBStructures.MOD_ID + ".loot", "Loot");
			addBlock(FTBStructuresBlocks.SMALL_CRATE, "Small Crate");
			addBlock(FTBStructuresBlocks.CRATE, "Crate");
			addBlock(FTBStructuresBlocks.WHITE_BARREL, "White Barrel");
			addBlock(FTBStructuresBlocks.RED_BARREL, "Red Barrel");
			addBlock(FTBStructuresBlocks.BLUE_BARREL, "Blue Barrel");
		}
	}

	private static class JMBlockStates extends BlockStateProvider {
		public JMBlockStates(DataGenerator gen, String modid, ExistingFileHelper exFileHelper) {
			super(gen, modid, exFileHelper);
		}

		@Override
		protected void registerStatesAndModels() {
			ModelFile.ExistingModelFile smallCrateMode = models().getExistingFile(modLoc("block/small_crate"));
			getVariantBuilder(FTBStructuresBlocks.SMALL_CRATE.get()).forAllStatesExcept(state -> ConfiguredModel.builder().modelFile(smallCrateMode).rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360).build(), BlockStateProperties.WATERLOGGED);

			simpleBlock(FTBStructuresBlocks.CRATE.get(), models().getExistingFile(modLoc("block/crate")));
			rotatedBarrel(FTBStructuresBlocks.WHITE_BARREL.get(), models().getExistingFile(modLoc("block/white_barrel")));
			rotatedBarrel(FTBStructuresBlocks.RED_BARREL.get(), models().getExistingFile(modLoc("block/red_barrel")));
			rotatedBarrel(FTBStructuresBlocks.BLUE_BARREL.get(), models().getExistingFile(modLoc("block/blue_barrel")));
		}

		private void rotatedBarrel(Block block, ModelFile model) {
			simpleBlock(block, ConfiguredModel.allYRotations(model, 0, false));
		}
	}

	private static class JMBlockModels extends BlockModelProvider {
		public JMBlockModels(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
			super(generator, modid, existingFileHelper);
		}

		@Override
		protected void registerModels() {
		}
	}

	private static class JMItemModels extends ItemModelProvider {
		public JMItemModels(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
			super(generator, modid, existingFileHelper);
		}

		@Override
		protected void registerModels() {
			// singleTexture("tempered_glass", mcLoc("item/generated"), "layer0", modLoc("item/tempered_glass"));

			withExistingParent("small_crate", modLoc("block/small_crate"));
			withExistingParent("crate", modLoc("block/crate"));
			withExistingParent("white_barrel", modLoc("block/white_barrel"));
			withExistingParent("red_barrel", modLoc("block/red_barrel"));
			withExistingParent("blue_barrel", modLoc("block/blue_barrel"));
		}
	}

	private static class JMBlockTags extends BlockTagsProvider {
		public JMBlockTags(DataGenerator generatorIn, String modId, ExistingFileHelper existingFileHelper) {
			super(generatorIn, modId, existingFileHelper);
		}

		@Override
		protected void addTags() {
		}
	}

	private static class JMItemTags extends ItemTagsProvider {
		public JMItemTags(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, String modId, ExistingFileHelper existingFileHelper) {
			super(dataGenerator, blockTagProvider, modId, existingFileHelper);
		}

		@Override
		protected void addTags() {
		}
	}

	private static class JMRecipes extends RecipeProvider {
		// public final Tag<Item> IRON_INGOT = ItemTags.bind("forge:ingots/iron");

		public JMRecipes(DataGenerator generatorIn) {
			super(generatorIn);
		}

		@Override
		protected void buildShapelessRecipes(Consumer<FinishedRecipe> consumer) {
			/*
			ShapedRecipeBuilder.shaped(FTBStructuresItems.ITEM_NAME.get())
					.unlockedBy("has_item", has(IRON_INGOT))
					.group(MODID + ":group_name")
					.pattern("III")
					.pattern("III")
					.pattern("III")
					.define('I', IRON_INGOT)
					.save(consumer);
			 */
		}
	}

	private static class JMLootTableProvider extends ForgeLootTableProvider {
		private final List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> lootTables = Lists.newArrayList(Pair.of(JMBlockLootTableProvider::new, LootContextParamSets.BLOCK));

		public JMLootTableProvider(DataGenerator dataGeneratorIn) {
			super(dataGeneratorIn);
		}

		@Override
		protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
			return lootTables;
		}
	}

	public static class JMBlockLootTableProvider extends BlockLoot {
		private final Map<ResourceLocation, LootTable.Builder> tables = Maps.newHashMap();

		@Override
		protected void addTables() {
			// dropSelf(FTBStructuresBlocks.BLOCK_NAME.get());
		}

		@Override
		public void accept(BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
			addTables();

			for (ResourceLocation rs : new ArrayList<>(tables.keySet())) {
				if (rs != BuiltInLootTables.EMPTY) {
					LootTable.Builder builder = tables.remove(rs);

					if (builder == null) {
						throw new IllegalStateException(String.format("Missing loottable '%s'", rs));
					}

					consumer.accept(rs, builder);
				}
			}

			if (!tables.isEmpty()) {
				throw new IllegalStateException("Created block loot tables for non-blocks: " + tables.keySet());
			}
		}

		@Override
		protected void add(Block blockIn, LootTable.Builder table) {
			tables.put(blockIn.getLootTable(), table);
		}
	}
}
