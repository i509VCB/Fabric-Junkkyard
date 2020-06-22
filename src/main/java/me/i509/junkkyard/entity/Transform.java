package me.i509.junkkyard.entity;

import static com.google.common.base.Preconditions.checkNotNull;

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
	private final Vec2f rotation;

	public static Transform<World> from(Entity entity) {
		checkNotNull(entity, "Cannot create transform from null entity");

		return of(entity.world, entity.getPos(), new Vec2f(entity.yaw, entity.pitch));
	}

	public static Transform<ServerWorld> from(ServerPlayerEntity player) {
		checkNotNull(player, "Cannot create transform from null player");

		return of(player.getServerWorld(), player.getPos(), new Vec2f(player.yaw, player.pitch));
	}

	public static <W extends World> Transform<W> of(W world, Vec3d pos, float yaw, float pitch) {
		return of(world, pos, new Vec2f(yaw, pitch));
	}

	public static <W extends World> Transform<W> of(W world, Vec3d pos, Vec2f rotation) {
		checkNotNull(world, "Cannot create transform from null world");
		checkNotNull(pos, "Cannot create transform with null position");
		checkNotNull(rotation, "Cannot create transform with no rotation");

		return new Transform<>(world, pos, rotation);
	}

	private Transform(W world, Vec3d pos, Vec2f rotation) {
		this.world = world;
		this.pos = pos;
		this.rotation = rotation;
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

	public Vec2f getRotation() {
		return this.rotation;
	}

	public float getYaw() {
		return this.rotation.x;
	}

	public float getPitch() {
		return this.rotation.y;
	}

	public <N extends World> Transform<N> with(N world) {
		checkNotNull(world, "Cannot create transform with null world.");

		return new Transform<>(world, this.getPos(), this.getRotation());
	}

	public Transform<W> with(Vec3d pos) {
		checkNotNull(pos, "Cannot create transform with null position.");

		return new Transform<>(this.getWorld(), pos, this.getRotation());
	}

	public Transform<W> with(Vec2f rotation) {
		checkNotNull(rotation, "Cannot create transform with null rotation.");

		return new Transform<>(this.getWorld(), this.getPos(), rotation);
	}

	public Transform<W> with(float yaw, float pitch) {
		return this.with(new Vec2f(yaw, pitch));
	}

	public Transform<W> withYaw(float yaw) {
		return this.with(new Vec2f(yaw, this.getRotation().y));
	}

	public Transform<W> withPitch(float pitch) {
		return this.with(new Vec2f(this.getRotation().x, pitch));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Transform)) return false;

		final Transform<?> that = (Transform<?>) o;

		// Validate pos and rotation
		if (!this.getPos().equals(that.getPos())) return false;
		if (!this.getRotation().equals(that.getRotation())) return false;

		// Fail if the other world is not on the same logical side, then check the registry key
		if (this.getWorld().isClient() != that.getWorld().isClient()) return false;
		if (this.getWorld().getRegistryKey() != that.getWorld().getRegistryKey()) return false;

		// Now the world test
		return this.getWorld().equals(that.getWorld());
	}

	@Override
	public int hashCode() {
		int result = this.getWorld().getRegistryKey().hashCode();
		result = 31 * result + System.identityHashCode(this.getWorld().isClient());
		result = 31 * result + this.getPos().hashCode();
		result = 31 * result + this.getRotation().hashCode();
		return result;
	}
}
