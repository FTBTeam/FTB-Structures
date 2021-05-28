package dev.ftb.mods.ftbstructures.recipe;

import dev.latvian.kubejs.integration.gamestages.GameStageKJSHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

/**
 * @author LatvianModder
 */
public class LootRecipe implements Recipe<NoInventory> {
	private final ResourceLocation id;
	private final String group;
	public Ingredient ingredient;
	public ItemStack result;
	public int weight;
	public String stage;

	public LootRecipe(ResourceLocation i, String g) {
		id = i;
		group = g;
		ingredient = Ingredient.EMPTY;
		result = ItemStack.EMPTY;
		weight = 1;
		stage = "";
	}

	@Override
	public boolean matches(NoInventory inv, Level world) {
		return true;
	}

	@Override
	public ItemStack assemble(NoInventory inv) {
		return result.copy();
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getResultItem() {
		return result;
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public String getGroup() {
		return group;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return FTBStructuresRecipeSerializers.LOOT.get();
	}

	@Override
	public RecipeType<?> getType() {
		return FTBStructuresRecipeSerializers.LOOT_TYPE;
	}

	public boolean isAvailableFor(Player player) {
		return stage.isEmpty() || GameStageKJSHelper.hasStage(player, stage);
	}
}