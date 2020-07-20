package me.i509.junkkyard.abilities;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public final class AbilityRegistry {
	private static final Map<AbilityKey<?>, AbilityFactory<?>> ABILITIES = new IdentityHashMap<>();

	static AbilityKey<BooleanAbility> createVanillaBoolean(String name, Function<PlayerEntity, Boolean> getter, BiConsumer<PlayerEntity, Boolean> setter) {
		final AbilityKey<BooleanAbility> key = new AbilityKey<>(new Identifier(name));
		ABILITIES.put(key, new VanillaBooleanAbilityFactory(key, getter, setter));

		return key;
	}

	static AbilityKey<FloatAbility> createVanillaFloat(String name, Function<PlayerEntity, Float> getter, BiConsumer<PlayerEntity, Float> setter) {
		final AbilityKey<FloatAbility> key = new AbilityKey<>(new Identifier(name));
		ABILITIES.put(key, new VanillaFloatAbilityFactory(key, getter, setter));

		return key;
	}

	static class VanillaFloatAbilityFactory extends AbilityFactory<FloatAbility> {
		private final AbilityKey<FloatAbility> key;
		private final Function<PlayerEntity, Float> getter;
		private final BiConsumer<PlayerEntity, Float> setter;

		VanillaFloatAbilityFactory(AbilityKey<FloatAbility> key, Function<PlayerEntity, Float> getter, BiConsumer<PlayerEntity, Float> setter) {
			this.key = key;
			this.getter = getter;
			this.setter = setter;
		}

		public FloatAbility create(PlayerEntity player) {
			return new VanillaFloatAbility(this.key, player, this.getter, this.setter);
		}
	}

	static class VanillaBooleanAbilityFactory extends AbilityFactory<BooleanAbility> {
		private final AbilityKey<BooleanAbility> key;
		private final Function<PlayerEntity, Boolean> getter;
		private final BiConsumer<PlayerEntity, Boolean> setter;

		VanillaBooleanAbilityFactory(AbilityKey<BooleanAbility> key, Function<PlayerEntity, Boolean> getter, BiConsumer<PlayerEntity, Boolean> setter) {
			this.key = key;
			this.getter = getter;
			this.setter = setter;
		}

		public BooleanAbility create(PlayerEntity player) {
			return new VanillaBooleanAbility(this.key, player, this.getter, this.setter);
		}
	}

	abstract static class AbilityFactory<A extends Ability<A>> {
	}
}
