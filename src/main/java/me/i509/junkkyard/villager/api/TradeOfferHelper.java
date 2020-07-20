package me.i509.junkkyard.villager.api;

import java.util.Arrays;
import java.util.function.Consumer;

import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;

import me.i509.junkkyard.villager.impl.TradeOfferInternals;

/**
 * Utilities to help with registration of trade offers
 */
public final class TradeOfferHelper {
	/**
	 * Used to register custom factories to create trade offers for a villager.
	 * This method may be used by mods which add custom villager professions or add custom villager trades to existing professions.
	 *
	 * <p>It is advised to use this method over directly adding to {@link TradeOffers#PROFESSION_TO_LEVELED_TRADE} since this method simplifies the process of adding trades to existing professions.
	 *
	 * <p>Below is an example of how to register a simple trade factory to an armorer.
	 * <blockquote><pre>
	 * TradeOfferHelper.registerOffers(VillagerProfession.ARMORER, registrar -> {
	 * 	// Register a trade to profession level 2
	 *	registrar.register(2, new MyTradeFactory(2));
	 * });
	 * </pre></blockquote>
	 *
	 * @param profession the profession
	 * @param consumer a consumer which registers trade offers
	 */
	public static void registerVillagerProfessionOffers(VillagerProfession profession, Consumer<Registrar> consumer) {
		TradeOfferInternals.registerVillagerProfessionOffers(profession, consumer);
	}

	public interface Registrar {
		/**
		 * Registers a trade offer factory.
		 *
		 * @param level the profession level this factory may be used by. Typically 1-5 in vanilla.
		 * @param factory the trade offer factory
		 */
		void register(int level, TradeOffers.Factory factory);

		/**
		 * Registers multiple trade offer factories.
		 *
		 * @param level the profession level this factory may be used by. Typically 1-5 in vanilla.
		 * @param factories trade offer factories to be registered
		 */
		default void registerAll(int level, TradeOffers.Factory[] factories) {
			this.registerAll(level, Arrays.asList(factories));
		}

		/**
		 * Registers multiple trade offer factories.
		 *
		 * @param level the profession level this factory may be used by, typically 1 through 5 in vanilla.
		 * @param factories trade offer factories to be registered
		 */
		default void registerAll(int level, Iterable<TradeOffers.Factory> factories) {
			for (TradeOffers.Factory factory : factories) {
				this.register(level, factory);
			}
		}
	}

	private TradeOfferHelper() {
	}
}
