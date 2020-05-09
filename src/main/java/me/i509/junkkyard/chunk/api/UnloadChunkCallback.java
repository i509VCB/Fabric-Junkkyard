package me.i509.junkkyard.chunk.api;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/**
 * Contains events that are called when a chunk is unloaded from a world.
 *
 * @apiNote Contractually, all chunks being unloaded must be a {@link net.minecraft.world.chunk.WorldChunk}.
 *
 * @param <W> the type of world
 */
public interface UnloadChunkCallback<W extends World> {
	void onChunkUnload(W world, Chunk chunk);
}
