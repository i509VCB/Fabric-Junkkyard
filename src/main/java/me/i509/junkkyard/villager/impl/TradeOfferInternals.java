package me.i509.junkkyard.villager.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;

public final class TradeOfferInternals {
	private static final Map<VillagerProfession, Int2ObjectMap<TradeOffers.Factory[]>> TRADE_FACTORIES = new HashMap<>();

	private TradeOfferInternals() {
	}

	public static void registerOffers(VillagerProfession profession, int level, Consumer<List<TradeOffers.Factory>> factory) {
		final List<TradeOffers.Factory> list = new ArrayList<>();
		factory.accept(list);

		final TradeOffers.Factory[] additionalEntries = list.toArray(new TradeOffers.Factory[0]);
		final Int2ObjectMap<TradeOffers.Factory[]> professionEntry = TRADE_FACTORIES.computeIfAbsent(profession, p -> new Int2ObjectOpenHashMap<>());

		final TradeOffers.Factory[] currentEntries = professionEntry.computeIfAbsent(level, l -> new TradeOffers.Factory[0]);
		final TradeOffers.Factory[] newEntries = merge(additionalEntries, currentEntries);
		professionEntry.put(level, newEntries);
	}

	public static void populateOffers(Map<VillagerProfession, Int2ObjectMap<TradeOffers.Factory[]>> tradeMap) {
		for (Map.Entry<VillagerProfession, Int2ObjectMap<TradeOffers.Factory[]>> tradeFactoryEntry : TRADE_FACTORIES.entrySet()) {
			// Create an empty map or get all existing profession entries
			final Int2ObjectMap<TradeOffers.Factory[]> leveledFactoryMap = tradeMap.computeIfAbsent(tradeFactoryEntry.getKey(), k -> new Int2ObjectOpenHashMap<>());
			// Get our existing entries
			final Int2ObjectMap<TradeOffers.Factory[]> value = tradeFactoryEntry.getValue();

			// Iterate through the existing entries
			for (Int2ObjectMap.Entry<TradeOffers.Factory[]> entry : value.int2ObjectEntrySet()) {
				// Make a new array to merge the existing and factory provided entries
				final TradeOffers.Factory[] result = merge(leveledFactoryMap.get(entry.getIntKey()), entry.getValue());
				// And write the new array to the map to be passed back to the public static field
				leveledFactoryMap.put(entry.getIntKey(), result);
			}
		}
	}

	private static TradeOffers.Factory[] merge(TradeOffers.Factory[] array, TradeOffers.Factory[] other) {
		TradeOffers.Factory[] result = new TradeOffers.Factory[array.length + other.length];
		System.arraycopy(array, 0, result, 0, array.length);
		System.arraycopy(other, 0, result, array.length, other.length);

		return result;
	}
}
