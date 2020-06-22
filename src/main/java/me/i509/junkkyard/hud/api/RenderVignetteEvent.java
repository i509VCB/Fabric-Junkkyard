package me.i509.junkkyard.hud.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public final class RenderVignetteEvent {
	public static final Event<Render> RENDER = EventFactory.createArrayBacked(Render.class, callbacks -> (client, hud, matrices, cameraEntity, tickDelta) -> {
		return RenderEventFactory.invokeBeforeRenderEvent(callbacks, callback -> callback.onRender(client, hud, matrices, cameraEntity, tickDelta),
				client.getProfiler()::push, client.getProfiler()::pop, matrices::push, matrices::pop, "fabricRenderVignette");
	});

	public static final Event<AfterRender> AFTER_RENDER = EventFactory.createArrayBacked(AfterRender.class, callbacks -> (client, hud, matrices, cameraEntity, tickDelta) -> {
		RenderEventFactory.invokeAfterEvent(callbacks, callback -> callback.onRender(client, hud, matrices, cameraEntity, tickDelta),
				client.getProfiler()::push, client.getProfiler()::pop, matrices::push, matrices::pop, "fabricAfterRenderVignette");
	});

	public interface Render {
		boolean onRender(MinecraftClient client, InGameHud hud, MatrixStack matrices, Entity cameraEntity, float tickDelta);
	}

	public interface AfterRender {
		void onRender(MinecraftClient client, InGameHud hud, MatrixStack matrices, Entity cameraEntity, float tickDelta);
	}

	private RenderVignetteEvent() {
	}
}
