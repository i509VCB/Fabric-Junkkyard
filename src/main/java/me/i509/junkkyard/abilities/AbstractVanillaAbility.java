package me.i509.junkkyard.abilities;

import net.minecraft.entity.player.PlayerEntity;

abstract class AbstractVanillaAbility<A extends Ability<A>> implements Ability<A> {
	private final AbilityKey<A> key;
	protected final PlayerEntity player;

	protected AbstractVanillaAbility(AbilityKey<A> key, PlayerEntity player) {
		this.key = key;
		this.player = player;
	}

	@Override
	public AbilityKey<A> getKey() {
		return key;
	}
}
