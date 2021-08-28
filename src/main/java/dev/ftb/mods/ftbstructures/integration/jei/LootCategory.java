package dev.ftb.mods.ftbstructures.integration.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ftb.mods.ftbstructures.FTBStructures;
import dev.ftb.mods.ftbstructures.FTBStructuresData.Loot;
import dev.ftb.mods.ftbstructures.FTBStructuresData.WeightedSet;
import dev.ftb.mods.ftbstructures.FTBStructuresData.WeightedSet.Entry;
import dev.ftb.mods.ftbstructures.block.FTBStructuresBlocks;
import dev.ftb.mods.ftbstructures.util.FTBStructuresLang;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LootCategory implements IRecipeCategory<Loot> {
	public static final ResourceLocation ID = new ResourceLocation(FTBStructures.MOD_ID, "sluice_jei");
	public static final ResourceLocation BACKGROUND = new ResourceLocation(FTBStructures.MOD_ID, "textures/gui/loot_jei_background.png");

	private final IDrawable background;
	private final IDrawable icon;

	public LootCategory(IGuiHelper guiHelper) {
		background = guiHelper.drawableBuilder(BACKGROUND, 0, 0, 172, 126).setTextureSize(180, 126).build();
		icon = guiHelper.createDrawableIngredient(new ItemStack(FTBStructuresBlocks.CRATE.get()));
	}

	@Override
	public ResourceLocation getUid() {
		return ID;
	}

	@Override
	public Class<? extends Loot> getRecipeClass() {
		return Loot.class;
	}

	@Override
	public String getTitle() {
		return I18n.get(FTBStructuresLang.JEI_LOOT_TITLE.getKey());
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
	public void setIngredients(Loot loot, IIngredients ingr) {
		ingr.setInput(VanillaTypes.ITEM, loot.item.getDefaultInstance());

		List<ItemStack> list = new ArrayList<>();
		for (Entry<ItemStack> e : loot.items.descendingSet()) {
			ItemStack result = e.result;
			if (result.isEmpty()) {
				result = new ItemStack(Items.BARRIER);
				result.setHoverName(FTBStructuresLang.JEI_LOOT_NOTHING.withStyle(ChatFormatting.RED));
			}
			list.add(result);
		}

		ingr.setOutputs(VanillaTypes.ITEM, list);
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, Loot loot, IIngredients ingr) {
		IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();

		itemStacks.init(0, true, 4, 4);

		for (int i = 0; i < loot.items.size(); i++) {
			itemStacks.init(1 + i, false, 27 + (i % 8 * 18), 4 + i / 8 * 24);
		}

		itemStacks.set(ingr);
	}

	// TODO: add rolls hint
	// TODO: add pagination, reduce size to 4x8 / 4x7
	@Override
	public void draw(Loot loot, PoseStack ms, double mouseX, double mouseY) {
		IRecipeCategory.super.draw(loot, ms, mouseX, mouseY);

		WeightedSet<ItemStack> items = loot.items;
		Iterator<Entry<ItemStack>> iterator = items.descendingIterator();
		int row = 0;
		for (int i = 0; i < items.size() && iterator.hasNext(); i++) {
			Entry<ItemStack> item = iterator.next();

			double c = (double) item.weight / (double) items.totalWeight * 100;
			String chance = c < 0.1 ? "< 0.1%" : String.format("%.2f%%", c);

			if (i > 0 && i % 8 == 0) {
				row++;
			}

			ms.pushPose();
			ms.translate(36 + (i % 8 * 18), 23.5f + (row * 24), 100);
			ms.scale(.5F, .5F, 8000F);
			Gui.drawCenteredString(ms, Minecraft.getInstance().font, chance, 0, 0, 0xFFFFFF);
			ms.popPose();
		}
	}
}