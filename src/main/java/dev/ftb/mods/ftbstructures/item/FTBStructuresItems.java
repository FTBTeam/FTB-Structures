package dev.ftb.mods.ftbstructures.item;

import dev.ftb.mods.ftbstructures.FTBStructures;
import dev.ftb.mods.ftbstructures.block.FTBStructuresBlocks;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

/**
 * @author LatvianModder
 */
public class FTBStructuresItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, FTBStructures.MOD_ID);

	public static RegistryObject<BlockItem> blockItem(String id, Supplier<Block> sup) {
		return REGISTRY.register(id, () -> new BlockItem(sup.get(), new Item.Properties().tab(FTBStructures.group)));
	}

	public static final RegistryObject<BlockItem> SMALL_CRATE = blockItem("small_crate", FTBStructuresBlocks.SMALL_CRATE);
	public static final RegistryObject<BlockItem> CRATE = blockItem("crate", FTBStructuresBlocks.CRATE);
	public static final RegistryObject<BlockItem> WHITE_BARREL = blockItem("white_barrel", FTBStructuresBlocks.WHITE_BARREL);
	public static final RegistryObject<BlockItem> RED_BARREL = blockItem("red_barrel", FTBStructuresBlocks.RED_BARREL);
	public static final RegistryObject<BlockItem> BLUE_BARREL = blockItem("blue_barrel", FTBStructuresBlocks.BLUE_BARREL);
}