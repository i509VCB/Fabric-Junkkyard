package me.i509.junkkyard.hacks.test;

import java.lang.reflect.InvocationTargetException;

import me.i509.junkkyard.hacks.PreLaunchHacks;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class JuunkyardPreLaunch implements PreLaunchEntrypoint {
	@Override
	public void onPreLaunch() {
		try {
			// Give AuthLib and Brigadier to Knot's Classloader so Mixin works.
			PreLaunchHacks.hackilyLoadForMixin("com.mojang.brigadier.LiteralMessage");
			PreLaunchHacks.hackilyLoadForMixin("com.mojang.authlib.UserType");
			//PreLaunchHacks.hackilyLoadForMixin("com.mojang.datafixers.TypeRewriteRule");

		} catch (ClassNotFoundException | InvocationTargetException | IllegalAccessException e) {
		//	throw new RuntimeException("Failed to give Knot Classloader required libraries", e);
		}
	}
}
