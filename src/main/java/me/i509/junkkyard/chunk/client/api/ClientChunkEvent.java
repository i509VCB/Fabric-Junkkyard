package me.i509.junkkyard.chunk.client.api;

import me.i509.junkkyard.chunk.api.LoadChunkCallback;
import me.i509.junkkyard.chunk.api.UnloadChunkCallback;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.profiler.Profiler;

@Environment(EnvType.CLIENT)
public final class ClientChunkEvent {
	private ClientChunkEvent() {
	}

	/**
	 * An event called when a chunk is loaded on the client.
	 *
	 * @apiNote You should avoid modifying a ClientWorld.
	 */
	@Environment(EnvType.CLIENT)
	public static final Event<LoadChunkCallback<ClientWorld>> LOAD = EventFactory.createArrayBacked(LoadChunkCallback.class, callbacks -> (clientWorld, chunk) -> {
		if (EventFactory.isProfilingEnabled()) {
			Profiler profiler = clientWorld.getProfiler();
			profiler.push("fabricClientChunkLoad");

			for (LoadChunkCallback<ClientWorld> callback : callbacks) {
				profiler.push(EventFactory.getHandlerName(callback));
				callback.onChunkLoad(clientWorld, chunk);
				profiler.pop();
			}

			profiler.pop();
		} else {
			for (LoadChunkCallback<ClientWorld> callback : callbacks) {
				callback.onChunkLoad(clientWorld, chunk);
			}
		}
	});

	/**
	 * An event called when a chunk is unloaded on the client.
	 *
	 * <p>This typically occurs when a chunk is no longer within the client's render distance.
	 *
	 * @apiNote You should avoid modifying a ClientWorld.
	 */
	@Environment(EnvType.CLIENT)
	public static final Event<UnloadChunkCallback<ClientWorld>> UNLOAD = EventFactory.createArrayBacked(UnloadChunkCallback.class, callbacks -> (clientWorld, chunk) -> {
		if (EventFactory.isProfilingEnabled()) {
			Profiler profiler = clientWorld.getProfiler();
			profiler.push("fabricClientChunkUnload");

			for (UnloadChunkCallback<ClientWorld> callback : callbacks) {
				profiler.push(EventFactory.getHandlerName(callback));
				callback.onChunkUnload(clientWorld, chunk);
				profiler.pop();
			}

			profiler.pop();
		} else {
			for (UnloadChunkCallback<ClientWorld> callback : callbacks) {
				callback.onChunkUnload(clientWorld, chunk);
			}
		}
	});
}
