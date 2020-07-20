package me.i509.junkkyard.schem;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

final class SchematicEntityImpl implements SchematicEntity {
	@Override
	public double getRelativeX() {
		return 0;
	}

	@Override
	public double getRelativeY() {
		return 0;
	}

	@Override
	public double getRelativeZ() {
		return 0;
	}

	@Override
	public Identifier getEntityType() {
		return null;
	}

	@Override
	public Entity createEntity(World world) {
		return null;
	}

	@Override
	public CompoundTag getExtraData() {
		return null;
	}
}
