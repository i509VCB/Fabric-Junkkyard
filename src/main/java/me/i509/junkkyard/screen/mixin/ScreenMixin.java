package me.i509.junkkyard.screen.mixin;

import java.util.List;

import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.network.packet.c2s.play.PickFromInventoryC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;

import me.i509.junkkyard.JunkkyardClient;
import me.i509.junkkyard.screen.api.ScreenContext;
import me.i509.junkkyard.screen.api.ScreenInitializeCallback;
import me.i509.junkkyard.screen.impl.ButtonList;

@Mixin(Screen.class)
public abstract class ScreenMixin implements ScreenContext {
	@Shadow
	protected ItemRenderer itemRenderer;
	@Shadow
	protected TextRenderer textRenderer;
	@Shadow
	@Final
	protected List<AbstractButtonWidget> buttons;
	@Shadow
	@Final
	protected List<Element> children;

	@Shadow protected MinecraftClient client;
	@Unique
	private ButtonList<AbstractButtonWidget> fabricButtons;

	@Inject(method = "init(Lnet/minecraft/client/MinecraftClient;II)V", at = @At("TAIL"))
	private void onInitScreen(MinecraftClient client, int width, int height, CallbackInfo ci) {
		ScreenInitializeCallback.EVENT.invoker().onInitialize((Screen) (Object) this, this, client, width, height);
	}

	@Override
	public List<AbstractButtonWidget> getButtons() {
		// Lazy init to handle class init
		if (this.fabricButtons == null) {
			this.fabricButtons = new ButtonList<>(this.buttons, this.children);
		}

		return this.fabricButtons;
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
	public Screen getScreen() {
		return (Screen) (Object) this;
	}

	// TEST

	@Inject(method = "keyPressed", at = @At("HEAD"))
	private void sendShit(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
		System.out.println("CL: Pressed");
		if (this.client.player != null && keyCode == GLFW.GLFW_KEY_U) {
			client.getNetworkHandler().sendPacket(new PickFromInventoryC2SPacket(5555));
			client.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(5555));
		}
	}
}
