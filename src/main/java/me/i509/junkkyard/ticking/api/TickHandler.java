package me.i509.junkkyard.ticking.api;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;

/**
 * A handler of a ticking event.
 *
 * @param <S> the type of the subject being ticked
 */
public interface TickHandler<S> {
	/**
	 * Called when an applicable subject is created.
	 * This determines whether this ticking event should apply to a subject.
	 *
	 * @return If {@code true}, this event will apply to the subject of the ticking event.
	 * If {@code false}, this tick handler will not be called on the subject.
	 */
	boolean isAcceptable(S subject);

	/**
	 * Called before a subject's {@code tick} method is called.
	 * Because this method is called every tick, implementations should limit the amount of operations run every call, and avoid expensive computations.
	 *
	 * <p>This method will only be called if the {@link TickHandler#isAcceptable(Object) subject is acceptable}.
	 *
	 * @param subject the subject
	 */
	void beforeTick(S subject);

	/**
	 * Called after a subject's {@code tick} method is called.
	 * Because this method is called every tick, implementations should limit the amount of operations run every call, and avoid expensive computations.
	 *
	 * <p>This method will only be called if the {@link TickHandler#isAcceptable(Object) subject is acceptable}.
	 *
	 * @param subject the subject
	 */
	void afterTick(S subject);

	interface EntityHandler extends TickHandler<Entity> {
	}

	interface BlockEntityHandler extends TickHandler<BlockEntity> {
	}
}
