package me.i509.junkkyard.entity.impl;

import me.i509.junkkyard.entity.client.api.ClientEntityEvents;
import me.i509.junkkyard.render.JunkkyardClient;
import net.fabricmc.fabric.api.event.server.ServerStopCallback;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.registry.Registry;

public class ClientEntityTests {
	private int clientEntities = 0;

	public ClientEntityTests(JunkkyardClient junkkyardClient) {
		this.init();
	}

	private void init() {
		ClientEntityEvents.LOAD.register((entity, world) -> {
			this.clientEntities++;
			System.out.println(this.clientEntities + " -> CLIENT-ELD :: " + Registry.ENTITY_TYPE.getId(entity.getType()).toString());
		});

		ClientEntityEvents.UNLOAD.register((entity, world) -> {
			this.clientEntities--;
			System.out.println(this.clientEntities + " -> CLIENT-EUL :: " + Registry.ENTITY_TYPE.getId(entity.getType()).toString());
		});

		ServerTickCallback.EVENT.register(minecraftServer -> {
			if (minecraftServer.getTicks() % 100 == 0 && MinecraftClient.getInstance().world != null) {
				System.out.println(minecraftServer.getTicks() + " :CLIENT--E: " + this.clientEntities);

				int actual = 0;
				for (Entity entity : MinecraftClient.getInstance().world.getEntities()) {
					actual++;
				}

				System.out.println("actualEC -- " + actual);
			}
		});

		// TODO: Bind this to `disconnect from server instead?` -- TEMP: TAIL of MinecraftClient#disconnect(Screen)
		ServerStopCallback.EVENT.register(minecraftServer -> {
			if (!minecraftServer.isDedicated()) { // fixme: Use ClientNetworking#PLAY_DISCONNECTED instead of the server stop callback, and then stop tracking here
				System.out.println("[CLIENT] =========>" + this.clientEntities + "<==========");
				//this.clientEntities = 0; // We need to stop tracking all entities since we are being disconnected
			}
		});
	}
}
