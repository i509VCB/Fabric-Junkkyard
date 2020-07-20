package me.i509.junkkyard.entity.mixin;

import java.net.SocketAddress;
import java.util.Optional;

import com.mojang.authlib.GameProfile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldProperties;

import me.i509.junkkyard.entity.PlayerLoginCallback;
import me.i509.junkkyard.entity.PlayerRespawnEvents;
import me.i509.junkkyard.entity.impl.LoginContextImpl;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {
	@Inject(method = "respawnPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/s2c/play/PlayerRespawnS2CPacket;<init>(Lnet/minecraft/util/registry/RegistryKey;Lnet/minecraft/util/registry/RegistryKey;JLnet/minecraft/world/GameMode;Lnet/minecraft/world/GameMode;ZZZ)V"), locals = LocalCapture.PRINT)
	private void fireRespawnEvent(ServerPlayerEntity oldPlayer, boolean alive, CallbackInfoReturnable<ServerPlayerEntity> cir, BlockPos spawnPoint, boolean spawnPointSet, ServerWorld spawnPointWorld, Optional<Vec3d> respawnPosition, ServerPlayerInteractionManager interactionManager, ServerWorld destination, ServerPlayerEntity newPlayer, boolean isRespawnAnchor, WorldProperties oldWorldProperties) {
		PlayerRespawnEvents.AFTER_RESPAWN.invoker().onPlayerRespawn(oldPlayer, newPlayer, alive);
	}

	@Inject(method = "checkCanJoin", at = @At("RETURN"), cancellable = true)
	private void checkIfPlayerCanLogin(SocketAddress address, GameProfile profile, CallbackInfoReturnable<Text> cir) {
		final LoginContextImpl context = new LoginContextImpl((PlayerManager) (Object) this, address, profile, cir.getReturnValue());

		// TODO: Fire event

		if (context.canJoin()) {
			cir.setReturnValue(null);

			// TODO: Fire post event
			return;
		}

		// Provide a generic reason if no reason for disconnection was provided
		cir.setReturnValue(context.getDisconnectionReason().orElse(new TranslatableText("multiplayer.disconnect.generic")));
	}
}
