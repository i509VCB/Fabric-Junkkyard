package me.i509.junkkyard.entity.mixin.client;

import me.i509.junkkyard.entity.client.api.ClientEntityEvents;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
	@Shadow
	private ClientWorld world;

	@Inject(method = "onPlayerRespawn", at = @At(value = "NEW", target = "net/minecraft/client/world/ClientWorld"))
	private void onPlayerRespawn(PlayerRespawnS2CPacket packet, CallbackInfo ci) {
		// If a world already exists, we need to unload all entities in the world.
		if (this.world != null) {
			for (Entity entity : world.getEntities()) {
				ClientEntityEvents.UNLOAD.invoker().onEntityUnload(entity, this.world);
			}
		}
	}

	/**
	 * An explanation why we unload entities during onGameJoin:
	 *
	 * Waterfall may send another Game Join packet if entity meta rewrite is disabled, so we will cover ourselves.
	 *
	 * Velocity by default will send a Game Join packet when the player changes servers, which will create a new client world.
	 *
	 * Anyone can send another GameJoinPacket at any time, so we need to watch out.
	 */
	@Inject(method = "onGameJoin", at = @At(value = "NEW", target = "net/minecraft/client/world/ClientWorld"))
	private void onGameJoin(GameJoinS2CPacket packet, CallbackInfo ci) {
		// If a world already exists, we need to unload all entities in the world.
		if (this.world != null) {
			for (Entity entity : world.getEntities()) {
				ClientEntityEvents.UNLOAD.invoker().onEntityUnload(entity, this.world);
			}
		}
	}

	// Called when the client disconnects from a server.
	@Inject(method = "clearWorld", at = @At("HEAD"))
	private void onClearWorld(CallbackInfo ci) {
		// If a world already exists, we need to unload all entities in the world.
		if (this.world != null) {
			for (Entity entity : world.getEntities()) {
				ClientEntityEvents.UNLOAD.invoker().onEntityUnload(entity, this.world);
			}
		}
	}
}
