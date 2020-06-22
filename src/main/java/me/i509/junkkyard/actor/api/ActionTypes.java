package me.i509.junkkyard.actor.api;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

/**
 * An enumeration of all built-in action types.
 */
public final class ActionTypes {
	public static final RegistryKey<Registry<ActionType>> REGISTRY_KEY = RegistryKey.ofRegistry(new Identifier("fabric", "actions"));
	static final Registry<ActionType> REGISTRY = FabricRegistryBuilder.createSimple(ActionType.class, new Identifier("fabric", "actions")).buildAndRegister();
}
