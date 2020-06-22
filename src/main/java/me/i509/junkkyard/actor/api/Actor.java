package me.i509.junkkyard.actor.api;

import net.minecraft.world.World;

/**
 * Represents the actor which can cause an action.
 */
public interface Actor {
	/**
	 * Gets the world this actor is in.
	 * For a server, this will be the the spawn world or the world where the server has acted.
	 */
	World getWorld();

	/**
	 * Gets the name of this actor.
	 */
	String getName();

	/**
	 * Checks whether this actor is a client actor.
	 */
	boolean isClient();
}
