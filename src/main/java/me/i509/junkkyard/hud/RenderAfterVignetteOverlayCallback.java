package me.i509.junkkyard.hud;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.profiler.Profiler;

import me.i509.junkkyard.hud.mixin.InGameHudAccessor;

public interface RenderAfterVignetteOverlayCallback {
	Event<RenderAfterVignetteOverlayCallback> EVENT = EventFactory.createArrayBacked(RenderAfterVignetteOverlayCallback.class, callbacks -> (hud, matrices, cameraEntity, tickDelta) -> {
		if (EventFactory.isProfilingEnabled()) {
			final MinecraftClient client = ((InGameHudAccessor) hud).getClient();
			final Profiler profiler = client.getProfiler();
			profiler.push("fabricHud_RenderAfterVignetteOverlay");

			for (RenderAfterVignetteOverlayCallback callback : callbacks) {
				profiler.push(EventFactory.getHandlerName(callback));
				callback.onRender(hud, matrices, cameraEntity, tickDelta);
				profiler.pop();
			}

			profiler.pop();
		} else {
			for (RenderAfterVignetteOverlayCallback callback : callbacks) {
				callback.onRender(hud, matrices, cameraEntity, tickDelta);
			}
		}
	});

	void onRender(final InGameHud hud, final MatrixStack matrices, final Entity cameraEntity, final float tickDelta);
}
