package me.i509.junkkyard.screen.mixin;

import java.util.Collections;
import java.util.List;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.render.item.ItemRenderer;

import me.i509.junkkyard.screen.api.ScreenContext;
import me.i509.junkkyard.screen.api.ScreenInitializeCallback;

@Mixin(Screen.class)
public abstract class ScreenMixin implements ScreenContext {
	@Shadow
	protected ItemRenderer itemRenderer;
	@Shadow
	protected TextRenderer textRenderer;
	@Shadow
	@Final
	protected List<AbstractButtonWidget> buttons;

	@Inject(method = "init(Lnet/minecraft/client/MinecraftClient;II)V", at = @At("TAIL"))
	private void onInitScreen(MinecraftClient client, int width, int height, CallbackInfo ci) {
		ScreenInitializeCallback.EVENT.invoker().onInitialize((Screen) (Object) this, this, client, width, height);
	}

	@Override
	public List<AbstractButtonWidget> buttons() {
		return Collections.unmodifiableList(this.buttons);
	}

	@Override
	public ItemRenderer getItemRenderer() {
		return this.itemRenderer;
	}

	@Override
	public TextRenderer getTextRenderer() {
		return this.textRenderer;
	}

	@Override
	public Screen screen() {
		return (Screen) (Object) this;
	}
}
