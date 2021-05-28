package dev.ftb.mods.ftbstructures.client;

import dev.ftb.mods.ftbstructures.FTBStructures;
import dev.ftb.mods.ftbstructures.FTBStructuresCommon;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = FTBStructures.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FTBStructuresClient extends FTBStructuresCommon {
	@Override
	public void init() {
	}

	@SubscribeEvent
	public static void setup(FMLClientSetupEvent event) {
		// ItemBlockRenderTypes.setRenderLayer(FTBStructuresBlocks.CRATE.get(), RenderType.cutout());
	}
}
