package dev.ftb.mods.ftbstructures.block;

import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.ToolType;

import static dev.ftb.mods.ftbstructures.FTBStructures.reg;
import static net.minecraft.world.level.block.state.BlockBehaviour.Properties;

/**
 * @author LatvianModder
 */
@SuppressWarnings("unused")
public class FTBStructuresBlocks {

	private static final NonNullUnaryOperator<Properties> CRATE_PROPERTIES = p -> p.strength(5F, 6F)
			.sound(SoundType.WOOD)
			.noDrops()
			.noOcclusion()
			.harvestTool(ToolType.AXE);

	private static final NonNullUnaryOperator<Properties> BARREL_PROPERTIES = p -> p.strength(5F, 6F)
			.sound(SoundType.NETHERITE_BLOCK)
			.noDrops()
			.noOcclusion()
			.harvestTool(ToolType.PICKAXE);

	public static final BlockEntry<CrateBlock> CRATE = crate("crate", "Crate", CrateBlock::new).register();
	public static final BlockEntry<SmallCrateBlock> SMALL_CRATE = crate("small_crate", "Small Crate", SmallCrateBlock::new)
			.blockstate((ctx, sp) -> {
				ModelFile.ExistingModelFile smallCrateModel = sp.models().getExistingFile(ctx.getId());
				sp.getVariantBuilder(ctx.getEntry())
						.forAllStatesExcept(state -> ConfiguredModel.builder()
										.modelFile(smallCrateModel)
										.rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360).build(),
								BlockStateProperties.WATERLOGGED);
			})
			.register();
	public static final BlockEntry<CrateBlock> PULSATING_CRATE = crate("pulsating_crate", "Pulsating Crate", CrateBlock::new)
			.initialProperties(Material.BUILDABLE_GLASS, MaterialColor.NETHER)
			.properties(p -> p.harvestTool(ToolType.PICKAXE))
			.register();

	public static final BlockEntry<BarrelBlock> WHITE_BARREL = barrel("white_barrel", "White Barrel", BarrelBlock::new).register();
	public static final BlockEntry<BarrelBlock> GREEN_BARREL = barrel("green_barrel", "Green Barrel", BarrelBlock::new).register();
	public static final BlockEntry<BarrelBlock> BLUE_BARREL = barrel("blue_barrel", "Blue Barrel", BarrelBlock::new).register();
	public static final BlockEntry<BarrelBlock> PURPLE_BARREL = barrel("purple_barrel", "Purple Barrel", BarrelBlock::new).register();
	public static final BlockEntry<BarrelBlock> RED_BARREL = barrel("red_barrel", "Red Barrel", BarrelBlock::new).register();
	public static final BlockEntry<BarrelBlock> BLACK_BARREL = barrel("black_barrel", "Black Barrel", BarrelBlock::new).register();
	public static final BlockEntry<BarrelBlock> GOLDEN_BARREL = barrel("golden_barrel", "Golden Barrel", BarrelBlock::new).register();

	public static <T extends Block> BlockBuilder<T, ?> crate(String id, String name, NonNullFunction<Properties, T> builder) {
		return reg().block(id, builder)
				.initialProperties(Material.WOOD)
				.properties(CRATE_PROPERTIES)
				.lang(name)
				.simpleItem()
				.blockstate(((ctx, sp) -> sp.simpleBlock(ctx.getEntry(), sp.models().getExistingFile(ctx.getId()))));
	}

	public static <T extends Block> BlockBuilder<T, ?> barrel(String id, String name, NonNullFunction<Properties, T> builder) {
		return reg().block(id, builder)
				.initialProperties(Material.METAL)
				.properties(BARREL_PROPERTIES)
				.lang(name)
				.simpleItem()
				.blockstate(((ctx, sp) -> sp.simpleBlock(ctx.getEntry(), ConfiguredModel.allYRotations(sp.models().getExistingFile(ctx.getId()), 0, false))));
	}

	public static void init() {
	}
}
