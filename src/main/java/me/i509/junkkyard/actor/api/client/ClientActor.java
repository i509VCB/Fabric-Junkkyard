package me.i509.junkkyard.actor.api.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;

import me.i509.junkkyard.actor.api.Actor;

@Environment(EnvType.CLIENT)
public interface ClientActor extends Actor {
	@Override
	ClientWorld getWorld();

	@Override
	default boolean isClient() {
		return true;
	}

	MinecraftClient getClient();
}
