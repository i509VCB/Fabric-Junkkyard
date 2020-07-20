package me.i509.junkkyard.schem;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;

final class SchematicBlockEntityImpl implements SchematicBlockEntity {
	private final int x;
	private final int y;
	private final int z;
	private final Identifier id;
	private final CompoundTag extraData;

	SchematicBlockEntityImpl(int x, int y, int z, String id, CompoundTag extraData) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.id = new Identifier(id);
		this.extraData = extraData;
	}

	@Override
	public int getRelativeX() {
		return this.x;
	}

	@Override
	public int getRelativeY() {
		return this.y;
	}

	@Override
	public int getRelativeZ() {
		return this.z;
	}

	@Override
	public Identifier getId() {
		return this.id;
	}

	@Override
	public BlockEntity createBlockEntity() {
		throw new UnimplementedException("SchematicBlockEntityImpl", "createBlockEntity");
	}

	@Override
	public CompoundTag getExtraData() {
		return this.extraData.copy(); // Provide a copy
	}
}
