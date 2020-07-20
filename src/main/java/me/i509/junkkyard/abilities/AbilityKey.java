package me.i509.junkkyard.abilities;

import net.minecraft.util.Identifier;

public final class AbilityKey<A extends Ability<A>> {
	private Identifier id;

	AbilityKey(Identifier id) {
		this.id = id;
	}

	public Identifier getId() {
		return this.id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof AbilityKey)) return false;

		AbilityKey<?> that = (AbilityKey<?>) o;

		return this.getId().equals(that.getId());
	}

	@Override
	public int hashCode() {
		return this.getId().hashCode();
	}
}
