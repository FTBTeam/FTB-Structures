package dev.ftb.mods.ftbstructures.recipe;

import dev.ftb.mods.ftbstructures.FTBStructuresData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class LootRecipe implements Recipe<NoInventory> {
	private final ResourceLocation id;
	public final List<FTBStructuresData.Loot> loot;

	public LootRecipe(ResourceLocation i) {
		id = i;
		loot = new ArrayList<>();
	}

	@Override
	public boolean matches(NoInventory inv, Level world) {
		return true;
	}

	@Override
	public ItemStack assemble(NoInventory inv) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return true;
	}

	@Override
	public ItemStack getResultItem() {
		return ItemStack.EMPTY;
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}

	@Override
	public String getGroup() {
		return "";
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return FTBStructuresRecipeSerializers.LOOT.get();
	}

	@Override
	public RecipeType<?> getType() {
		return FTBStructuresRecipeSerializers.LOOT_TYPE;
	}

	@Override
	public boolean isSpecial() {
		return true;
	}
}