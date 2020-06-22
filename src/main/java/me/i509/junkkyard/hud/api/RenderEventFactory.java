package me.i509.junkkyard.hud.api;

import java.util.function.Consumer;
import java.util.function.Function;

import net.fabricmc.fabric.api.event.EventFactory;

final class RenderEventFactory {
	static <T> boolean invokeBeforeRenderEvent(T[] callbacks, Function<T, Boolean> invokerFunction, Consumer<String> pushProfiler, Runnable popProfiler, Runnable pushHandler, Runnable popHandler, String profilerName) {
		boolean shouldRender = true;

		if (EventFactory.isProfilingEnabled()) {
			pushProfiler.accept(profilerName);

			for (T callback : callbacks) {
				pushProfiler.accept(EventFactory.getHandlerName(callback));
				pushHandler.run();

				final boolean result = invokerFunction.apply(callback);

				if (!result) {
					shouldRender = false;
				}

				popHandler.run();
				popProfiler.run();
			}

			popProfiler.run();
		} else {
			for (T callback : callbacks) {
				pushHandler.run();

				final boolean result = invokerFunction.apply(callback);

				if (!result) {
					shouldRender = false;
				}

				popHandler.run();
			}
		}

		return shouldRender;
	}

	static <T> void invokeAfterEvent(T[] callbacks, Consumer<T> invoker, Consumer<String> pushProfiler, Runnable popProfiler, Runnable pushHandler, Runnable popHandler, String profilerName) {
		if (EventFactory.isProfilingEnabled()) {
			pushProfiler.accept(profilerName);

			for (T callback : callbacks) {
				pushProfiler.accept(EventFactory.getHandlerName(callback));
				pushHandler.run();

				invoker.accept(callback);

				popHandler.run();
				popProfiler.run();
			}

			popProfiler.run();
		} else {
			for (T callback : callbacks) {
				pushHandler.run();

				invoker.accept(callback);

				popHandler.run();
			}
		}
	}
}
