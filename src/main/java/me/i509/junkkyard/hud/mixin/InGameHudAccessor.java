package me.i509.junkkyard.hud.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;

@Mixin(InGameHud.class)
public interface InGameHudAccessor {
	@Accessor
	MinecraftClient getClient();
}
