package me.i509.junkkyard.lifecycle.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

import me.i509.junkkyard.lifecycle.api.LoadWorldCallback;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
	// The locals you have to manage for an inject are insane. A redirect is much cleaner.
	// Here is what it looks like with an inject: https://gist.github.com/i509VCB/f80077cc536eb4dba62b794eba5611c1
	@Redirect(method = "createWorlds", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
	private <K, V> V onLoadWorld(Map<K, V> map, K registryKey, V serverWorld) {
		// Add to map to replicate vanilla logic
		final V result = map.put(registryKey, serverWorld);

		// Fire away
		LoadWorldCallback.SERVER.invoker().onLoad((ServerWorld) serverWorld);
		return result;
	}
}
