package me.i509.junkkyard.entity.mixin;

import java.util.Set;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.TeleportCommand;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import me.i509.junkkyard.entity.EntityChangeWorldEvents;

@Mixin(TeleportCommand.class)
public abstract class TeleportCommandMixin {
	@Shadow @Final private static SimpleCommandExceptionType INVALID_POSITION_EXCEPTION;
	@Unique
	private static boolean fabric_cancelWorldChangeEvent = false;
	@Unique
	private static EntityChangeWorldEvents.TeleportData fabric_teleportData = null;

	@ModifyVariable(method = "teleport", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;detach()V"))
	private static ServerWorld fireWorldChangeEvent(ServerWorld destination, ServerCommandSource source, Entity target, ServerWorld world, double x, double y, double z, Set<PlayerPositionLookS2CPacket.Flag> movementFlags, float yaw, float pitch) {
		final TypedActionResult<ServerWorld> worldResult = EntityChangeWorldEvents.BEFORE_CHANGE.invoker().beforeChangeDimension(target, (ServerWorld) target.world, destination);

		if (worldResult.getResult() == ActionResult.FAIL) {
			fabric_cancelWorldChangeEvent = true;
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

	@Inject(method = "teleport", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;detach()V"))
	private static void cancelTeleportEvent(CallbackInfo ci) {
		if (fabric_cancelWorldChangeEvent) {
			fabric_cancelWorldChangeEvent = false;
			ci.cancel();
		}
	}

	@Inject(method = "teleport", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;refreshPositionAndAngles(DDDFF)V", args = "log=true"), locals = LocalCapture.CAPTURE_FAILEXCEPTION,
		slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityType;create(Lnet/minecraft/world/World;)Lnet/minecraft/entity/Entity;")))
	private static void modifyDestination(ServerCommandSource source, Entity target, ServerWorld destination, double x, double y, double z, Set<PlayerPositionLookS2CPacket.Flag> movementFlags, float yaw, float pitch, @Coerce Object facingLocation, CallbackInfo ci, float wrappedYaw, float wrappedPitch, Entity entity) throws CommandSyntaxException {
		final EntityChangeWorldEvents.TeleportData teleport = new EntityChangeWorldEvents.TeleportData(x, y, z, yaw, pitch);
		EntityChangeWorldEvents.CHANGE.invoker().onChangeWorld(target, (ServerWorld) entity.world, destination, teleport);

		if (!World.method_25953(new BlockPos(teleport.getX(), teleport.getY(), teleport.getZ()))) {
			throw INVALID_POSITION_EXCEPTION.create(); // Clamp to default teleport command values
		}

		fabric_teleportData = teleport;
	}

	@ModifyArgs(method = "teleport", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;refreshPositionAndAngles(DDDFF)V", ordinal = 1))
	private static void modifyPosAndAngles(Args args) {
		final EntityChangeWorldEvents.TeleportData teleport = fabric_teleportData;
		args.setAll(teleport.getX(), teleport.getY(), teleport.getZ(), teleport.getYaw(), teleport.getPitch());
	}

	@ModifyArg(method = "teleport", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setHeadYaw(F)V", ordinal = 1))
	private static float modifyHeadYaw(float original) {
		float headYaw = fabric_teleportData.getYaw();
		fabric_teleportData = null;
		return headYaw;
	}
}
