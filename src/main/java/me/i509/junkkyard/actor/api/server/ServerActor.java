package me.i509.junkkyard.actor.api.server;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

import me.i509.junkkyard.actor.api.Actor;

public interface ServerActor extends Actor {
	@Override
	ServerWorld getWorld();

	@Override
	default boolean isClient() {
		return false;
	}

	MinecraftServer getServer();
}
