package me.i509.junkkyard.entity;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;

public class EntityChangeWorldEvents {
	public static final Event<BeforeChangeWorld> BEFORE_CHANGE = EventFactory.createArrayBacked(BeforeChangeWorld.class, callbacks -> (entity, origin, destination) -> {
		TypedActionResult<ServerWorld> result = null;

		if (EventFactory.isProfilingEnabled()) {
			final Profiler profiler = origin.getProfiler();
			profiler.push("fabricBeforeEntityChangeWorld");

			for (BeforeChangeWorld callback : callbacks) {
				profiler.push(EventFactory.getHandlerName(callback));
				result = callback.beforeChangeDimension(entity, origin, destination);

				if (result.getResult() == ActionResult.FAIL) {
					profiler.pop();
					return result;
				}

				if (result.getResult() == ActionResult.SUCCESS) {
					profiler.pop();
					return result;
				}

				profiler.pop();
			}

			profiler.pop();
		} else {
			for (BeforeChangeWorld callback : callbacks) {
				result = callback.beforeChangeDimension(entity, origin, destination);

				if (result.getResult() == ActionResult.FAIL) {
					return result;
				}

				if (result.getResult() == ActionResult.SUCCESS) {
					return result;
				}
			}
		}

		return result != null ? result : TypedActionResult.pass(destination); // Fall back to pass if we got no result for some weird chance
	});

	public static final Event<ChangeWorld> CHANGE = EventFactory.createArrayBacked(ChangeWorld.class, callbacks -> (entity, origin, destination, teleport) -> {
		if (EventFactory.isProfilingEnabled()) {
			final Profiler profiler = entity.getEntityWorld().getProfiler();
			profiler.push("fabricEntityChangeWorld");

			for (ChangeWorld callback : callbacks) {
				profiler.push(EventFactory.getHandlerName(callback));
				callback.onChangeWorld(entity, origin, destination, teleport);
				profiler.pop();
			}

			profiler.pop();
		} else {
			for (ChangeWorld callback : callbacks) {
				callback.onChangeWorld(entity, origin, destination, teleport);
			}
		}
	});

	public interface BeforeChangeWorld {
		/**
		 * @param entity      the entity being teleported
		 * @param origin      the origin world of this teleportation
		 * @param destination the destination of this teleportation
		 * @return <ul>
		 * <li>{@link TypedActionResult#success(Object)} -> Stop event execution.
		 * The entity will be teleported to the world passed in the result.
		 * <li>{@link TypedActionResult#pass(Object)} -> Continue event execution.
		 * <li>{@link TypedActionResult#consume(Object)} -> Continue event execution.
		 * Functions the same as pass.
		 * <li>{@link TypedActionResult#fail(Object)} -> Stop event execution.
		 * The teleportation will not occur.
		 * </ul>
		 */
		TypedActionResult<ServerWorld> beforeChangeDimension(Entity entity, ServerWorld origin, ServerWorld destination);
	}

	public interface ChangeWorld {
		/**
		 * @param entity      the entity being teleported
		 * @param origin      the origin world of this teleportation
		 * @param destination the destination of this teleportation
		 * @param teleport the destination the entity will be placed at and facing towards. Facing is specifed as pitch, yaw
		 * @return <ul>
		 * <li>{@link TypedActionResult#success(Object)} -> Stop event execution.
		 * The entity will be teleported to the world passed in the result.
		 * <li>{@link TypedActionResult#pass(Object)} -> Continue event execution.
		 * <li>{@link TypedActionResult#consume(Object)} -> Continue event execution.
		 * Functions the same as pass.
		 * <li>{@link TypedActionResult#fail(Object)} -> Stop event execution.
		 * The teleportation will not occur.
		 * </ul>
		 */
		void onChangeWorld(Entity entity, ServerWorld origin, ServerWorld destination, TeleportData teleport);
	}

	public interface AfterChangeWorld {
		void afterChangeWorld(Entity oldEntity, Entity newEntity);
	}

	public static final class TeleportData {
		private final double originalX;
		private final double originalY;
		private final double originalZ;
		private final float originalYaw;
		private final float originalPitch;
		private double x;
		private double y;
		private double z;
		private float yaw;
		private float pitch;

		public TeleportData(BlockPos pos, float yaw, float pitch) {
			this(pos.getX(), pos.getY(), pos.getZ(), yaw, pitch);
		}

		public TeleportData(double originalX, double originalY, double originalZ, float originalYaw, float originalPitch) {
			this.originalX = originalX;
			this.originalY = originalY;
			this.originalZ = originalZ;
			this.originalYaw = originalYaw;
			this.originalPitch = originalPitch;
			this.x = originalX;
			this.y = originalY;
			this.z = originalZ;
			this.yaw = originalYaw;
			this.pitch = originalPitch;
		}

		public double getOriginalX() {
			return this.originalX;
		}

		public double getOriginalY() {
			return this.originalY;
		}

		public double getOriginalZ() {
			return this.originalZ;
		}

		public float getOriginalYaw() {
			return this.originalYaw;
		}

		public float getOriginalPitch() {
			return this.originalPitch;
		}

		public double getX() {
			return this.x;
		}

		public void setX(double x) {
			this.x = x;
		}

		public double getY() {
			return this.y;
		}

		public void setY(double y) {
			this.y = y;
		}

		public double getZ() {
			return this.z;
		}

		public void setZ(double z) {
			this.z = z;
		}

		public float getYaw() {
			return this.yaw;
		}

		public void setYaw(float yaw) {
			this.yaw = yaw;
		}

		public float getPitch() {
			return this.pitch;
		}

		public void setPitch(float pitch) {
			this.pitch = pitch;
		}
	}
}
