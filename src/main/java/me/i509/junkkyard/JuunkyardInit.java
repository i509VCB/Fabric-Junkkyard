package me.i509.junkkyard;

import net.fabricmc.api.ModInitializer;
import org.spongepowered.asm.mixin.MixinEnvironment;

import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.VillagerType;
import net.minecraft.world.biome.Biomes;

import me.i509.junkkyard.entity.EntityChangeWorldEvents;
import me.i509.junkkyard.lifecycle.api.LoadWorldCallback;

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
		//VillagerType.BIOME_TO_TYPE.put(Biomes.BASALT_DELTAS, TESTER);

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

		MixinEnvironment.getCurrentEnvironment().audit(); // Must audit at the end
	}
}
