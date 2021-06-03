package dev.ftb.mods.ftbstructures.recipe;

import com.google.gson.JsonObject;
import dev.ftb.mods.ftbstructures.FTBStructuresData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistryEntry;

/**
 * @author LatvianModder
 */
public class LootRecipeSerializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<LootRecipe> {
	@Override
	public LootRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
		LootRecipe recipe = new LootRecipe(recipeId);
		recipe.loot.addAll(FTBStructuresData.lootMap.values());
		return recipe;
	}

	@Override
	public LootRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
		LootRecipe recipe = new LootRecipe(recipeId);

		int s = buffer.readVarInt();

		for (int i = 0; i < s; i++) {
			recipe.loot.add(new FTBStructuresData.Loot(buffer));
		}

		return recipe;
	}

	@Override
	public void toNetwork(FriendlyByteBuf buffer, LootRecipe r) {
		buffer.writeVarInt(r.loot.size());

		for (FTBStructuresData.Loot loot : r.loot) {
			loot.write(buffer);
		}
	}
}
