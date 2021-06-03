package dev.ftb.mods.ftbstructures.jei;


import dev.ftb.mods.ftblibrary.util.StringUtils;
import dev.ftb.mods.ftbstructures.FTBStructures;
import dev.ftb.mods.ftbstructures.item.FTBStructuresItems;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.List;

/**
 * @author LatvianModder
 */
public class LootCategory implements IRecipeCategory<LootWrapper> {
	public static final ResourceLocation UID = new ResourceLocation(FTBStructures.MOD_ID + ":loot");

	private final IDrawable background;
	private final IDrawable icon;

	public LootCategory(IGuiHelper guiHelper) {
		background = guiHelper.drawableBuilder(new ResourceLocation(FTBStructures.MOD_ID + ":textures/gui/loot_jei.png"), 0, 0, 71, 30).setTextureSize(128, 64).build();
		icon = guiHelper.createDrawableIngredient(new ItemStack(FTBStructuresItems.CRATE.get()));
	}

	@Override
	public ResourceLocation getUid() {
		return UID;
	}

	@Override
	public Class<? extends LootWrapper> getRecipeClass() {
		return LootWrapper.class;
	}

	@Override
	public String getTitle() {
		return I18n.get("jei." + FTBStructures.MOD_ID + ".loot");
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
	public void setIngredients(LootWrapper recipe, IIngredients ingredients) {
		ingredients.setInput(VanillaTypes.ITEM, recipe.input);
		ingredients.setOutput(VanillaTypes.ITEM, recipe.output);
	}

	@Override
	public void setRecipe(IRecipeLayout layout, LootWrapper recipe, IIngredients ingredients) {
		IGuiItemStackGroup itemStacks = layout.getItemStacks();
		itemStacks.init(0, true, 2, 6);
		itemStacks.init(1, false, 47, 6);
		itemStacks.set(ingredients);
	}

	@Override
	public List<Component> getTooltipStrings(LootWrapper recipe, double mouseX, double mouseY) {
		if (mouseX >= 23D && mouseY >= 7D && mouseX < 39D && mouseY < 22D) {
			if (Screen.hasShiftDown()) {
				return Collections.singletonList(new TextComponent("Weight: " + recipe.weight).withStyle(ChatFormatting.GRAY));
			} else {
				return Collections.singletonList(new TextComponent("Chance: " + StringUtils.formatDouble00(recipe.weight * 100D / (double) recipe.totalWeight) + "%").withStyle(ChatFormatting.GRAY));
			}
		}

		return Collections.emptyList();
	}
}
