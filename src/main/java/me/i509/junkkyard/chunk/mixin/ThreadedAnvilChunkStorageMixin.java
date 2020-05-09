package me.i509.junkkyard.chunk.mixin;

import me.i509.junkkyard.chunk.api.LoadChunkCallback;
import me.i509.junkkyard.chunk.api.ChunkDeserializeCallback;
import me.i509.junkkyard.chunk.api.ChunkSerializeCallback;
import me.i509.junkkyard.chunk.api.ServerChunkEvent;
import me.i509.junkkyard.chunk.api.UnloadChunkCallback;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ChunkHolder;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.ThreadedAnvilChunkStorage;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.concurrent.CompletableFuture;

@Mixin(ThreadedAnvilChunkStorage.class)
public abstract class ThreadedAnvilChunkStorageMixin {
	@Shadow
	@Final
	private ServerWorld world;

	/**
	 * Inside of "loadChunk"
	 */
	@Inject(method = "method_17256", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;setLastSaveTime(J)V"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private void onLoadChunkData(ChunkPos pos, CallbackInfoReturnable<Chunk> callbackInfoReturnable, CompoundTag tag, boolean valid, Chunk chunk) {
		ServerChunkEvent.DESERIALIZE.invoker().onDeserialize(this.world, chunk, tag);
	}

	@Inject(method = "save(Lnet/minecraft/world/chunk/Chunk;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ThreadedAnvilChunkStorage;setTagAt(Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/nbt/CompoundTag;)V"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private void onSaveChunkData(Chunk chunk, CallbackInfoReturnable<Boolean> cir, ChunkPos pos, ChunkStatus status, CompoundTag tag) {
		ServerChunkEvent.SERIALIZE.invoker().onSerialize(this.world, chunk, tag);
	}

	// Chunk (Un)Load events, An explanation:
	// Must of this code is wrapped inside of futures and consumers, so it's generally a mess.

	/**
	 * Injection is inside of tryUnloadChunk
	 *
	 * We inject just after "setLoadedToWorld" is made false, since here the WorldChunk is guaranteed to be unloaded
	 */
	@Inject(method = "method_18843", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/WorldChunk;setLoadedToWorld(Z)V", shift = At.Shift.AFTER))
	private void onChunkUnload(ChunkHolder chunkHolder, CompletableFuture<Chunk> chunkFuture, long pos, Chunk chunk, CallbackInfo ci) {
		ServerChunkEvent.UNLOAD.invoker().onChunkUnload(this.world, chunk);
	}

	/**
	 * Injection is inside of convertToFullChunk?
	 *
	 * The following is expected contractually:
	 *
	 * the chunk being loaded MUST be a WorldChunk
	 * everything within the chunk has been loaded into the world. Entities, BlockEntities, etc.
	 */
	@Inject(method = "method_17227", at = @At("TAIL"))
	private void onChunkLoad(ChunkHolder chunkHolder, Chunk protoChunk, CallbackInfoReturnable<Chunk> callbackInfoReturnable) {
		// We fire the event at TAIL since the chunk is guaranteed to be a WorldChunk then.
		ServerChunkEvent.LOAD.invoker().onChunkLoad(this.world, callbackInfoReturnable.getReturnValue());
	}
}
