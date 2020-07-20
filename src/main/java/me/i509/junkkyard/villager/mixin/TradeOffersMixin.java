package me.i509.junkkyard.villager.mixin;

import java.util.HashMap;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;

import me.i509.junkkyard.villager.impl.TradeOfferInternals;

@Mixin(TradeOffers.class)
public abstract class TradeOffersMixin {
	@Inject(method = "method_16929(Ljava/util/HashMap;)V", at = @At("TAIL"))
	private void appendCustomTrades(HashMap<VillagerProfession, Int2ObjectMap<TradeOffers.Factory[]>> tradeMap, CallbackInfo ci) {
		TradeOfferInternals.applyVillagerTradeOffers(tradeMap);
	}
}
