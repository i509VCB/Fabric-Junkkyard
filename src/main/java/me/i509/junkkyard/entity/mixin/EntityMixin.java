package me.i509.junkkyard.entity.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import me.i509.junkkyard.entity.EntityChangeWorldEvents;

@Mixin(Entity.class)
public abstract class EntityMixin {
	@Shadow
	public World world;
	@Shadow
	public boolean removed;
	@Shadow
	public float yaw;
	@Shadow
	public float pitch;
	@Shadow
	public abstract double getX();
	@Shadow
	public abstract double getY();
	@Shadow
	public abstract double getZ();
	@Shadow
	public abstract void refreshPositionAndAngles(double x, double y, double z, float yaw, float pitch);

	@Unique
	protected boolean fabric_cancelWorldChangeEvent;

	@ModifyVariable(method = "changeDimension", at = @At("HEAD"), argsOnly = true)
	private ServerWorld beforeDimensionChange(ServerWorld destination) {
		if (!(this.world instanceof ServerWorld) || this.removed) {
			return destination; // Fallback to vanilla logic
		}

		final TypedActionResult<ServerWorld> worldResult = EntityChangeWorldEvents.BEFORE_CHANGE.invoker().beforeChangeDimension((Entity) (Object) this, (ServerWorld) this.world, destination);

		if (worldResult.getResult() == ActionResult.FAIL) {
			this.fabric_cancelWorldChangeEvent = true;
			return destination; // Return destination, will be cancelled momentarily
		}

		if (worldResult.getResult() == ActionResult.SUCCESS) {
			// Change the destination, make sure we don't get passed null.
			if (worldResult.getValue() != null) {
				return worldResult.getValue();
			}
		}

		// PASS and CONSUME just fall back to vanilla logic
		return destination;
	}

	@Inject(method = "changeDimension", at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/util/profiler/Profiler;push(Ljava/lang/String;)V", args = "ldc=changeDimension"), cancellable = true)
	private void cancelWorldChangeEvent(ServerWorld serverWorld, CallbackInfoReturnable<Entity> cir) {
		if (this.fabric_cancelWorldChangeEvent) {
			this.fabric_cancelWorldChangeEvent = false;
			cir.setReturnValue((Entity) (Object) this);
		}
	}

	@Redirect(method = "changeDimension", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;refreshPositionAndAngles(Lnet/minecraft/util/math/BlockPos;FF)V"))
	private void afterDimensionChange(Entity entity, BlockPos pos, float yaw, float pitch, ServerWorld destination) {
		final EntityChangeWorldEvents.TeleportData teleport = new EntityChangeWorldEvents.TeleportData(pos, yaw, pitch);
		// "this" refers to the original entity.
		EntityChangeWorldEvents.CHANGE.invoker().onChangeWorld(entity, (ServerWorld) this.world, (ServerWorld) entity.world, teleport);

		// Remember, the facing result is pitch, yaw. So we reverse those params
		entity.refreshPositionAndAngles(teleport.getX(), teleport.getY(), teleport.getZ(), teleport.getYaw(), teleport.getPitch());
	}
}
