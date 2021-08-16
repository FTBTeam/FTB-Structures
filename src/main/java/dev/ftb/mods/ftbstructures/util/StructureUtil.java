package dev.ftb.mods.ftbstructures.util;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.ftb.mods.ftbstructures.FTBStructures;
import dev.ftb.mods.ftbstructures.FTBStructuresData;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.Random;

public enum StructureUtil {
	;

	public static boolean placeWithPlaceholders(WorldGenLevel level, FTBStructuresData.Structure structure, BlockPos pos, Random random) {
		StructureTemplate template = structure.getTemplate(level);
		int sx = template.getSize().getX();
		int sz = template.getSize().getZ();

		StructurePlaceSettings settings = new StructurePlaceSettings();
		settings.setRandom(random);
		settings.setRotationPivot(new BlockPos(sx / 2, 0, sz / 2));
		settings.setRotation(Rotation.getRandom(random));
		settings.setMirror(Mirror.values()[random.nextInt(3)]);

		BlockPos templatePosition = new BlockPos(pos.getX() - sx / 2, pos.getY() + structure.y, pos.getZ() - sz / 2);
		template.placeInWorld(level, templatePosition, settings, random);

		for (StructureTemplate.StructureBlockInfo info : template.filterBlocks(templatePosition, settings, Blocks.STRUCTURE_BLOCK)) {
			if (info.nbt != null && StructureMode.valueOf(info.nbt.getString("mode")) == StructureMode.DATA) {
				String id = info.nbt.getString("metadata");
				if (FTBStructuresData.palettes.containsKey(id)) {
					BlockState state;
					BlockStateParser parser = new BlockStateParser(new StringReader(FTBStructuresData.palettes.get(id).getOne(random, "")), false);

					try {
						parser.parse(false);
						state = parser.getState();
					} catch (CommandSyntaxException e) {
						FTBStructures.LOGGER.warn("Error parsing block state: {}", e.getRawMessage());
						state = Blocks.AIR.defaultBlockState();
					}
					level.setBlock(info.pos, state, 3);
				}
			}
		}

		return true;
	}

}
