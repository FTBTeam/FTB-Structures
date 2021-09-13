package dev.ftb.mods.ftbstructures.kubejs;

import dev.ftb.mods.ftbstructures.FTBStructures;
import dev.latvian.kubejs.KubeJSPlugin;
import dev.latvian.kubejs.recipe.RegisterRecipeHandlersEvent;
import net.minecraft.resources.ResourceLocation;

public class FTBStructuresKubeJSPlugin extends KubeJSPlugin {
	@Override
	public void addRecipes(RegisterRecipeHandlersEvent event) {
		event.ignore(new ResourceLocation(FTBStructures.MOD_ID, "loot"));
	}
}
