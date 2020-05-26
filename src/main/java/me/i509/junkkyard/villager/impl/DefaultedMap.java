package me.i509.junkkyard.villager.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Map;

import com.google.common.collect.ForwardingMap;

public final class DefaultedMap<K, V> extends ForwardingMap<K, V> {
	private final Map<K, V> delegate;
	private final K fallbackKey;

	public DefaultedMap(final Map<K, V> delegate, final K fallbackKey) {
		this.delegate = delegate;
		checkNotNull(fallbackKey, "Fallback key cannot be null");
		checkNotNull(delegate.get(fallbackKey), "Result of map check for fallback key cannot be null");
		this.fallbackKey = fallbackKey;
	}

	@Override
	protected Map<K, V> delegate() {
		return this.delegate;
	}

	/* @NonNull */
	@Override
	public V get(Object key) {
		final V value = super.get(key);

		if (value != null) return value;

		return this.delegate.get(this.fallbackKey);
	}
}
