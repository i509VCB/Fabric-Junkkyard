package me.i509.junkkyard.ticking.impl;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientBlockEntityEvents;

public class TickEventClientMod implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientBlockEntityEvents.BLOCK_ENTITY_LOAD.register((blockEntity, clientWorld) -> {

		});

		ClientBlockEntityEvents.BLOCK_ENTITY_UNLOAD.register((blockEntity, clientWorld) -> {

		});
	}
}
