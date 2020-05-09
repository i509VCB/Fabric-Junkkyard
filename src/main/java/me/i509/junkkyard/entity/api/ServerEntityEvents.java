package me.i509.junkkyard.entity.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;

public final class ServerEntityEvents {
	private ServerEntityEvents() {
	}

	public static final Event<EntityLoadCallback<ServerWorld>> LOAD = EventFactory.createArrayBacked(EntityLoadCallback.class, callbacks -> (entity, world) -> {
		final Profiler profiler = world.getProfiler();

		if (EventFactory.isProfilingEnabled()) {
			profiler.push("fabricServerEntityLoad");

			for (EntityLoadCallback<ServerWorld> callback : callbacks) {
				profiler.push(EventFactory.getHandlerName(callback));
				callback.onEntityLoad(entity, world);
				profiler.pop();
			}

			profiler.pop();
		} else {
			for (EntityLoadCallback<ServerWorld> callback : callbacks) {
				profiler.push(EventFactory.getHandlerName(callback));
				callback.onEntityLoad(entity, world);
				profiler.pop();
			}
		}
	});

	public static final Event<EntityUnloadCallback<ServerWorld>> UNLOAD = EventFactory.createArrayBacked(EntityUnloadCallback.class, callbacks -> (entity, world) -> {
		final Profiler profiler = world.getProfiler();

		if (EventFactory.isProfilingEnabled()) {
			profiler.push("fabricServerEntityLoad");

			for (EntityUnloadCallback<ServerWorld> callback : callbacks) {
				profiler.push(EventFactory.getHandlerName(callback));
				callback.onEntityUnload(entity, world);
				profiler.pop();
			}

			profiler.pop();
		} else {
			for (EntityUnloadCallback<ServerWorld> callback : callbacks) {
				profiler.push(EventFactory.getHandlerName(callback));
				callback.onEntityUnload(entity, world);
				profiler.pop();
			}
		}
	});
}
