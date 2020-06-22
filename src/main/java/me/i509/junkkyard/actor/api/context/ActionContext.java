package me.i509.junkkyard.actor.api.context;

import java.util.NoSuchElementException;

import com.google.common.collect.ImmutableMap;

import me.i509.junkkyard.actor.impl.ActionContextImpl;

public interface ActionContext {
	static ActionContext.Builder builder() {
		return new Builder();
	}

	<T> boolean has(ActionContextKey<T> key);

	<T> T get(ActionContextKey<T> key) throws NoSuchElementException;

	final class Builder {
		private final ImmutableMap.Builder<ActionContextKey<?>, ?> context = ImmutableMap.builder();

		private Builder() {
		}

		public ActionContext build() {
			return new ActionContextImpl(this.context.build());
		}
	}
}
