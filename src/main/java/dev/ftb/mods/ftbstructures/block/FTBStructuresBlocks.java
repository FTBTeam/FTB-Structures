package dev.ftb.mods.ftbstructures.block;

import dev.ftb.mods.ftbstructures.FTBStructures;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author LatvianModder
 */
public class FTBStructuresBlocks {
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, FTBStructures.MOD_ID);

	public static final RegistryObject<Block> SMALL_CRATE = REGISTRY.register("small_crate", SmallCrateBlock::new);
	public static final RegistryObject<Block> CRATE = REGISTRY.register("crate", CrateBlock::new);
	public static final RegistryObject<Block> WHITE_BARREL = REGISTRY.register("white_barrel", BarrelBlock::new);
	public static final RegistryObject<Block> RED_BARREL = REGISTRY.register("red_barrel", BarrelBlock::new);
	public static final RegistryObject<Block> BLUE_BARREL = REGISTRY.register("blue_barrel", BarrelBlock::new);
}
