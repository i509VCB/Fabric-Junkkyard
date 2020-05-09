package me.i509.junkkyard.villager.mixin;

import com.google.common.collect.ImmutableMap;
import me.i509.junkkyard.villager.impl.FabricVillagerType;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(TradeOffers.class)
public abstract class TradeOffersMixin {
	@Redirect(
			at = @At(
					value = "INVOKE",
					target = "Lcom/google/common/collect/ImmutableMap$Builder;build()Lcom/google/common/collect/ImmutableMap;"
			),
			slice = @Slice(
					from = @At(value = "FIELD", target = "Lnet/minecraft/item/Items;PUFFERFISH:Lnet/minecraft/item/Item;"),
					to = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lcom/google/common/collect/ImmutableMap$Builder;build()Lcom/google/common/collect/ImmutableMap;")
			),
			method = "method_16929"
	)
	private static ImmutableMap<VillagerType, Item> test(ImmutableMap.Builder<VillagerType, Item> builder) {
		// Register the boat item for all villager types
		Registry.VILLAGER_TYPE.stream().filter(type -> type instanceof FabricVillagerType).forEach(type -> {
			builder.put(type, ((FabricVillagerType) type).getFishermanBoatItem());
		});

		return builder.build(); // Build the immutable map similarly to vanilla
	}
}
