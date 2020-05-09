package me.i509.junkkyard.entity.mixin;

import me.i509.junkkyard.entity.api.ServerEntityEvents;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {
	@Shadow
	boolean inEntityTick;

	// Call our load event after vanilla has loaded the entity
	@Inject(method = "loadEntityUnchecked", at = @At("TAIL"))
	private void onLoadEntity(Entity entity, CallbackInfo ci) {
		if (!this.inEntityTick) { // Copy vanilla logic, we cannot load entities while the game is ticking entities
			ServerEntityEvents.LOAD.invoker().onEntityLoad(entity, (ServerWorld) (Object) this);
		}
	}

	// TODO: Killing an entity does not unload it
	// Call our unload event before vanilla does.
	@Inject(method = "unloadEntity", at = @At("HEAD"))
	private void onUnloadEntity(Entity entity, CallbackInfo ci) {
		ServerEntityEvents.UNLOAD.invoker().onEntityUnload(entity, (ServerWorld) (Object) this);
	}
}
