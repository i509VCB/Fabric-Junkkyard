package me.i509.junkkyard.villager.api;

import java.util.List;
import java.util.function.Consumer;

import com.google.common.collect.ImmutableList;

import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;

import me.i509.junkkyard.villager.impl.TradeOfferInternals;

/**
 * Utilities to help with registration of trade offers
 */
public final class TradeOfferHelper {
	public static void registerOffers(VillagerProfession profession, int level, Consumer<List<TradeOffers.Factory>> factories) {
		TradeOfferInternals.registerOffers(profession, level, factories);
	}
}
