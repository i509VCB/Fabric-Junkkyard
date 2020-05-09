package me.i509.junkkyard.chunk.mixin.client;

import me.i509.junkkyard.chunk.api.LoadChunkCallback;
import me.i509.junkkyard.chunk.api.UnloadChunkCallback;
import me.i509.junkkyard.chunk.client.api.ClientChunkEvent;
import net.minecraft.client.world.ClientChunkManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.world.biome.source.BiomeArray;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ClientChunkManager.class)
public abstract class ClientChunkManagerMixin {
	@Final
	@Shadow
	private ClientWorld world;

	@Inject(method = "loadChunkFromPacket", at = @At("TAIL"))
	private void onChunkLoad(int chunkX, int chunkZ, BiomeArray biomes, PacketByteBuf buf, CompoundTag tag, int k, boolean complete, CallbackInfoReturnable<WorldChunk> cir) {
		ClientChunkEvent.LOAD.invoker().onChunkLoad(this.world, cir.getReturnValue());
	}

	@Inject(method = "unload", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientChunkManager$ClientChunkMap;compareAndSet(ILnet/minecraft/world/chunk/WorldChunk;Lnet/minecraft/world/chunk/WorldChunk;)Lnet/minecraft/world/chunk/WorldChunk;", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private void onChunkUnload(int chunkX, int chunkZ, CallbackInfo ci, int i, WorldChunk chunk) {
		ClientChunkEvent.UNLOAD.invoker().onChunkUnload(this.world, chunk);
	}
}
