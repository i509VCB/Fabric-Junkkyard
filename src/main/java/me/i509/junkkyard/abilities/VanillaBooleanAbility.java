package me.i509.junkkyard.abilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

import net.minecraft.entity.player.PlayerEntity;

class VanillaBooleanAbility extends AbstractVanillaAbility<BooleanAbility> implements BooleanAbility {
	private final List<AbilitySource> grantedSources = new ArrayList<>();
	private final Function<PlayerEntity, Boolean> getter;
	private final BiConsumer<PlayerEntity, Boolean> setter;

	protected VanillaBooleanAbility(AbilityKey<BooleanAbility> key, PlayerEntity player, Function<PlayerEntity, Boolean> getter, BiConsumer<PlayerEntity, Boolean> setter) {
		super(key, player);
		this.getter = getter;
		this.setter = setter;
	}

	@Override
	public boolean isEnabled() {
		return this.getter.apply(this.player);
	}

	@Override
	public void grant(AbilitySource source) {
		Objects.requireNonNull(source, "Source cannot be null");
		this.grantedSources.add(source);

		if (!this.isEnabled()) {
			this.setter.accept(this.player, true); // Grant it internally
		}
	}

	@Override
	public void revoke(AbilitySource source) {
		this.grantedSources.remove(source);

		if (this.grantedSources.isEmpty()) {

		}
	}

	@Override
	public boolean isGrantedBy(AbilitySource source) {
		return this.grantedSources.contains(source);
	}
}
