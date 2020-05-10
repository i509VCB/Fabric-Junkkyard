package me.i509.junkkyard.entity.impl;

import com.google.common.collect.Streams;
import me.i509.junkkyard.chunk.api.ServerChunkEvent;
import me.i509.junkkyard.entity.api.ServerEntityEvents;
import me.i509.junkkyard.hacks.test.JuunkyardInit;
import net.fabricmc.fabric.api.event.server.ServerStopCallback;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.TypeFilterableList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;

import java.util.List;
import java.util.stream.StreamSupport;

public class ServerEntityTests {
	private int serverEntities = 0;

	public ServerEntityTests(JuunkyardInit init) {
		this.init();
	}

	private void init() {
		ServerEntityEvents.LOAD.register((entity, world) -> {
			this.serverEntities++;
			System.out.println(this.serverEntities + " -> SERVER-ELD :: " + Registry.ENTITY_TYPE.getId(entity.getType()).toString());
		});

		ServerEntityEvents.UNLOAD.register((entity, world) -> {
			this.serverEntities--;
			System.out.println(this.serverEntities + " -> SERVER-EUL :: " + Registry.ENTITY_TYPE.getId(entity.getType()).toString());
		});

		ServerStopCallback.EVENT.register(minecraftServer -> {
			System.out.println("STOP");
			int amt = 0;

			for (ServerWorld world : minecraftServer.getWorlds()) {
				amt += world.getEntities(null, entity -> true).size();
			}
			System.out.println(amt);

			// fixme: I have no idea where to find a proper way to do this that doesn't involve super nasty chunk serializer hacks. Just clean it up during server stop
			for (ServerWorld world : minecraftServer.getWorlds()) {
				final List<Entity> entities = world.getEntities(null, entity -> true);
				for (Entity entity : entities) {
					// Players are weird and are actually not unloaded by the server automatically on shutdown. So we have to manually fire the player's events
					ServerEntityEvents.UNLOAD.invoker().onEntityUnload(entity, world);
					System.out.println("<-" + Registry.ENTITY_TYPE.getId(entity.getType()).toString() + "->" + this.serverEntities);
				}
			}

			//this.serverEntities = 0; // All entities are unloaded, and should stop being tracked
			System.out.println("[SERVER] =========>" + this.serverEntities + "<==========");
		});

		ServerTickCallback.EVENT.register(minecraftServer -> {
			if (minecraftServer.getTicks() % 100 == 0) {
				System.out.println(minecraftServer.getTicks() + " :SERVER--E: " + this.serverEntities);

				int actual = 0;

				for (ServerWorld world : minecraftServer.getWorlds()) {
					actual += world.getEntities(null, entity -> true).size();
				}

				System.out.println("actual SERVER -- " + actual);
			}
		});
	}
}
