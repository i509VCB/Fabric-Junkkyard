package me.i509.junkkyard.actor.api;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public final class ActionType {
	static ActionType createAndRegister(Identifier identifier) {
		return Registry.register(ActionTypes.REGISTRY, identifier, new ActionType(identifier));
	}

	private Identifier id;

	private ActionType(Identifier id) {
		this.id = id;
	}

	public Identifier getId() {
		return this.id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ActionType)) return false;

		ActionType type = (ActionType) o;

		return this.getId().equals(type.getId());
	}

	@Override
	public int hashCode() {
		return this.getId().hashCode();
	}
}
