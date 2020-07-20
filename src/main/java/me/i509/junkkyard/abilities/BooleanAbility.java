package me.i509.junkkyard.abilities;

public interface BooleanAbility extends Ability<BooleanAbility> {
	boolean isEnabled();

	void grant(AbilitySource source);
}
