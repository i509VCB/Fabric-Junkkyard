package me.i509.junkkyard.hud;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.profiler.Profiler;

import me.i509.junkkyard.hud.mixin.InGameHudAccessor;

public interface RenderAfterCrosshairCallback {
	Event<RenderAfterCrosshairCallback> EVENT = EventFactory.createArrayBacked(RenderAfterCrosshairCallback.class, callbacks -> (hud, matrices, tickDelta) -> {
		if (EventFactory.isProfilingEnabled()) {
			final MinecraftClient client = ((InGameHudAccessor) hud).getClient();
			final Profiler profiler = client.getProfiler();
			profiler.push("fabricHud_RenderAfterCrosshair");

			for (RenderAfterCrosshairCallback callback : callbacks) {
				profiler.push(EventFactory.getHandlerName(callback));
				callback.onRender(hud, matrices, tickDelta);
				profiler.pop();
			}

			profiler.pop();
		} else {
			for (RenderAfterCrosshairCallback callback : callbacks) {
				callback.onRender(hud, matrices, tickDelta);
			}
		}
	});

	void onRender(final InGameHud hud, final MatrixStack matrices, final float tickDelta);
}
