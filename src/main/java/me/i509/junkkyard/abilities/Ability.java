package me.i509.junkkyard.abilities;

public interface Ability<A extends Ability<A>> {
	AbilityKey<A> getKey();

	void revoke(AbilitySource source);

	boolean isGrantedBy(AbilitySource source);
}
