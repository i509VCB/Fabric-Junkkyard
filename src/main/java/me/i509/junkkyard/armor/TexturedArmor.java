package me.i509.junkkyard.armor;

import net.minecraft.item.ArmorItem;

/**
 * Specifies that a piece of armor has a texture located outside of the minecraft namespace.
 * By implementing this interface, textures will be queried in the namespace that the item is registered in.
 *
 * <p>For an armor item registered in the namespace of {@code examplemod}, the textures will be located at {@code assets/examplemod/textures/models/armor/}.
 * The item this interface is implemented on <strong>must</strong> be a {@link ArmorItem} or the namespace will be ignored.
 */
public interface TexturedArmor {
}
