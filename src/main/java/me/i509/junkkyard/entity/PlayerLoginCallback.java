package me.i509.junkkyard.entity;

import java.net.SocketAddress;
import java.util.Optional;

import com.mojang.authlib.GameProfile;

import net.minecraft.server.BannedIpEntry;
import net.minecraft.server.BannedPlayerEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;

public final class PlayerLoginCallback {
	interface BeforeLogin {
		void beforePlayerLogin(SocketAddress address, GameProfile profile, LoginContext context);
	}

	private PlayerLoginCallback() {
	}

	/**
	 * Provides additional context about the player logging in.
	 */
	public interface LoginContext {
		/**
		 * The server the player is joining.
		 *
		 * @return the server
		 */
		MinecraftServer getServer();

		/**
		 * Whether the player will be allowed to join the server.
		 *
		 * @return true if the player will be able to join.
		 */
		boolean canJoin();

		/**
		 * Checks whether the player can bypass the player limit.
		 *
		 * @return true if the player can bypass the player limit.
		 */
		boolean canBypassPlayerLimit();

		/**
		 * Allows the player to join.
		 *
		 * <p>By calling this method, the player will be allowed to join regardless of ban status, player count or any other reason.
		 */
		void allow();

		/**
		 * Prevents the player logging in.
		 *
		 * @param reason the message explaining why the player was disconnected
		 */
		void deny(Text reason);

		/**
		 * Prevents the player from logging in.
		 */
		void deny();

		/**
		 * Gets the reason why the player is prevented from logging in.
		 *
		 * <p>{@link Optional#empty() No reason being provided} does not mean the player will be allowed to join.
		 * Users of this method should query {@link #canJoin()} to verify the player can join if no reason is provided.
		 *
		 * @return the reason the player is prevented from logging in.
		 */
		Optional<Text> getDisconnectionReason();

		/**
		 * Gets the ban entry associated with this player.
		 *
		 * @return the ban entry or {@link Optional#empty()} if the player is not banned.
		 */
		Optional<BannedPlayerEntry> getBannedPlayerEntry();

		/**
		 * Gets the ban entry associated with the address this player is connecting from.
		 *
		 * @return the ban entry or {@link Optional#empty()} if the player's connecting address is not banned.
		 */
		Optional<BannedIpEntry> getBannedIpEntry();
	}
}
