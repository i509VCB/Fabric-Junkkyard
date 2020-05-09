package me.i509.junkkyard.blockentity.client.api;

import me.i509.junkkyard.blockentity.api.BlockEntityLoadCallback;
import me.i509.junkkyard.blockentity.api.BlockEntityUnloadCallback;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;

@Environment(EnvType.CLIENT)
public final class ClientBlockEntityEvents {
	private ClientBlockEntityEvents() {
	}

	@Environment(EnvType.CLIENT)
	public static final Event<BlockEntityLoadCallback<ClientWorld>> LOAD = EventFactory.createArrayBacked(BlockEntityLoadCallback.class, callbacks -> (blockEntity, world) -> {
		final Profiler profiler = world.getProfiler();

		if (EventFactory.isProfilingEnabled()) {
			profiler.push("fabricClientBlockEntityLoad");

			for (BlockEntityLoadCallback<ClientWorld> callback : callbacks) {
				profiler.push(EventFactory.getHandlerName(callback));
				callback.onLoadBlockEntity(blockEntity, world);
				profiler.pop();
			}

			profiler.pop();
		} else {
			for (BlockEntityLoadCallback<ClientWorld> callback : callbacks) {
				callback.onLoadBlockEntity(blockEntity, world);
			}
		}
	});

	@Environment(EnvType.CLIENT)
	public static final Event<BlockEntityUnloadCallback<ClientWorld>> UNLOAD = EventFactory.createArrayBacked(BlockEntityUnloadCallback.class, callbacks -> (blockEntity, world) -> {
		final Profiler profiler = world.getProfiler();

		if (EventFactory.isProfilingEnabled()) {
			profiler.push("fabricClientBlockEntityUnload");

			for (BlockEntityUnloadCallback<ClientWorld> callback : callbacks) {
				profiler.push(EventFactory.getHandlerName(callback));
				callback.onUnloadBlockEntity(blockEntity, world);
				profiler.pop();
			}

			profiler.pop();
		} else {
			for (BlockEntityUnloadCallback<ClientWorld> callback : callbacks) {
				callback.onUnloadBlockEntity(blockEntity, world);
			}
		}
	});
}
