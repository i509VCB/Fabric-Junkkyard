package me.i509.junkkyard.hud.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;

public final class RenderChatHudEvent {
	public static final Event<Render> RENDER = EventFactory.createArrayBacked(Render.class, callbacks -> (client, chatHud, inGameHud, matrices, tickDelta, ticks) -> {
		return RenderEventFactory.invokeBeforeRenderEvent(callbacks, callback -> callback.onRender(client, chatHud, inGameHud, matrices, tickDelta, ticks),
				client.getProfiler()::push, client.getProfiler()::pop, matrices::push, matrices::pop, "fabricRenderChatHud");
	});

	public static final Event<AfterRender> AFTER_RENDER = EventFactory.createArrayBacked(AfterRender.class, callbacks -> (client, chatHud, inGameHud, matrices, tickDelta, ticks) -> {
		RenderEventFactory.invokeAfterEvent(callbacks, callback -> callback.onRender(client, chatHud, inGameHud, matrices, tickDelta, ticks),
				client.getProfiler()::push, client.getProfiler()::pop, matrices::push, matrices::pop, "fabricAfterRenderChatHud");
	});

	public interface Render {
		boolean onRender(MinecraftClient client, ChatHud chatHud, InGameHud inGameHud, MatrixStack matrices, float tickDelta, int ticks);
	}

	public interface AfterRender {
		void onRender(MinecraftClient client, ChatHud chatHud, InGameHud inGameHud, MatrixStack matrices, float tickDelta, int ticks);
	}

	private RenderChatHudEvent() {
	}
}
