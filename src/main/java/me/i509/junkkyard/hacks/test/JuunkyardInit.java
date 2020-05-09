package me.i509.junkkyard.hacks.test;

import me.i509.junkkyard.entity.api.ServerEntityEvents;
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
	private int serverEntities = 0;

	@Override
	public void onInitialize() {
		/*ServerEntityEvents.LOAD.register((entity, world) -> {
			this.serverEntities++;
			System.out.println(this.serverEntities + " -> LD :: " + Registry.ENTITY_TYPE.getId(entity.getType()).toString());
		});

		ServerEntityEvents.UNLOAD.register((entity, world) -> {
			this.serverEntities--;
			System.out.println(this.serverEntities + " -> UL :: " + Registry.ENTITY_TYPE.getId(entity.getType()).toString());
		});

		ServerStopCallback.EVENT.register(minecraftServer -> {
			this.serverEntities = 0; // All entities are unloaded, and should stop being tracked
		});

		ServerTickCallback.EVENT.register(minecraftServer -> {
			if (minecraftServer.getTicks() % 10 == 0) {
				System.out.println(minecraftServer.getTicks() + " :: " + this.serverEntities);

				int actual = 0;

				for (ServerWorld world : minecraftServer.getWorlds()) {
					actual += world.getEntities(null, entity -> true).size();
				}

				System.out.println("actual -- " + actual);
			}
		});*/

		// VillagerTypes break auditing the game
		VillagerType.BIOME_TO_TYPE.put(Biomes.BASALT_DELTAS, BORGAR);

		MixinEnvironment.getCurrentEnvironment().audit(); // Must audit at the end
	}
}
