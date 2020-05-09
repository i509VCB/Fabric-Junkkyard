package me.i509.junkkyard.entity.mixin.client;

import me.i509.junkkyard.entity.client.api.ClientEntityEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin {
	// Call our load event after vanilla has loaded the entity
	@Inject(method = "addEntityPrivate", at = @At("TAIL"))
	private void onEntityLoad(int id, Entity entity, CallbackInfo ci) {
		ClientEntityEvents.LOAD.invoker().onEntityLoad(entity, (ClientWorld) (Object) this);
	}

	// Call our unload event before vanilla does.
	@Inject(method = "finishRemovingEntity", at = @At("HEAD"))
	private void onEntityUnload(Entity entity, CallbackInfo ci) {
		ClientEntityEvents.UNLOAD.invoker().onEntityUnload(entity, (ClientWorld) (Object) this);
	}
}
