package me.i509.junkkyard.render.api;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Consumer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;

import me.i509.junkkyard.render.impl.CascadingEvent;

@Environment(EnvType.CLIENT)
public final class RegisterFeatureRendererEvent {
	private static final Map<Class<?>, CascadingEvent<?>> REGISTER_EVENTS = new IdentityHashMap<>();

	public static <T extends LivingEntity, M extends EntityModel<T>, R extends LivingEntityRenderer<T, M>> Event<Register<T, M, R>> event(Class<? extends R> rendererClass) {
		return RegisterFeatureRendererEvent.getOrCreateRegisterEvent(rendererClass);
	}

	private static <T extends LivingEntity, M extends EntityModel<T>, R extends LivingEntityRenderer<T, M>> CascadingEvent<Register<T, M, R>> getOrCreateRegisterEvent(Class<? extends R> rendererClass) {
		if (!LivingEntityRenderer.class.isAssignableFrom(rendererClass)) {
			throw new IllegalArgumentException(String.format("Cannot register feature renderer register event to non-living-entity-renderer class. Found: %s", rendererClass));
		}

		//noinspection unchecked
		return (CascadingEvent<Register<T, M, R>>) RegisterFeatureRendererEvent.REGISTER_EVENTS.computeIfAbsent(rendererClass, clazz -> {
			final CascadingEvent<Register<T, M, R>> event = new CascadingEvent<>(RegisterFeatureRendererEvent.createRegisterEvent());

			if (clazz != LivingEntityRenderer.class) { // LivingEntityRenderer cannot inherit callbacks due to being where features are defined.
				//noinspection unchecked
				final Class<R> superclass = (Class<R>) clazz.getSuperclass();
				RegisterFeatureRendererEvent.getOrCreateRegisterEvent(superclass).registerDescendant(event);
			}

			return event;
		});
	}

	private static <T extends LivingEntity, M extends EntityModel<T>, R extends LivingEntityRenderer<T, M>> Event<Register<T, M, R>> createRegisterEvent() {
		return EventFactory.createArrayBacked(Register.class, callbacks -> (entityRenderer, acceptor) -> {
			for (Register<T, M, R> callback : callbacks) {
				callback.registerFeatureRenderers(entityRenderer, acceptor);
			}
		});
	}

	@FunctionalInterface
	public interface Register<T extends LivingEntity, M extends EntityModel<T>, R extends LivingEntityRenderer<T, M>> {
		void registerFeatureRenderers(R entityRenderer, Consumer<FeatureRenderer<T, M>> acceptor);
	}
}
