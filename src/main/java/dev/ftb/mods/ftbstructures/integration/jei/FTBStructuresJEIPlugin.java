package dev.ftb.mods.ftbstructures.integration.jei;

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
			r.addRecipes(lr.loot, LootCategory.ID);
		}
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration r) {
		r.addRecipeCategories(new LootCategory(r.getJeiHelpers().getGuiHelper()));
	}
}