package me.i509.junkkyard.lifecycle.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;

public interface UnloadServerEntityCallback {
	Event<UnloadServerEntityCallback> EVENT = EventFactory.createArrayBacked(UnloadServerEntityCallback.class, callbacks -> (entity, world) -> {
		for (UnloadServerEntityCallback callback : callbacks) {
			callback.onUnload(entity, world);
		}
	});

	void onUnload(Entity entity, ServerWorld world);
}
