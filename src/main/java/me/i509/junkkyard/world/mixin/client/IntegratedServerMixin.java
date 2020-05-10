package me.i509.junkkyard.world.mixin.client;

import me.i509.junkkyard.world.mixin.MinecraftServerMixin;
import net.minecraft.server.integrated.IntegratedServer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(IntegratedServer.class)
public abstract class IntegratedServerMixin extends MinecraftServerMixin {
}
