package me.i509.junkkyard.hud;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.profiler.Profiler;

import me.i509.junkkyard.hud.mixin.InGameHudAccessor;

public interface RenderHeldItemTooltipCallback {
	Event<RenderHeldItemTooltipCallback> EVENT = EventFactory.createArrayBacked(RenderHeldItemTooltipCallback.class, callbacks -> (hud, matrices, tickDelta) -> {
		boolean shouldRender = true;

		if (EventFactory.isProfilingEnabled()) {
			final MinecraftClient client = ((InGameHudAccessor) hud).getClient();
			final Profiler profiler = client.getProfiler();
			profiler.push("fabricHud_RenderHeldItemTooltip");

			for (RenderHeldItemTooltipCallback callback : callbacks) {
				profiler.push(EventFactory.getHandlerName(callback));
				final boolean result = callback.onRender(hud, matrices, tickDelta);

				if (!result) {
					shouldRender = false;
				}

				profiler.pop();
			}

			profiler.pop();
		} else {
			for (RenderHeldItemTooltipCallback callback : callbacks) {
				final boolean result = callback.onRender(hud, matrices, tickDelta);

				if (!result) {
					shouldRender = false;
				}
			}
		}

		return shouldRender;
	});

	boolean onRender(final InGameHud hud, final MatrixStack matrices, final float tickDelta);
}
