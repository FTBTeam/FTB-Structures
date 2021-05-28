package dev.ftb.mods.ftbstructures.recipe;

import dev.ftb.mods.ftbstructures.FTBStructures;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author LatvianModder
 */
public class FTBStructuresRecipeSerializers {
	public static final DeferredRegister<RecipeSerializer<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, FTBStructures.MOD_ID);

	public static final RegistryObject<RecipeSerializer<?>> LOOT = REGISTRY.register("loot", LootRecipeSerializer::new);
	public static final RecipeType<LootRecipe> LOOT_TYPE = RecipeType.register(FTBStructures.MOD_ID + ":loot");
}