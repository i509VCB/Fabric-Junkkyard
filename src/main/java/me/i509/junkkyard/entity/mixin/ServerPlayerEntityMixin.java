package me.i509.junkkyard.entity.mixin;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.PortalForcer;

import me.i509.junkkyard.entity.EntityChangeWorldEvents;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin extends EntityMixin {
	@Shadow
	public abstract ServerWorld getServerWorld();

	@Inject(method = "teleport", at = @At("TAIL"))
	private void fireAfterChangeWorld(ServerWorld targetWorld, double x, double y, double z, float yaw, float pitch, CallbackInfo ci) {
		// TODO: Fire after world change
	}

	@Inject(method = "teleport", at = @At("TAIL"))
	private void fireAfterTeleport(ServerWorld targetWorld, double x, double y, double z, float yaw, float pitch, CallbackInfo ci) {
		if (targetWorld != this.world) {
			// TODO: Fire After world change
		}
	}

	// TODO: Old code below

	@Unique
	private boolean portalForcerDimensionChangedEventFired;

	@Unique
	private EntityChangeWorldEvents.TeleportData teleportData;

	// changeDimension method

	@ModifyVariable(method = "changeDimension", at = @At("HEAD"), argsOnly = true)
	private ServerWorld fireBeforeWorldChangeEvent(ServerWorld destination) {
		final TypedActionResult<ServerWorld> worldResult = EntityChangeWorldEvents.BEFORE_CHANGE.invoker().beforeChangeDimension((Entity) (Object) this, this.getServerWorld(), destination);

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

	// Cancel dimension change if necessary.
	@Inject(method = "changeDimension", at = @At(value = "FIELD", target = "Lnet/minecraft/server/network/ServerPlayerEntity;inTeleportationState:Z"), cancellable = true)
	private void afterDimensionEvent(ServerWorld destination, CallbackInfoReturnable<Entity> cir) {
		if (this.fabric_cancelWorldChangeEvent) {
			this.fabric_cancelWorldChangeEvent = false;
			cir.setReturnValue((Entity) (Object) this);
		}
	}

	// Can occur if the player is heading to the end.
	@Redirect(method = "changeDimension", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;refreshPositionAndAngles(DDDFF)V"),
			slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;method_29200(Lnet/minecraft/server/world/ServerWorld;)V")))
	private void modifyDimensionChangeToEnd(ServerPlayerEntity player, double x, double y, double z, float yaw, float pitch, ServerWorld destination) {
		final EntityChangeWorldEvents.TeleportData teleport = new EntityChangeWorldEvents.TeleportData(x, y, z, yaw, pitch);

		EntityChangeWorldEvents.CHANGE.invoker().onChangeWorld(player, (ServerWorld) player.world, destination, teleport);
		player.refreshPositionAndAngles(teleport.getX(), teleport.getY(), teleport.getZ(), teleport.getYaw(), teleport.getPitch());
	}

	// Normally occurs when using the portal forcer
	@Redirect(method = "changeDimension", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/PortalForcer;usePortal(Lnet/minecraft/entity/Entity;F)Z"))
	private boolean modifyDimensionChange(PortalForcer portalForcer, Entity entity, float yawOffset, ServerWorld destination) {
		boolean success = portalForcer.usePortal(entity, yawOffset);

		if (success) { // If successful, dispatch
			this.portalForcerDimensionChangedEventFired = true;
			final EntityChangeWorldEvents.TeleportData teleport = new EntityChangeWorldEvents.TeleportData(entity.getX(), entity.getY(), entity.getZ(), entity.yaw, entity.pitch);
			// entity.world is origin since setWorld call has not occured yet
			EntityChangeWorldEvents.CHANGE.invoker().onChangeWorld(entity, this.getServerWorld(), destination, teleport);

			// Apply the teleport
			entity.refreshPositionAndAngles(teleport.getX(), teleport.getY(), teleport.getZ(), teleport.getYaw(), teleport.getPitch());
		}

		return success;
	}

	// This case can sometimes occur if the portal forcer returns false twice. If it does, dispatch this event
	@Inject(method = "changeDimension", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/PortalForcer;usePortal(Lnet/minecraft/entity/Entity;F)Z", ordinal = 1, shift = At.Shift.AFTER))
	private void modifyDimensionChangePortalFailure(ServerWorld destination, CallbackInfoReturnable<Entity> cir) {
		if (!this.portalForcerDimensionChangedEventFired) {
			final EntityChangeWorldEvents.TeleportData teleport = new EntityChangeWorldEvents.TeleportData(this.getX(), this.getY(), this.getZ(), this.yaw, this.pitch);

			EntityChangeWorldEvents.CHANGE.invoker().onChangeWorld((Entity) (Object) this, this.getServerWorld(), destination, teleport);
			this.refreshPositionAndAngles(teleport.getX(), teleport.getY(), teleport.getZ(), teleport.getYaw(), teleport.getPitch());
		}

		this.portalForcerDimensionChangedEventFired = false; // Clear it so we don't break the next dimension change
	}

	/*
	 * For teleport method.
	 */

	@ModifyVariable(method = "teleport", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;getServerWorld()Lnet/minecraft/server/world/ServerWorld;"), argsOnly = true, index = 1)
	private ServerWorld onTeleport(ServerWorld destination) {
		final TypedActionResult<ServerWorld> worldResult = EntityChangeWorldEvents.BEFORE_CHANGE.invoker().beforeChangeDimension((Entity) (Object) this, this.getServerWorld(), destination);

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

	@Inject(method = "teleport", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;getServerWorld()Lnet/minecraft/server/world/ServerWorld;", shift = At.Shift.AFTER), cancellable = true)
	private void cancelTeleportDimensionChangeEvent(ServerWorld destination, double x, double y, double z, float yaw, float pitch, CallbackInfo ci) {
		if (this.fabric_cancelWorldChangeEvent) {
			this.fabric_cancelWorldChangeEvent = false;
			ci.cancel();
		}
	}

	@Inject(method = "teleport", at = @At(value = "FIELD", target = "Lnet/minecraft/server/network/ServerPlayerEntity;removed:Z", opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER))
	private void modifyDestinationPosition(ServerWorld destination, double x, double y, double z, float yaw, float pitch, CallbackInfo ci) {
		final EntityChangeWorldEvents.TeleportData teleport = new EntityChangeWorldEvents.TeleportData(x, y, z, yaw, pitch);
		EntityChangeWorldEvents.CHANGE.invoker().onChangeWorld((Entity) (Object) this, this.getServerWorld(), destination, teleport);

		this.teleportData = teleport;
	}

	@ModifyArgs(method = "teleport", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayerEntity;refreshPositionAndAngles(DDDFF)V"))
	private void modifyPosAndAngles(Args args) {
		final EntityChangeWorldEvents.TeleportData teleport = this.teleportData;
		args.setAll(teleport.getX(), teleport.getY(), teleport.getZ(), teleport.getYaw(), teleport.getPitch());
	}

	@ModifyArgs(method = "teleport", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;requestTeleport(DDDFF)V", ordinal = 1))
	private void modifyTeleportRequest(Args args) {
		final EntityChangeWorldEvents.TeleportData teleport = this.teleportData;
		args.setAll(teleport.getX(), teleport.getY(), teleport.getZ(), teleport.getYaw(), teleport.getPitch());
	}

	@Inject(method = "teleport", at = @At("TAIL"))
	private void clearTeleportData(ServerWorld destination, double x, double y, double z, float yaw, float pitch, CallbackInfo ci) {
		this.teleportData = null;
	}
}
