package dev.ftb.mods.ftbstructures.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.registries.ForgeRegistryEntry;

/**
 * @author LatvianModder
 */
public class LootRecipeSerializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<LootRecipe> {
	@Override
	public LootRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
		LootRecipe r = new LootRecipe(recipeId, json.has("group") ? json.get("group").getAsString() : "");

		if (json.has("weight")) {
			r.weight = json.get("weight").getAsInt();
		}

		if (json.has("stage")) {
			r.stage = json.get("stage").getAsString();
		}

		if (json.has("ingredient")) {
			r.ingredient = Ingredient.fromJson(json.get("ingredient"));
		}

		if (json.has("result")) {
			r.result = ShapedRecipe.itemFromJson(json.get("result").getAsJsonObject());
		}

		return r;
	}

	@Override
	public LootRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
		LootRecipe r = new LootRecipe(recipeId, buffer.readUtf(Short.MAX_VALUE));
		r.weight = buffer.readVarInt();
		r.stage = buffer.readUtf(Short.MAX_VALUE);
		r.ingredient = Ingredient.fromNetwork(buffer);
		r.result = buffer.readItem();
		return r;
	}

	@Override
	public void toNetwork(FriendlyByteBuf buffer, LootRecipe r) {
		buffer.writeUtf(r.getGroup(), Short.MAX_VALUE);
		buffer.writeVarInt(r.weight);
		buffer.writeUtf(r.stage, Short.MAX_VALUE);
		r.ingredient.toNetwork(buffer);
		buffer.writeItem(r.result);
	}
}
