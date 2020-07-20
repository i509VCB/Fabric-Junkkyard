package me.i509.junkkyard.entity;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Objects;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.WorldPosition;
import net.minecraft.world.World;

/**
 * Represents a transformation containing a world, position and rotation.
 * @param <W> the type of world
 */
public final class Transform<W extends World> implements WorldPosition {
	private final W world;
	private final Vec3d pos;
	private final float yaw;
	private final float pitch;

	public static Transform<World> from(Entity entity) {
		Objects.requireNonNull(entity, "Cannot create transform from null entity");

		return of(entity.world, entity.getPos(), entity.yaw, entity.pitch);
	}

	public static Transform<ServerWorld> from(ServerPlayerEntity player) {
		Objects.requireNonNull(player, "Cannot create transform from null player");

		return of(player.getServerWorld(), player.getPos(), player.yaw, player.pitch);
	}

	public static <W extends World> Transform<W> of(W world, Vec3d pos, float yaw, float pitch) {
		return new Transform<>(world, pos, yaw, pitch);
	}

	private Transform(W world, Vec3d pos, float yaw, float pitch) {
		this.world = world;
		this.pos = pos;
		this.yaw = yaw;
		this.pitch = pitch;
	}

	@Override
	public W getWorld() {
		return this.world;
	}

	@Override
	public double getX() {
		return this.pos.getX();
	}

	@Override
	public double getY() {
		return this.pos.getY();
	}

	@Override
	public double getZ() {
		return this.pos.getZ();
	}

	public Vec3d getPos() {
		return this.pos;
	}

	public float getYaw() {
		return this.yaw;
	}

	public float getPitch() {
		return this.pitch;
	}

	public <N extends World> Transform<N> with(N world) {
		Objects.requireNonNull(world, "Cannot create transform with null world.");

		return new Transform<>(world, this.getPos(), this.yaw, this.pitch);
	}

	public Transform<W> with(Vec3d pos) {
		Objects.requireNonNull(pos, "Cannot create transform with null position.");

		return new Transform<>(this.getWorld(), pos, this.yaw, this.pitch);
	}

	public Transform<W> with(float yaw, float pitch) {
		return new Transform<>(this.getWorld(), this.getPos(), yaw, pitch);
	}

	public Transform<W> withYaw(float yaw) {
		return new Transform<>(this.getWorld(), this.getPos(), yaw, this.pitch);
	}

	public Transform<W> withPitch(float pitch) {
		return new Transform<>(this.getWorld(), this.getPos(), this.yaw, pitch);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Transform)) return false;

		final Transform<?> that = (Transform<?>) o;

		// Validate pos and rotation
		if (!this.getPos().equals(that.getPos())) return false;
		if (this.getYaw() != that.getYaw()) return false;
		if (this.getPitch() != that.getPitch()) return false;

		// Fail if the other world is not on the same logical side, then check the registry key
		if (this.getWorld().isClient() != that.getWorld().isClient()) return false;
		if (this.getWorld().getRegistryKey() != that.getWorld().getRegistryKey()) return false;

		// Now the world test
		return this.getWorld().equals(that.getWorld());
	}

	@Override
	public int hashCode() {
		int result = this.getWorld().hashCode();
		result = 31 * result + this.getPos().hashCode();
		result = 31 * result + (this.getYaw() != 0.0f ? Float.floatToIntBits(this.getYaw()) : 0);
		result = 31 * result + (this.getPitch() != 0.0f ? Float.floatToIntBits(this.getPitch()) : 0);
		return result;
	}
}
