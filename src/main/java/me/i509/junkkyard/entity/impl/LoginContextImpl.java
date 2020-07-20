package me.i509.junkkyard.entity.impl;

import java.net.SocketAddress;
import java.util.Optional;

import com.mojang.authlib.GameProfile;

import net.minecraft.server.BannedIpEntry;
import net.minecraft.server.BannedPlayerEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.text.Text;

import me.i509.junkkyard.entity.PlayerLoginCallback;

public final class LoginContextImpl implements PlayerLoginCallback.LoginContext {
	private final PlayerManager playerManager;
	private final SocketAddress address;
	private final GameProfile profile;
	private Text disconnectionReason;
	private boolean allowed;

	public LoginContextImpl(PlayerManager playerManager, SocketAddress address, GameProfile profile, /* @Nullable */ Text disconnectionReason) {
		this.playerManager = playerManager;
		this.address = address;
		this.profile = profile;
		this.disconnectionReason = disconnectionReason;
		this.allowed = this.disconnectionReason == null;
	}

	@Override
	public MinecraftServer getServer() {
		return this.playerManager.getServer();
	}

	@Override
	public boolean canJoin() {
		return this.allowed;
	}

	@Override
	public boolean canBypassPlayerLimit() {
		return this.playerManager.canBypassPlayerLimit(this.profile);
	}

	@Override
	public void allow() {
		this.allowed = true;
	}

	@Override
	public void deny(Text reason) {
		this.allowed = false;
		this.disconnectionReason = reason;
	}

	@Override
	public void deny() {
		this.allowed = false;
		this.disconnectionReason = null;
	}

	@Override
	public Optional<Text> getDisconnectionReason() {
		return Optional.ofNullable(this.disconnectionReason);
	}

	@Override
	public Optional<BannedPlayerEntry> getBannedPlayerEntry() {
		return Optional.ofNullable(this.playerManager.getUserBanList().get(this.profile));
	}

	@Override
	public Optional<BannedIpEntry> getBannedIpEntry() {
		return Optional.ofNullable(this.playerManager.getIpBanList().get(this.address));
	}
}
