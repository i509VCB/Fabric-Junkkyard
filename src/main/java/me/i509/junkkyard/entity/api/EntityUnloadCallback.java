package me.i509.junkkyard.entity.api;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public interface EntityUnloadCallback<W extends World> {
	void onEntityUnload(Entity entity, W world);
}
