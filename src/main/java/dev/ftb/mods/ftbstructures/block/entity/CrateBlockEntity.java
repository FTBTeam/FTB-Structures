package dev.ftb.mods.ftbstructures.block.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author LatvianModder
 */
public class CrateBlockEntity extends BlockEntity {
	public CrateBlockEntity() {
		super(FTBStructuresBlockEntities.CRATE.get());
	}

	@Override
	public CompoundTag save(CompoundTag compound) {
		return super.save(compound);
	}

	@Override
	public void load(BlockState state, CompoundTag compound) {
		super.load(state, compound);
	}
}
