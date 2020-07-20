package me.i509.junkkyard.lifecycle.mixin;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;

import me.i509.junkkyard.lifecycle.impl.TrackedWorld;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin implements TrackedWorld {
	@Unique
	private List<Entity> trackedEntities = new ArrayList<>();

	@Override
	public void fabric_startTracking(Entity entity) {
		this.trackedEntities.add(entity);
	}

	@Override
	public void fabric_stopTracking(Entity entity) {
		this.trackedEntities.remove(entity);
	}

	@Override
	public List<Entity> fabric_getTrackedEntities() {
		return this.trackedEntities;
	}
}
