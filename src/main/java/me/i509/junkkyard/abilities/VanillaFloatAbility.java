package me.i509.junkkyard.abilities;

import java.util.function.BiConsumer;
import java.util.function.Function;

import net.minecraft.entity.player.PlayerEntity;

class VanillaFloatAbility extends AbstractVanillaAbility<FloatAbility> implements FloatAbility {
	private final Function<PlayerEntity, Float> getter;
	private final BiConsumer<PlayerEntity, Float> setter;

	protected VanillaFloatAbility(AbilityKey<FloatAbility> key, PlayerEntity player, Function<PlayerEntity, Float> getter, BiConsumer<PlayerEntity, Float> setter) {
		super(key, player);
		this.getter = getter;
		this.setter = setter;
	}

	@Override
	public Float getValue() {
		return this.getter.apply(this.player);
	}

	@Override
	public void grant(AbilitySource source, Operation operation, Float amount) {

	}

	@Override
	public void revoke(AbilitySource source) {

	}

	@Override
	public boolean isGrantedBy(AbilitySource source) {
		return false;
	}
}
