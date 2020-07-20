package me.i509.junkkyard;

import java.util.ArrayList;
import java.util.List;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.registry.RegistryEntryAddedCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import org.spongepowered.asm.mixin.MixinEnvironment;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.VillagerProfession;
import net.minecraft.village.VillagerType;

import me.i509.junkkyard.lifecycle.api.UnloadServerEntityCallback;
import me.i509.junkkyard.lifecycle.impl.TrackedWorld;
import me.i509.junkkyard.villager.api.TradeOfferHelper;

public class JuunkyardInit implements ModInitializer {
	//public static final VillagerType BORGAR = VillagerTypeHelper.registerVillagerType(new Identifier("test", "borgar"), Items.ANCIENT_DEBRIS);
	//public static final VillagerType TESTER = register("tester");

	private static VillagerType register(String name) {
		final Identifier identifier = new Identifier("test", name);

		return Registry.register(Registry.VILLAGER_TYPE, identifier, new VillagerType() {
			@Override
			public String toString() {
				return identifier.toString();
			}
		});
	}

	@Override
	public void onInitialize() {
		final net.fabricmc.loader.FabricLoader instance = net.fabricmc.loader.FabricLoader.INSTANCE;

		for (ModContainer c : FabricLoader.getInstance().getAllMods()) {
			System.out.println(c.getMetadata().getId());
			System.out.println(c.getRootPath());
		}

		ServerEntityEvents.ENTITY_LOAD.register((entity, serverWorld) -> {
			final TrackedWorld trackedWorld = (TrackedWorld) serverWorld;
			trackedWorld.fabric_startTracking(entity);
		});

		ServerTickEvents.END_WORLD_TICK.register(serverWorld -> {
			final TrackedWorld trackedWorld = (TrackedWorld) serverWorld;

			final List<Entity> worldEntities = serverWorld.getEntities(null, entity -> true);
			final List<Entity> trackedEntities = trackedWorld.fabric_getTrackedEntities();
			final List<Entity> toRemove = new ArrayList<>();

			// Fuck me Mojang
			// Iterate through all tracked entities we have from loads.
			for (Entity entity : trackedEntities) {
				// See if this entity is still being tracked by the world
				if (!worldEntities.contains(entity)) {
					// This entity is no longer tracked by the world. Let's mark it for removal it.
					// We can't remove it right now due to CMEs
					toRemove.add(entity);

					//UnloadServerEntityCallback.EVENT.invoker().onUnload(entity, serverWorld);
					//trackedWorld.fabric_stopTracking(entity);
				}
			}

			for (Entity entity : toRemove) {
				UnloadServerEntityCallback.EVENT.invoker().onUnload(entity, serverWorld);
				trackedWorld.fabric_stopTracking(entity);
			}
		});

		ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
			for (ServerWorld world : server.getWorlds()) {
				final TrackedWorld trackedWorld = (TrackedWorld) world;

				for (Entity entity : trackedWorld.fabric_getTrackedEntities()) {
					trackedWorld.fabric_stopTracking(entity); // Stop tracking all entities
				}
			}
		});

		//VillagerType.BIOME_TO_TYPE.put(Biomes.BASALT_DELTAS, TESTER);
		/*
		LoadWorldCallback.SERVER.register(serverWorld -> {
			System.out.println(serverWorld.getRegistryKey());
		});

		EntityChangeWorldEvents.BEFORE_CHANGE.register((entity, origin, destination) -> {
			System.out.println(String.format("BEFORE %s: %s -> %s", entity.toString(), origin.getRegistryKey().getValue(), destination.getRegistryKey().getValue()));
			return TypedActionResult.pass(destination);
		});

		EntityChangeWorldEvents.CHANGE.register((entity, origin, destination, teleport) -> {
			teleport.setX(teleport.getX() + 2.0D);
			System.out.println(String.format("CHANGE %s DST %s: %s -> %s", entity.toString(), teleport.toString(), origin.getRegistryKey().getValue(), destination.getRegistryKey().getValue()));
		});
		 */
		MixinEnvironment.getCurrentEnvironment().audit(); // Must audit at the end

		Registry.VILLAGER_PROFESSION.forEach(this::registerOffers);

		RegistryEntryAddedCallback.event(Registry.VILLAGER_PROFESSION).register((rawId, id, profession) -> {
			this.registerOffers(profession);
		});
	}

	private void registerOffers(VillagerProfession profession) {
		TradeOfferHelper.registerVillagerProfessionOffers(profession, registrar -> {

		});
	}
}
