package me.i509.junkkyard.screen.api;

import java.util.List;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.render.item.ItemRenderer;

/**
 * Provides access to additional context a screen can hold.
 */
public interface ScreenContext {
	static ScreenContext from(Screen screen) {
		return (ScreenContext) screen;
	}

	/**
	 * Gets all button widgets the screen holds.
	 *
	 * @return an unmodifiable list of all button widgets
	 */
	List<AbstractButtonWidget> buttons();

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
	Screen screen();
}
