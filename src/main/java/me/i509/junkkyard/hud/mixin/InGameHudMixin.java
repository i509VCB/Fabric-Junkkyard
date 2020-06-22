package me.i509.junkkyard.hud.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

import me.i509.junkkyard.hud.api.RenderChatHudEvent;
import me.i509.junkkyard.hud.api.RenderCrosshairEvent;
import me.i509.junkkyard.hud.api.RenderHeldItemTooltipEvent;
import me.i509.junkkyard.hud.api.RenderPumpkinOverlayEvent;
import me.i509.junkkyard.hud.api.RenderVignetteEvent;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
	@Shadow protected abstract void renderPumpkinOverlay();
	@Shadow protected abstract void renderVignetteOverlay(Entity entity);
	@Shadow protected abstract void renderCrosshair(MatrixStack matrixStack);
	@Shadow @Final private MinecraftClient client;

	@Shadow public abstract void renderHeldItemTooltip(MatrixStack matrixStack);

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderVignetteOverlay(Lnet/minecraft/entity/Entity;)V"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;F)V")
	private void onRenderVignetteOverlay(InGameHud hud, Entity entity, MatrixStack matrices, float tickDelta) {
		final boolean shouldRender = RenderVignetteEvent.RENDER.invoker().onRender(this.client, hud, matrices, entity, tickDelta);

		if (shouldRender) {
			this.renderVignetteOverlay(entity);
		}

		RenderVignetteEvent.AFTER_RENDER.invoker().onRender(this.client, hud, matrices, entity, tickDelta);
	}

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderPumpkinOverlay()V"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;F)V")
	private void onRenderPumpkinOverlay(InGameHud inGameHud, MatrixStack matrices, float tickDelta) {
		final boolean shouldRender = RenderPumpkinOverlayEvent.RENDER.invoker().onRender(this.client, inGameHud, matrices, tickDelta);

		if (shouldRender) {
			this.renderPumpkinOverlay();
		}

		RenderPumpkinOverlayEvent.AFTER_RENDER.invoker().onRender(this.client, inGameHud, matrices, tickDelta);
	}

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderCrosshair(Lnet/minecraft/client/util/math/MatrixStack;)V"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;F)V")
	private void onRenderCrosshair(InGameHud hud, MatrixStack matrices, MatrixStack unused, float tickDelta) {
		final boolean shouldRender = RenderCrosshairEvent.RENDER.invoker().onRender(this.client, hud, matrices, tickDelta);

		if (shouldRender) {
			this.renderCrosshair(matrices);
		}

		RenderCrosshairEvent.AFTER_RENDER.invoker().onRender(this.client, hud, matrices, tickDelta);

		// Bind texture back to gui icons after event
		this.client.getTextureManager().bindTexture(DrawableHelper.GUI_ICONS_TEXTURE);
	}

	// TODO: Status bars

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHeldItemTooltip(Lnet/minecraft/client/util/math/MatrixStack;)V"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;F)V")
	private void onRenderHeldItemTooltip(InGameHud hud, MatrixStack matrices, MatrixStack unused, float tickDelta) {
		final boolean shouldRender = RenderHeldItemTooltipEvent.RENDER.invoker().onRender(this.client, hud, matrices, tickDelta);

		if (shouldRender) {
			this.renderHeldItemTooltip(matrices);
		}

		RenderHeldItemTooltipEvent.AFTER_RENDER.invoker().onRender(this.client, hud, matrices, tickDelta);
	}

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;render(Lnet/minecraft/client/util/math/MatrixStack;I)V"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;F)V")
	private void onRenderChatHud(ChatHud chatHud, MatrixStack matrices, int ticks, MatrixStack unused, float tickDelta) {
		final boolean shouldRender = RenderChatHudEvent.RENDER.invoker().onRender(this.client, chatHud, (InGameHud) (Object) this, matrices, tickDelta, ticks);

		if (shouldRender) {
			chatHud.render(matrices, ticks);
		}

		RenderChatHudEvent.AFTER_RENDER.invoker().onRender(this.client, chatHud, (InGameHud) (Object) this, matrices, tickDelta, ticks);
	}
}
