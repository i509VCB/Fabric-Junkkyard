package me.i509.junkkyard.hacks.test;

import me.i509.junkkyard.blockentity.api.ServerBlockEntityEvents;
import me.i509.junkkyard.entity.api.ServerEntityEvents;
import me.i509.junkkyard.entity.impl.ServerEntityTests;
import me.i509.junkkyard.villager.api.VillagerTypeHelper;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.server.ServerStopCallback;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.VillagerType;
import net.minecraft.world.biome.Biomes;
import org.spongepowered.asm.mixin.MixinEnvironment;

public class JuunkyardInit implements ModInitializer {
	public static final VillagerType BORGAR = VillagerTypeHelper.registerVillagerType(new Identifier("test", "borgar"), Items.ANCIENT_DEBRIS);

	private int serverBlockEntities = 0;
	private ServerEntityTests serverTest;

	@Override
	public void onInitialize() {
		this.serverTest = new ServerEntityTests(this);

		/*
		ServerBlockEntityEvents.LOAD.register((entity, world) -> {
			this.serverBlockEntities++;
			System.out.println(this.serverBlockEntities + " -> LD :: " + Registry.BLOCK_ENTITY_TYPE.getId(entity.getType()).toString());
		});

		ServerBlockEntityEvents.UNLOAD.register((blockEntity, world) -> {
			this.serverBlockEntities--;
			System.out.println(this.serverBlockEntities + " -> UL :: " + Registry.BLOCK_ENTITY_TYPE.getId(blockEntity.getType()).toString());
		});

		ServerStopCallback.EVENT.register(minecraftServer -> {
			this.serverBlockEntities = 0; // All blockentities are unloaded, and should stop being tracked
		});

		ServerTickCallback.EVENT.register(minecraftServer -> {
			if (minecraftServer.getTicks() % 10 == 0) {
				System.out.println(minecraftServer.getTicks() + " :: " + this.serverBlockEntities);

				int actual = 0;

				for (ServerWorld world : minecraftServer.getWorlds()) {
					// TODO: Query all block entities
					actual += world.blockEntities.size(); // I hope people are nice and don't add there
				}

				System.out.println("actual -- " + actual);
			}
		});
		 */

		// VillagerTypes break auditing the game
		VillagerType.BIOME_TO_TYPE.put(Biomes.BASALT_DELTAS, BORGAR);

		MixinEnvironment.getCurrentEnvironment().audit(); // Must audit at the end
	}
}
