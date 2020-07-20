package me.i509.junkkyard.ticking.impl;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerBlockEntityEvents;

public class TickEventMod implements ModInitializer {
	private static final AbstractTickManager BLOCK_ENTITY_CLIENT_MANAGER = null;// = new AbstractTickManager();

	@Override
	public void onInitialize() {
		ServerBlockEntityEvents.BLOCK_ENTITY_LOAD.register((blockEntity, serverWorld) -> {

		});

		ServerBlockEntityEvents.BLOCK_ENTITY_UNLOAD.register((blockEntity, serverWorld) -> {

		});
	}
}
