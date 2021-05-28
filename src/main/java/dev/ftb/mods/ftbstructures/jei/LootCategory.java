package dev.ftb.mods.ftbstructures.jei;


import dev.ftb.mods.ftbstructures.FTBStructures;
import dev.ftb.mods.ftbstructures.item.FTBStructuresItems;
import dev.ftb.mods.ftbstructures.recipe.LootRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author LatvianModder
 */
public class LootCategory implements IRecipeCategory<LootRecipe> {
	public static final ResourceLocation UID = new ResourceLocation(FTBStructures.MOD_ID + ":loot");

	private final IDrawable background;
	private final IDrawable icon;

	public LootCategory(IGuiHelper guiHelper) {
		background = guiHelper.drawableBuilder(new ResourceLocation(FTBStructures.MOD_ID + ":textures/gui/loot_recipe.png"), 0, 0, 150, 18).setTextureSize(256, 32).build();
		icon = guiHelper.createDrawableIngredient(new ItemStack(FTBStructuresItems.CRATE.get()));
	}

	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Override
	public Class<? extends LootRecipe> getRecipeClass() {
		return LootRecipe.class;
	}

	@Override
	public String getTitle() {
		return I18n.get("block." + FTBStructures.MOD_ID + ".loot");
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public IDrawable getIcon() {
		return icon;
	}

	@Override
	public void setIngredients(LootRecipe recipe, IIngredients ingredients) {
		ingredients.setInputLists(VanillaTypes.ITEM, Collections.singletonList(Arrays.asList(recipe.ingredient.getItems())));
		ingredients.setOutput(VanillaTypes.ITEM, recipe.result);
	}

	@Override
	public void setRecipe(IRecipeLayout layout, LootRecipe recipe, IIngredients ingredients) {
		IGuiItemStackGroup itemStacks = layout.getItemStacks();
		itemStacks.init(0, true, 0, 0);
		itemStacks.init(1, false, 92, 0);
		itemStacks.set(ingredients);
	}

	@Override
	public List<Component> getTooltipStrings(LootRecipe recipe, double mouseX, double mouseY) {
		if (mouseX >= 55D && mouseY >= 3D && mouseX < 73D && mouseY < 30D) {
			return Collections.singletonList(new TextComponent("Weight [WIP]: " + recipe.weight));
		}

		return Collections.emptyList();
	}
}
