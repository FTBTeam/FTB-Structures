package dev.ftb.mods.ftbstructures.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class StateWithData {
	public static final StateWithData EMPTY = new StateWithData(Blocks.AIR.defaultBlockState(), null);

	public final BlockState state;
	public final CompoundTag data;

	public StateWithData(BlockState state, @Nullable CompoundTag data) {
		this.state = state;
		this.data = data;
	}
}
