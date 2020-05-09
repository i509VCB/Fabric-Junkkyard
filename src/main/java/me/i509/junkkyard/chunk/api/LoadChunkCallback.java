package me.i509.junkkyard.chunk.api;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/**
 * Contains events that are called when a chunk is loaded into a world.
 *
 * @apiNote Contractually, all chunks being loaded must be a {@link net.minecraft.world.chunk.WorldChunk}.
 *
 * @param <W> the type of world
 */
public interface LoadChunkCallback<W extends World> {
	void onChunkLoad(W world, Chunk chunk);
}
