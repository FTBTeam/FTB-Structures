package dev.ftb.mods.ftbstructures.kubejs;

import dev.latvian.kubejs.recipe.RecipeJS;
import dev.latvian.kubejs.util.ListJS;

public class LootRecipeJS extends RecipeJS {
	@Override
	public void create(ListJS args) {
		inputItems.add(parseIngredientItem(args.get(1)));
		outputItems.add(parseResultItem(args.get(0)));
	}

	public LootRecipeJS weight(int t) {
		json.addProperty("weight", t);
		save();
		return this;
	}

	@Override
	public RecipeJS stage(String s) {
		json.addProperty("stage", s);
		save();
		return this;
	}

	@Override
	public void deserialize() {
		inputItems.add(parseIngredientItem(json.get("ingredient")));
		outputItems.add(parseResultItem(json.get("result")));
	}

	@Override
	public void serialize() {
		if (serializeOutputs) {
			json.add("result", outputItems.get(0).toResultJson());
		}

		if (serializeInputs) {
			json.add("ingredient", inputItems.get(0).toJson());
		}
	}
}
