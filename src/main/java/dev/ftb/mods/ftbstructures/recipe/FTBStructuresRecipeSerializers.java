package dev.ftb.mods.ftbstructures.recipe;

import com.tterrag.registrate.util.entry.RegistryEntry;
import dev.ftb.mods.ftbstructures.FTBStructures;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import static dev.ftb.mods.ftbstructures.FTBStructures.reg;

/**
 * @author LatvianModder
 */
public class FTBStructuresRecipeSerializers {
	public static final RegistryEntry<LootRecipeSerializer> LOOT = reg().simple("loot", RecipeSerializer.class, LootRecipeSerializer::new);
	public static final RecipeType<LootRecipe> LOOT_TYPE = RecipeType.register(FTBStructures.MOD_ID + ":loot");

	public static void init() {
	}
}