package me.i509.junkkyard.villager.mixin;

import java.util.Random;
import java.util.stream.Stream;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.village.TradeOffer;

@Mixin(targets = "net/minecraft/village/TradeOffers$TypeAwareBuyForOneEmeraldFactory")
public abstract class TypeAwareTradeMixin {
	/**
	 * Vanilla will check the "VillagerType -> Item" map in the stream and throw an exception for villager types not specified in the map. This breaks any and all custom villager types.
	 * We want to prevent this so we can register custom villager types. So we return an empty stream so it will never throw an exception.
	 */
	@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/registry/DefaultedRegistry;stream()Ljava/util/stream/Stream;"))
	private <T> Stream<T> disableVanillaCheck(DefaultedRegistry<T> registry) {
		return Stream.empty();
	}

	@Inject(method = "create", at = @At(value = "NEW", target = "net/minecraft/village/TradeOffer"), locals = LocalCapture.CAPTURE_FAILEXCEPTION, cancellable = true)
	private void failOnNullItem(Entity entity, Random random, CallbackInfoReturnable<TradeOffer> cir, ItemStack buyingItem) {
		if (buyingItem.isEmpty()) { // Will return true for an "empty" item stack that had null passed in the ctor
			cir.setReturnValue(null); // Return null to prevent creation of empty trades
		}
	}
}
