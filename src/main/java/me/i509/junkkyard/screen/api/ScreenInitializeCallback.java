package me.i509.junkkyard.screen.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

@Environment(EnvType.CLIENT)
public interface ScreenInitializeCallback {
	Event<ScreenInitializeCallback> EVENT = EventFactory.createArrayBacked(ScreenInitializeCallback.class, callbacks -> (screen, context, client, windowWidth, windowHeight) -> {
		for (ScreenInitializeCallback callback : callbacks) {
			callback.onInitialize(screen, context, client, windowWidth, windowHeight);
		}
	});

	void onInitialize(Screen screen, ScreenContext context, MinecraftClient client, int windowWidth, int windowHeight);
}
