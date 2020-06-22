package me.i509.junkkyard.actor.impl;

import java.util.Map;
import java.util.NoSuchElementException;

import com.google.common.collect.ImmutableMap;

import me.i509.junkkyard.actor.api.context.ActionContext;
import me.i509.junkkyard.actor.api.context.ActionContextKey;

public final class ActionContextImpl implements ActionContext {
	public ActionContextImpl(ImmutableMap<ActionContextKey<?>, ?> context) {
		this.context = context;
	}

	private final Map<ActionContextKey<?>, ?> context;

	@Override
	public <T> boolean has(ActionContextKey<T> key) {
		return this.context.containsKey(key);
	}

	@Override
	public <T> T get(ActionContextKey<T> key) throws NoSuchElementException {
		//noinspection unchecked
		final T object = (T) this.context.get(key);

		if (object == null) {
			throw new NoSuchElementException(String.format("key %s is not present in this context.", key));
		}

		return object;
	}
}
