package me.i509.junkkyard.chunk.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.chunk.Chunk;

public interface ChunkDeserializeCallback {
	void onDeserialize(ServerWorld world, Chunk chunk, CompoundTag tag);
}
