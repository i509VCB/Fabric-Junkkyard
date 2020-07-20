package me.i509.junkkyard.schem;

import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public interface SchematicEntity extends ExtraDataHolder {
	double getRelativeX();

	double getRelativeY();

	double getRelativeZ();

	Identifier getEntityType();

	/**
	 * Creates a minecraft entity using the schematic data.
	 *
	 * @param world the world to create the entity in
	 *
	 * @return a new entity.
	 */
	/* @Nullable */ Entity createEntity(World world);
}
