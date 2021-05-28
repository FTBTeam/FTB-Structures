package dev.ftb.mods.ftbstructures.jei;

import dev.ftb.mods.ftbstructures.FTBStructures;
import dev.ftb.mods.ftbstructures.recipe.FTBStructuresRecipeSerializers;
import dev.ftb.mods.ftbstructures.recipe.NoInventory;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

/**
 * @author LatvianModder
 */
@JeiPlugin
public class FTBStructuresJEIPlugin implements IModPlugin {
	public static IJeiRuntime RUNTIME;

	@Override
	public ResourceLocation getPluginUid() {
		return new ResourceLocation(FTBStructures.MOD_ID + ":jei");
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime r) {
		RUNTIME = r;
	}

	@Override
	public void registerRecipes(IRecipeRegistration r) {
		Level level = Minecraft.getInstance().level;
		r.addRecipes(level.getRecipeManager().getRecipesFor(FTBStructuresRecipeSerializers.LOOT_TYPE, NoInventory.INSTANCE, level), LootCategory.UID);
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration r) {
		r.addRecipeCategories(new LootCategory(r.getJeiHelpers().getGuiHelper()));
	}
}