package me.i509.junkkyard.lifecycle.impl;

import java.util.List;

import net.minecraft.entity.Entity;

public interface TrackedWorld {
	void fabric_startTracking(Entity entity);

	void fabric_stopTracking(Entity entity);

	List<Entity> fabric_getTrackedEntities();
}
