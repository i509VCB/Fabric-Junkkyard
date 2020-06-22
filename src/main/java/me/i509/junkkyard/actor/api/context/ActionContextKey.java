package me.i509.junkkyard.actor.api.context;

import com.google.common.base.MoreObjects;
import com.google.common.reflect.TypeToken;

import net.minecraft.util.Identifier;

public final class ActionContextKey<T> {
	private final Identifier id;
	private final TypeToken<T> allowedType;

	private ActionContextKey(Identifier id, TypeToken<T> allowedType) {
		this.id = id;
		this.allowedType = allowedType;
	}

	public static <T> ActionContextKey.Builder<T> builder() {
		return new Builder<T>();
	}

	public Identifier getId() {
		return this.id;
	}

	public TypeToken<T> getAllowedType() {
		return this.allowedType;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ActionContextKey)) return false;

		ActionContextKey<?> that = (ActionContextKey<?>) o;

		return getId().equals(that.getId());
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("id", this.getId())
				.add("allowedType", this.getAllowedType())
				.toString();
	}

	public static final class Builder<T> {
		private Identifier id;
		@SuppressWarnings("UnstableApiUsage")
		private TypeToken<T> allowedType;

		private Builder() {
		}

		public Builder<T> id(Identifier id) {
			this.id = id;
			return this;
		}

		public <N> Builder<N> type(Class<N> allowedType) {
			//noinspection UnstableApiUsage
			return type(TypeToken.of(allowedType));
		}

		@SuppressWarnings({"UnstableApiUsage", "unchecked"})
		public <N> Builder<N> type(TypeToken<N> allowedType) {
			this.allowedType = (TypeToken<T>) allowedType;
			return (Builder<N>) this;
		}

		public ActionContextKey<T> build() {
			return new ActionContextKey<>(this.id, this.allowedType);
		}
	}
}
