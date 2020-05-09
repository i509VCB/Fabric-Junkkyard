package me.i509.junkkyard.blockentity.api;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.World;

public interface BlockEntityLoadCallback<W extends World> {
	void onLoadBlockEntity(BlockEntity blockEntity, W world);
}
