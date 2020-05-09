package me.i509.junkkyard.entity.client.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.profiler.Profiler;

import me.i509.junkkyard.entity.api.EntityLoadCallback;
import me.i509.junkkyard.entity.api.EntityUnloadCallback;

@Environment(EnvType.CLIENT)
public class ClientEntityEvents {
	private ClientEntityEvents() {
	}

	@Environment(EnvType.CLIENT)
	public static final Event<EntityLoadCallback<ClientWorld>> LOAD = EventFactory.createArrayBacked(EntityLoadCallback.class, callbacks -> (entity, world) -> {
		final Profiler profiler = world.getProfiler();

		if (EventFactory.isProfilingEnabled()) {
			profiler.push("fabricClientEntityLoad");

			for (EntityLoadCallback<ClientWorld> callback : callbacks) {
				profiler.push(EventFactory.getHandlerName(callback));
				callback.onEntityLoad(entity, world);
				profiler.pop();
			}

			profiler.pop();
		} else {
			for (EntityLoadCallback<ClientWorld> callback : callbacks) {
				callback.onEntityLoad(entity, world);
			}
		}
	});

	@Environment(EnvType.CLIENT)
	public static final Event<EntityUnloadCallback<ClientWorld>> UNLOAD = EventFactory.createArrayBacked(EntityUnloadCallback.class, callbacks -> (entity, world) -> {
		final Profiler profiler = world.getProfiler();

		if (EventFactory.isProfilingEnabled()) {
			profiler.push("fabricClientEntityLoad");

			for (EntityUnloadCallback<ClientWorld> callback : callbacks) {
				profiler.push(EventFactory.getHandlerName(callback));
				callback.onEntityUnload(entity, world);
				profiler.pop();
			}

			profiler.pop();
		} else {
			for (EntityUnloadCallback<ClientWorld> callback : callbacks) {
				callback.onEntityUnload(entity, world);
			}
		}
	});
}
