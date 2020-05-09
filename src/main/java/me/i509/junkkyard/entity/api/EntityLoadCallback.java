package me.i509.junkkyard.entity.api;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public interface EntityLoadCallback<W extends World> {
	void onEntityLoad(Entity entity, W world);
}
