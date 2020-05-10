package me.i509.junkkyard.world.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
	@Inject(method = "createWorlds", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"),
			slice = @Slice(to = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;initScoreboard(Lnet/minecraft/world/PersistentStateManager;)V")))
	private void onLoadOverworld(WorldGenerationProgressListener worldGenerationProgressListener, CallbackInfo ci) {
		// createWorlds
	}

	@Inject(method = "createWorlds", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"),
			slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/world/dimension/DimensionType;getAll()Ljava/lang/Iterable;")))
	private void onLoadOtherDimensions(WorldGenerationProgressListener worldGenerationProgressListener, CallbackInfo ci) {
		//
	}
}

