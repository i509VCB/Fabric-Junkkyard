package me.i509.junkkyard;

import net.fabricmc.api.ModInitializer;
import org.spongepowered.asm.mixin.MixinEnvironment;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.VillagerType;
import net.minecraft.world.biome.Biomes;

public class JuunkyardInit implements ModInitializer {
	//public static final VillagerType BORGAR = VillagerTypeHelper.registerVillagerType(new Identifier("test", "borgar"), Items.ANCIENT_DEBRIS);
	public static final VillagerType TESTER = register("tester");

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
		VillagerType.BIOME_TO_TYPE.put(Biomes.BASALT_DELTAS, TESTER);

		TradeCallback.EVENT.register((trader, customer, offer) -> {
			return false; // No trades allowed
		});

		MixinEnvironment.getCurrentEnvironment().audit(); // Must audit at the end
	}
}
