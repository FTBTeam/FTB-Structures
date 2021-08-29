package dev.ftb.mods.ftbstructures.integration.jei;

import dev.ftb.mods.ftbstructures.FTBStructuresData;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;

public class WrappedLoot {

	public final FTBStructuresData.Loot loot;
	public final IRecipeLayout layout;
	public final IIngredients ingredients;
	public int index = 0;

	public WrappedLoot(FTBStructuresData.Loot loot, IRecipeLayout layout, IIngredients ingredients) {
		this.loot = loot;
		this.layout = layout;
		this.ingredients = ingredients;
	}

	public int maxIndex() {
		return (loot.items.size() - 1) / 21;
	}

}
