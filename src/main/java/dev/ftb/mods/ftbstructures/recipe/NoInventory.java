package dev.ftb.mods.ftbstructures.recipe;

import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

/**
 * @author LatvianModder
 */
public class NoInventory extends RecipeWrapper {
	public static final NoInventory INSTANCE = new NoInventory();

	private NoInventory() {
		super(new ItemStackHandler(0));
	}
}
