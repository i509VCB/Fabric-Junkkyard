package me.i509.junkkyard.blockentity.api;

import me.i509.junkkyard.entity.api.EntityLoadCallback;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;

public final class ServerBlockEntityEvents {
	private ServerBlockEntityEvents() {
	}

	public static final Event<BlockEntityLoadCallback<ServerWorld>> LOAD = EventFactory.createArrayBacked(BlockEntityLoadCallback.class, callbacks -> (blockEntity, world) -> {
		final Profiler profiler = world.getProfiler();

		if (EventFactory.isProfilingEnabled()) {
			profiler.push("fabricServerBlockEntityLoad");

			for (BlockEntityLoadCallback<ServerWorld> callback : callbacks) {
				profiler.push(EventFactory.getHandlerName(callback));
				callback.onLoadBlockEntity(blockEntity, world);
				profiler.pop();
			}

			profiler.pop();
		} else {
			for (BlockEntityLoadCallback<ServerWorld> callback : callbacks) {
				callback.onLoadBlockEntity(blockEntity, world);
			}
		}
	});

	public static final Event<BlockEntityUnloadCallback<ServerWorld>> UNLOAD = EventFactory.createArrayBacked(BlockEntityUnloadCallback.class, callbacks -> (blockEntity, world) -> {
		final Profiler profiler = world.getProfiler();

		if (EventFactory.isProfilingEnabled()) {
			profiler.push("fabricServerBlockEntityUnload");

			for (BlockEntityUnloadCallback<ServerWorld> callback : callbacks) {
				profiler.push(EventFactory.getHandlerName(callback));
				callback.onUnloadBlockEntity(blockEntity, world);
				profiler.pop();
			}

			profiler.pop();
		} else {
			for (BlockEntityUnloadCallback<ServerWorld> callback : callbacks) {
				callback.onUnloadBlockEntity(blockEntity, world);
			}
		}
	});
}
