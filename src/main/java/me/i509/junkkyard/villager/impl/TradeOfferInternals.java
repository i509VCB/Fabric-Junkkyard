package me.i509.junkkyard.villager.impl;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;

import me.i509.junkkyard.villager.api.TradeOfferHelper;

public final class TradeOfferInternals {
	private static final Map<VillagerProfession, RegistrationEntry> VILLAGER_PROFESSION_FACTORIES = new IdentityHashMap<>();

	// TODO: Figure out how to handle existing mods. Maybe refresh the list after any additions
	public static void registerVillagerProfessionOffers(VillagerProfession profession, Consumer<TradeOfferHelper.Registrar> consumer) {
		Objects.requireNonNull(profession, "Profession cannot be null");
		Objects.requireNonNull(consumer, "Consumer cannot be null");
		VILLAGER_PROFESSION_FACTORIES.computeIfAbsent(profession, key -> new RegistrationEntry()).consumers.add(consumer);
	}

	public static void applyVillagerTradeOffers(Map<VillagerProfession, Int2ObjectMap<TradeOffers.Factory[]>> tradeMap) {
		for (Map.Entry<VillagerProfession, RegistrationEntry> professionFactoryEntry : VILLAGER_PROFESSION_FACTORIES.entrySet()) {
			final VillagerProfession profession = professionFactoryEntry.getKey();
			final Int2ObjectMap<TradeOffers.Factory[]> levelToFactoryMap = tradeMap.computeIfAbsent(profession, key -> new Int2ObjectArrayMap<>());

			final Registrar registrar = new Registrar();

			for (Consumer<TradeOfferHelper.Registrar> consumer : professionFactoryEntry.getValue().consumers) {
				consumer.accept(registrar);
			}

			for (Map.Entry<Integer, List<TradeOffers.Factory>> registeredFactories : registrar.factories.entrySet()) {
				final int professionLevel = registeredFactories.getKey();
				final TradeOffers.Factory[] tradeFactories = levelToFactoryMap.computeIfAbsent(professionLevel, key -> new TradeOffers.Factory[0]);
				final TradeOffers.Factory[] addedFactories = registeredFactories.getValue().toArray(new TradeOffers.Factory[0]);
				final TradeOffers.Factory[] result = TradeOfferInternals.merge(tradeFactories, addedFactories);
				levelToFactoryMap.put(professionLevel, result);
			}
		}
	}

	private static TradeOffers.Factory[] merge(TradeOffers.Factory[] array, TradeOffers.Factory[] other) {
		final TradeOffers.Factory[] result = (TradeOffers.Factory[]) Array.newInstance(TradeOffers.Factory.class, array.length + other.length);
		System.arraycopy(array, 0, result, 0, array.length);
		System.arraycopy(other, array.length - 1, result, array.length - 1, other.length);

		return result;
	}

	private static final class RegistrationEntry {
		private final List<Consumer<TradeOfferHelper.Registrar>> consumers = new ArrayList<>();

		private RegistrationEntry() {
		}
	}

	private static final class Registrar implements TradeOfferHelper.Registrar {
		private final Map<Integer, List<TradeOffers.Factory>> factories = new IdentityHashMap<>();

		private Registrar() {
		}

		@Override
		public void register(int level, TradeOffers.Factory factory) {
			this.factories.computeIfAbsent(level, key -> new ArrayList<>()).add(factory);
		}
	}

	private TradeOfferInternals() {
	}
}
