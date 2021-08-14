package dev.ftb.mods.ftbstructures.jei;

import dev.ftb.mods.ftbstructures.FTBStructures;
import dev.ftb.mods.ftbstructures.FTBStructuresData;
import dev.ftb.mods.ftbstructures.recipe.LootRecipe;
import dev.ftb.mods.ftbstructures.util.FTBStructuresLang;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
@JeiPlugin
public class FTBStructuresJEIPlugin implements IModPlugin {
	public static IJeiRuntime RUNTIME;

	@Override
	public ResourceLocation getPluginUid() {
		return new ResourceLocation(FTBStructures.MOD_ID, "jei");
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime r) {
		RUNTIME = r;
	}

	@Override
	public void registerRecipes(IRecipeRegistration r) {
		RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();
		Recipe<?> recipe = recipeManager.byKey(new ResourceLocation(FTBStructures.MOD_ID, "loot")).orElse(null);

		if (recipe instanceof LootRecipe) {
			LootRecipe lr = (LootRecipe) recipe;
			List<LootWrapper> recipes = new ArrayList<>();

			for (FTBStructuresData.Loot loot : lr.loot) {
				ItemStack in = new ItemStack(loot.item);

				for (FTBStructuresData.WeightedList<ItemStack>.Entry item : loot.items.entries) {
					LootWrapper wrapper = new LootWrapper();
					wrapper.input = in;
					wrapper.output = item.result;

					if (wrapper.output.isEmpty()) {
						wrapper.output = new ItemStack(Items.BARRIER);
						wrapper.output.setHoverName(FTBStructuresLang.JEI_LOOT_NOTHING.withStyle(ChatFormatting.RED));
					}

					wrapper.weight = item.weight;
					wrapper.totalWeight = loot.totalWeight;
					recipes.add(wrapper);
				}
			}

			r.addRecipes(recipes, LootCategory.UID);
		}
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration r) {
		r.addRecipeCategories(new LootCategory(r.getJeiHelpers().getGuiHelper()));
	}
}