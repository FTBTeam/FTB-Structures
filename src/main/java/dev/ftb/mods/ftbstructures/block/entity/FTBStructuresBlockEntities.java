package dev.ftb.mods.ftbstructures.block.entity;

import dev.ftb.mods.ftbstructures.FTBStructures;
import dev.ftb.mods.ftbstructures.block.FTBStructuresBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @author LatvianModder
 */
public class FTBStructuresBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, FTBStructures.MOD_ID);

	public static final RegistryObject<BlockEntityType<CrateBlockEntity>> CRATE = REGISTRY.register("crate", () -> BlockEntityType.Builder.of(CrateBlockEntity::new,
			FTBStructuresBlocks.SMALL_CRATE.get(),
			FTBStructuresBlocks.CRATE.get()
	).build(null));
}