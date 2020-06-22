package me.i509.junkkyard.lifecycle.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import net.minecraft.server.world.ServerWorld;

public interface LoadWorldCallback {
	Event<LoadWorldCallback> SERVER = EventFactory.createArrayBacked(LoadWorldCallback.class, callbacks -> serverWorld -> {
		for (LoadWorldCallback callback : callbacks) {
			callback.onLoad(serverWorld);
		}
	});

	/**
	 * Called when a server world is loaded.
	 *
	 * @param serverWorld the server world
	 */
	void onLoad(ServerWorld serverWorld);
}
