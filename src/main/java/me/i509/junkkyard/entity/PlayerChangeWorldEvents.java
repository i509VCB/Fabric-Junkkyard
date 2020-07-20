package me.i509.junkkyard.entity;

import net.minecraft.server.network.ServerPlayerEntity;

public final class PlayerChangeWorldEvents {
	public interface AfterChangeWorld {
		void afterChangeWorld(ServerPlayerEntity player);
	}
}
