package me.i509.junkkyard.hud.api;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;

public final class RenderPumpkinOverlayEvent {
	public static final Event<Render> RENDER = EventFactory.createArrayBacked(Render.class, callbacks -> (client, hud, matrices, tickDelta) -> {
		return RenderEventFactory.invokeBeforeRenderEvent(callbacks, callback -> callback.onRender(client, hud, matrices, tickDelta),
				client.getProfiler()::push, client.getProfiler()::pop, matrices::push, matrices::pop, "fabricRenderPumpkinOverlay");
	});

	public static final Event<AfterRender> AFTER_RENDER = EventFactory.createArrayBacked(AfterRender.class, callbacks -> (client, hud, matrices, tickDelta) -> {
		RenderEventFactory.invokeAfterEvent(callbacks, callback -> callback.onRender(client, hud, matrices, tickDelta),
				client.getProfiler()::push, client.getProfiler()::pop, matrices::push, matrices::pop, "fabricAfterRenderPumpkinOverlay");
	});

	public interface Render {
		boolean onRender(MinecraftClient client, InGameHud hud, MatrixStack matrices, float tickDelta);
	}

	public interface AfterRender {
		void onRender(MinecraftClient client, InGameHud hud, MatrixStack matrices, float tickDelta);
	}

	private RenderPumpkinOverlayEvent() {
	}
}
