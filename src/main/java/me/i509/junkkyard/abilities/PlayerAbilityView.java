package me.i509.junkkyard.abilities;

import java.util.Collection;

public interface PlayerAbilityView {
	<A extends Ability<A>> A getAbility(AbilityKey<A> key);

	Collection<Ability<?>> getAbilities();
}
