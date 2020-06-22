package me.i509.junkkyard.armor;

import net.minecraft.item.ArmorItem;

/**
 * By implementing this interface, this piece of armor will get it's texture located outside of the minecraft namespace.
 *
 * <p>For an armor item registered in the namespace of {@code examplemod}, the textures will be located at {@code assets/examplemod/textures/models/armor/}.
 * The item this interface is implemented on <strong>must</strong> be a {@link ArmorItem} or the namespace will be ignored.
 */
public interface TexturedArmor {
}
