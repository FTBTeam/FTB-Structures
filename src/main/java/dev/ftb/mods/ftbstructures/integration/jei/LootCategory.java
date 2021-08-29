package dev.ftb.mods.ftbstructures.integration.jei;

import com.google.common.collect.Iterators;
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
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class LootCategory implements IRecipeCategory<Loot> {
	public static final ResourceLocation ID = new ResourceLocation(FTBStructures.MOD_ID, "sluice_jei");
	public static final ResourceLocation BACKGROUND = new ResourceLocation(FTBStructures.MOD_ID, "textures/gui/loot_jei_background.png");

	private final Map<Loot, WrappedLoot> wrappers = new WeakHashMap<>();

	private final IDrawable background;
	private final IDrawable icon;
	private final IDrawable up, down, up2, down2;

	public LootCategory(IGuiHelper guiHelper) {
		background = guiHelper.drawableBuilder(BACKGROUND, 0, 0, 156, 78).setTextureSize(180, 78).build();
		icon = guiHelper.createDrawableIngredient(new ItemStack(FTBStructuresBlocks.CRATE.get()));
		up = guiHelper.createDrawable(new ResourceLocation("textures/gui/server_selection.png"), 96, 0, 16, 16);
		down = guiHelper.createDrawable(new ResourceLocation("textures/gui/server_selection.png"), 64, 16, 16, 16);
		up2 = guiHelper.createDrawable(new ResourceLocation("textures/gui/server_selection.png"), 96, 32, 16, 16);
		down2 = guiHelper.createDrawable(new ResourceLocation("textures/gui/server_selection.png"), 64, 48, 16, 16);
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
			if (!result.isEmpty()) {
				list.add(result);
			}
		}

		ingr.setOutputs(VanillaTypes.ITEM, list);
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, Loot loot, IIngredients ingr) {
		IGuiItemStackGroup itemStacks = recipeLayout.getItemStacks();

		itemStacks.init(0, true, 4, 4);
		itemStacks.set(0, loot.item.getDefaultInstance());

		// only initialise these for now, we will update them later
		for (int i = 0; i < 21; i++) {
			itemStacks.init(1 + i, false, 27 + (i % 7 * 18), 4 + i / 7 * 24);
		}

		wrappers.put(loot, new WrappedLoot(loot, recipeLayout, ingr));
	}

	// TODO: add rolls hint (where though, there's no space)
	@Override
	public void draw(Loot loot, PoseStack ms, double mouseX, double mouseY) {
		IRecipeCategory.super.draw(loot, ms, mouseX, mouseY);

		WrappedLoot wrapper = wrappers.get(loot);

		if (wrapper != null) {
			int size = wrapper.loot.items.size();

			int min = wrapper.index * 21;
			int max = Math.min(min + 21, size);

			if (max < size) {
				if (mouseX >= 4 && mouseX <= 20 && mouseY >= 56 && mouseY <= 72) {
					down2.draw(ms, 4, 56);
				} else {
					down.draw(ms, 4, 56);
				}
			}

			if (min > 0) {
				if (mouseX >= 4 && mouseX <= 20 && mouseY >= 24 && mouseY <= 40) {
					up2.draw(ms, 4, 24);
				} else {
					up.draw(ms, 4, 24);
				}
			}

			String page = (wrapper.index + 1) + "/" + (wrapper.maxIndex() + 1);

			Font font = Minecraft.getInstance().font;
			float width = font.width(page);

			ms.pushPose();
			ms.translate(12, 44, 100);
			ms.scale(1, 1, 8000F);
			font.draw(ms, page, (-width / 2) + 1, 0, 0x555555);
			ms.popPose();

			IGuiItemStackGroup stacks = wrapper.layout.getItemStacks();

			for (int i = 0; i < 21; i++) {
				stacks.set(i + 1, (ItemStack) null);
			}

			WeightedSet<ItemStack> items = loot.items;
			Iterator<Entry<ItemStack>> iterator = items.descendingIterator();
			Iterators.advance(iterator, min);

			int row = 0;
			for (int i = 0; i < max - min; i++) {
				Entry<ItemStack> item = iterator.next();
				ItemStack result = item.result;

				if (result.isEmpty()) {
					result = new ItemStack(Items.BARRIER);
					result.setHoverName(FTBStructuresLang.JEI_LOOT_NOTHING.withStyle(ChatFormatting.RED));
				}

				stacks.set(i + 1, result);

				double c = (double) item.weight / (double) items.totalWeight * 100;
				String chance = c < 0.1 ? "< 0.1%" : String.format("%.2f%%", c);

				if (i > 0 && i % 7 == 0) {
					row++;
				}

				ms.pushPose();
				ms.translate(36 + (i % 7 * 18), 23.5f + (row * 24), 100);
				ms.scale(.5F, .5F, 8000F);
				Gui.drawCenteredString(ms, Minecraft.getInstance().font, chance, 0, 0, 0xFFFFFF);
				ms.popPose();

			}
		}
	}

	@Override
	public boolean handleClick(Loot loot, double mouseX, double mouseY, int button) {
		if (button == 0 && wrappers.containsKey(loot)) {
			WrappedLoot wrapper = wrappers.get(loot);
			if (mouseX >= 4 && mouseX <= 20) {
				if (mouseY >= 24 && mouseY <= 40 && wrapper.index > 0) {
					wrapper.index--;
					Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
					return true;
				}
				if (mouseY >= 56 && mouseY <= 72 && wrapper.index < wrapper.maxIndex()) {
					wrapper.index++;
					Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
					return true;
				}
			}
		}
		return IRecipeCategory.super.handleClick(loot, mouseX, mouseY, button);
	}
}