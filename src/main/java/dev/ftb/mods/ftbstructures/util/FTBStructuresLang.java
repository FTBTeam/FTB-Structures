package dev.ftb.mods.ftbstructures.util;

import dev.ftb.mods.ftbstructures.FTBStructures;
import net.minecraft.network.chat.TranslatableComponent;

public class FTBStructuresLang {
	public static TranslatableComponent TAB_LANG = FTBStructures.reg().addRawLang("itemGroup." + FTBStructures.MOD_ID, "FTB Structures");

	public static TranslatableComponent JEI_LOOT_TITLE = FTBStructures.reg().addRawLang("jei." + FTBStructures.MOD_ID + ".loot", "Loot");
	public static TranslatableComponent JEI_LOOT_NOTHING = FTBStructures.reg().addRawLang("jei." + FTBStructures.MOD_ID + ".nothing", "No Drop");

	public static void init() {
	}
}
