package me.i509.junkkyard.screen.api;

import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.render.item.ItemRenderer;

/**
 * Provides access to additional context a screen can hold.
 */
@Environment(EnvType.CLIENT)
public interface ScreenContext {
	/**
	 * Gets the screen's context.
	 *
	 * @param screen the screen
	 * @return the screen's context
	 */
	static ScreenContext from(Screen screen) {
		return (ScreenContext) screen;
	}

	/**
	 * Gets all button widgets the screen holds.
	 *
	 * @return a list of all buttons on this screen.
	 */
	List<AbstractButtonWidget> getButtons();

	/**
	 * Gets the item renderer the screen holds.
	 *
	 * @return the item renderer
	 */
	ItemRenderer getItemRenderer();

	/**
	 * Gets the text renderer the screen holds.
	 *
	 * @return the text renderer.
	 */
	TextRenderer getTextRenderer();

	/**
	 * Gets the screen which owns this context.
	 *
	 * @return the owning screen
	 */
	Screen getScreen();
}
