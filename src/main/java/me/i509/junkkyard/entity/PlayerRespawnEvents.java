package me.i509.junkkyard.entity;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import net.minecraft.server.network.ServerPlayerEntity;

public final class PlayerRespawnEvents {
	//public static final Event<BeforeRespawn> BEFORE_RESPAWN = null;
	public static final Event<AfterRespawn> AFTER_RESPAWN = EventFactory.createArrayBacked(AfterRespawn.class, callbacks -> (oldPlayer, newPlayer, alive) -> {

	});

	/*public interface BeforeRespawn {
		Optional<Transform<ServerWorld>> beforePlayerRespawn(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, Transform<ServerWorld> respawnLocation, boolean alive);
	}*/

	public interface AfterRespawn {
		void onPlayerRespawn(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive);
	}
}
