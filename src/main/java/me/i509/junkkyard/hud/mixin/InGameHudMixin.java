package me.i509.junkkyard.hud.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

import me.i509.junkkyard.hud.RenderAfterCrosshairCallback;
import me.i509.junkkyard.hud.RenderAfterHeldItemTooltipCallback;
import me.i509.junkkyard.hud.RenderAfterPumpkinOverlayCallback;
import me.i509.junkkyard.hud.RenderAfterVignetteOverlayCallback;
import me.i509.junkkyard.hud.RenderCrosshairCallback;
import me.i509.junkkyard.hud.RenderHeldItemTooltipCallback;
import me.i509.junkkyard.hud.RenderPumpkinOverlayCallback;
import me.i509.junkkyard.hud.RenderVignetteOverlayCallback;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
	@Shadow protected abstract void renderPumpkinOverlay();
	@Shadow protected abstract void renderVignetteOverlay(Entity entity);
	@Shadow protected abstract void renderCrosshair(MatrixStack matrixStack);
	@Shadow @Final private MinecraftClient client;

	@Shadow public abstract void renderHeldItemTooltip(MatrixStack matrixStack);

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderVignetteOverlay(Lnet/minecraft/entity/Entity;)V"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;F)V")
	private void onRenderVignetteOverlay(InGameHud hud, Entity entity, final MatrixStack matrices, float tickDelta) {
		final boolean shouldRender = RenderVignetteOverlayCallback.EVENT.invoker().onRender(hud, matrices, entity, tickDelta);

		if (shouldRender) {
			this.renderVignetteOverlay(entity);
		}

		RenderAfterVignetteOverlayCallback.EVENT.invoker().onRender(hud, matrices, entity, tickDelta);
	}

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderPumpkinOverlay()V"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;F)V")
	private void onRenderPumpkinOverlay(final InGameHud inGameHud, final MatrixStack matrices, float tickDelta) {
		final boolean shouldRender = RenderPumpkinOverlayCallback.EVENT.invoker().onRender(inGameHud, matrices, tickDelta);

		if (shouldRender) {
			this.renderPumpkinOverlay();
		}

		RenderAfterPumpkinOverlayCallback.EVENT.invoker().onRender(inGameHud, matrices, tickDelta);
	}

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderCrosshair(Lnet/minecraft/client/util/math/MatrixStack;)V"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;F)V")
	private void onRenderCrosshair(InGameHud hud, MatrixStack matrices, MatrixStack unused, float tickDelta) {
		final boolean shouldRender = RenderCrosshairCallback.EVENT.invoker().onRender(hud, matrices, tickDelta);

		if (shouldRender) {
			this.renderCrosshair(matrices);
		}

		RenderAfterCrosshairCallback.EVENT.invoker().onRender(hud, matrices, tickDelta);

		// Bind texture back to gui icons after event
		this.client.getTextureManager().bindTexture(DrawableHelper.GUI_ICONS_TEXTURE);
	}

	// TODO: Status bars

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHeldItemTooltip(Lnet/minecraft/client/util/math/MatrixStack;)V"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;F)V")
	private void onRenderHeldItemTooltip(InGameHud hud, MatrixStack matrices, MatrixStack unused, float tickDelta) {
		final boolean shouldRender = RenderHeldItemTooltipCallback.EVENT.invoker().onRender(hud, matrices, tickDelta);

		if (shouldRender) {
			this.renderHeldItemTooltip(matrices);
		}

		RenderAfterHeldItemTooltipCallback.EVENT.invoker().onRender(hud, matrices, tickDelta);
	}


}
