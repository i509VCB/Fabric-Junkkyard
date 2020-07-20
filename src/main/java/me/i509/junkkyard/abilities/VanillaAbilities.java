package me.i509.junkkyard.abilities;

import net.minecraft.entity.player.PlayerAbilities;

import me.i509.junkkyard.abilities.mixin.PlayerAbilitiesAccessor;

/**
 * An enumeration of all vanilla {@link PlayerAbilities player abilities}.
 */
public final class VanillaAbilities {
	public static final AbilityKey<BooleanAbility> INVULNERABLE = AbilityRegistry.createVanillaBoolean("invulnerable",
			player -> player.abilities.invulnerable,
			(player, value) -> {
				player.abilities.invulnerable = value;
				player.sendAbilitiesUpdate();
			});

	public static final AbilityKey<BooleanAbility> FLYING = AbilityRegistry.createVanillaBoolean("flying",
			player -> player.abilities.flying,
			(player, value) -> {
				player.abilities.flying = value;
				player.sendAbilitiesUpdate();
			});

	public static final AbilityKey<BooleanAbility> ALLOW_FLYING = AbilityRegistry.createVanillaBoolean("allow_flying",
			player -> player.abilities.allowFlying,
			(player, value) -> {
				player.abilities.allowFlying = value;
				player.sendAbilitiesUpdate();
			});

	public static final AbilityKey<BooleanAbility> CREATIVE_MODE = AbilityRegistry.createVanillaBoolean("creative_mode",
			player -> player.abilities.creativeMode,
			(player, value) -> {
				player.abilities.creativeMode = value;
				player.sendAbilitiesUpdate();
			});

	public static final AbilityKey<BooleanAbility> ALLOW_WORLD_MODIFICATION = AbilityRegistry.createVanillaBoolean("allow_world_modification",
			player -> player.abilities.allowModifyWorld,
			(player, value) -> {
				player.abilities.allowModifyWorld = value;
				player.sendAbilitiesUpdate();
			});

	public static final AbilityKey<FloatAbility> FLY_SPEED = AbilityRegistry.createVanillaFloat("fly_speed",
			player -> ((PlayerAbilitiesAccessor) player.abilities).accessor$getFlySpeed(),
			(player, value) -> {
				((PlayerAbilitiesAccessor) player.abilities).accessor$setFlySpeed(value);
				player.sendAbilitiesUpdate();
			});

	public static final AbilityKey<FloatAbility> WALK_SPEED = AbilityRegistry.createVanillaFloat("walk_speed",
			player -> ((PlayerAbilitiesAccessor) player.abilities).accessor$getWalkSpeed(),
			(player, value) -> {
				((PlayerAbilitiesAccessor) player.abilities).accessor$setWalkSpeed(value);
				player.sendAbilitiesUpdate();
			});

	private VanillaAbilities() {
	}
}
