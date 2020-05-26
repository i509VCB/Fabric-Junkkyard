package me.i509.junkkyard.villager.mixin;

import java.util.Map;
import java.util.stream.Stream;

import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.item.Item;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.village.VillagerType;

import me.i509.junkkyard.villager.impl.DefaultedMap;

@Mixin(targets = "net/minecraft/village/TradeOffers$TypeAwareBuyForOneEmeraldFactory")
public abstract class TypeAwareTradeMixin {
	@Final
	@Mutable
	@Shadow
	private Map<VillagerType, Item> map;

	/**
	 * So vanilla will check the VillagerType -> Item map in the stream and throw an exception for missing villager types.
	 * We want to prevent this so we can register custom villager types. So we return an empty stream so it will never throw an exception.
	 */
	@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/registry/DefaultedRegistry;stream()Ljava/util/stream/Stream;"))
	private <T> Stream<T> disableVanillaCheck(DefaultedRegistry<T> registry) {
		return Stream.empty();
	}

	/**
	 * We set the map to a map which will default to the plains key in case of a null value.
	 * This is so we avoid having air as the buying item in trades that villagers of custom types can hold.
	 * Avoid the AccessWidener by @Coercing the package-private object that is basically this to object.
	 */
	@Redirect(method = "<init>", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD, target = "Lnet/minecraft/village/TradeOffers$TypeAwareBuyForOneEmeraldFactory;map:Ljava/util/Map;"))
	private void useDefaultedMap(@Coerce Object self, Map<VillagerType, Item> value) {
		this.map = new DefaultedMap<>(value, VillagerType.PLAINS);
	}
}
