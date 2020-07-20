package me.i509.junkkyard.schem;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Identifier;

public interface SchematicBlockEntity extends ExtraDataHolder {
	int getRelativeX();

	int getRelativeY();

	int getRelativeZ();

	Identifier getId();

	/* @Nullable */ BlockEntity createBlockEntity();
}
