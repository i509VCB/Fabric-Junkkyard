package me.i509.junkkyard.blockentity.api;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.World;

public interface BlockEntityUnloadCallback<W extends World> {
	void onUnloadBlockEntity(BlockEntity blockEntity, W world);
}
