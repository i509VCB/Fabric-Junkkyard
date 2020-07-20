package me.i509.junkkyard.abilities;

public interface NumberAbility<A extends NumberAbility<A, V>, V extends Number> extends Ability<A> {
	V getValue();

	void grant(AbilitySource source, Operation operation, V amount);

	enum Operation {
		ADD,
		MULTIPLY
	}
}
