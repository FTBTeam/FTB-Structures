package dev.ftb.mods.ftbstructures.kubejs;

import dev.ftb.mods.ftbstructures.FTBStructures;
import dev.latvian.kubejs.KubeJSPlugin;
import dev.latvian.kubejs.recipe.RegisterRecipeHandlersEvent;
import net.minecraft.resources.ResourceLocation;

public class FTBJarModKubeJSPlugin extends KubeJSPlugin {
	@Override
	public void init() {
		RegisterRecipeHandlersEvent.EVENT.register(event -> event.register(new ResourceLocation(FTBStructures.MOD_ID, "loot"), LootRecipeJS::new));
	}
}
