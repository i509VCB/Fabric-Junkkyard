package me.i509.junkkyard.villager.api;

import me.i509.junkkyard.villager.impl.FabricVillagerType;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.VillagerType;

import static com.google.common.base.Preconditions.checkNotNull;

public final class VillagerTypeHelper {
	/**
	 * Creates and registers a new {@link VillagerType}.
	 *
	 * @param id the registry id this villager type will be registered to.
	 * @param fishermanBoatItem the boat item a villager of this type should sell if the villager's profession is a {@link net.minecraft.village.VillagerProfession#FISHERMAN}.
	 * @return a new villager type.
	 */
	public static VillagerType registerVillagerType(final Identifier id, final Item fishermanBoatItem) {
		checkNotNull(fishermanBoatItem, "Boat item cannot be null");
		final VillagerType villagerType = new FabricVillagerType(id, fishermanBoatItem);

		return Registry.register(Registry.VILLAGER_TYPE, id, villagerType);
	}
}
