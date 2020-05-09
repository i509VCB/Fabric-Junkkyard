package me.i509.junkkyard.chunk.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.profiler.Profiler;

public final class ServerChunkEvent {
	private ServerChunkEvent() {
	}

	/**
	 * An event called when a chunk is loaded on a server.
	 *
	 * <p>When this event is called, it is safe to modify the chunk.
	 */
	public static final Event<LoadChunkCallback<ServerWorld>> LOAD = EventFactory.createArrayBacked(LoadChunkCallback.class, callbacks -> (serverWorld, chunk) -> {
		if (EventFactory.isProfilingEnabled()) {
			Profiler profiler = serverWorld.getProfiler();
			profiler.push("fabricServerChunkLoad");

			for (LoadChunkCallback<ServerWorld> callback : callbacks) {
				profiler.push(EventFactory.getHandlerName(callback));
				callback.onChunkLoad(serverWorld, chunk);
				profiler.pop();
			}

			profiler.pop();
		} else {
			for (LoadChunkCallback<ServerWorld> callback : callbacks) {
				callback.onChunkLoad(serverWorld, chunk);
			}
		}
	});

	/**
	 * An event called when a chunk is unloaded on a server.
	 *
	 * <p>When this event is called, the chunk can still be modified.
	 */
	public static final Event<UnloadChunkCallback<ServerWorld>> UNLOAD = EventFactory.createArrayBacked(UnloadChunkCallback.class, callbacks -> (serverWorld, chunk) -> {
		if (EventFactory.isProfilingEnabled()) {
			Profiler profiler = serverWorld.getProfiler();
			profiler.push("fabricServerChunkUnload");

			for (UnloadChunkCallback<ServerWorld> callback : callbacks) {
				profiler.push(EventFactory.getHandlerName(callback));
				callback.onChunkUnload(serverWorld, chunk);
				profiler.pop();
			}

			profiler.pop();
		} else {
			for (UnloadChunkCallback<ServerWorld> callback : callbacks) {
				callback.onChunkUnload(serverWorld, chunk);
			}
		}
	});

	/**
	 * An event called when a chunk is being serialized before being saved to the disk.
	 *
	 * <p>Note all modifications to the chunk will be discarded.
	 */
	public static final Event<ChunkSerializeCallback> SERIALIZE = EventFactory.createArrayBacked(ChunkSerializeCallback.class, callbacks -> (world, chunk, tag) -> {
		final boolean profilingEnabled = EventFactory.isProfilingEnabled();

		if (profilingEnabled) {
			world.getProfiler().visit("fabricChunkSerialize");
		}

		for (ChunkSerializeCallback callback : callbacks) {
			if (profilingEnabled) {
				world.getProfiler().visit(() -> "fabricChunkSerialize_" + EventFactory.getHandlerName(callback));
			}

			callback.onSerialize(world, chunk, tag);
		}
	});

	/**
	 * An event called when a chunk is being deserialized before being loaded into a world.
	 *
	 * <p>Modifications can be made to the chunk.
	 */
	public static final Event<ChunkDeserializeCallback> DESERIALIZE = EventFactory.createArrayBacked(ChunkDeserializeCallback.class, callbacks -> (world, chunk, tag) -> {
		final boolean profilingEnabled = EventFactory.isProfilingEnabled();

		if (profilingEnabled) {
			world.getProfiler().visit("fabricChunkDeserialize");
		}

		for (ChunkDeserializeCallback callback : callbacks) {
			if (profilingEnabled) {
				world.getProfiler().visit(() -> "fabricChunkDeserialize_" + EventFactory.getHandlerName(callback));
			}

			callback.onDeserialize(world, chunk, tag);
		}
	});
}
