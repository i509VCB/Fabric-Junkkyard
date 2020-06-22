package me.i509.junkkyard.armor.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import me.i509.junkkyard.armor.TexturedArmor;

@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmorFeatureRendererMixin<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> {
	/**
	 * Use namespace of registered item for asset directory. For modded armor items.
	 */
	@ModifyArg(at = @At(value = "INVOKE", target = "Ljava/util/Map;computeIfAbsent(Ljava/lang/Object;Ljava/util/function/Function;)Ljava/lang/Object;"), method = "getArmorTexture", index = 0)
	private String getCustomTexture(String inputPath, EquipmentSlot slot, ArmorItem item, boolean secondLayer, /* @Nullable */ String suffix) {
		if (item instanceof TexturedArmor) { // If we implement textured armor, we use the registry namespace of the item for the texture
			final Identifier itemId = Registry.ITEM.getId(item);

			// If we get the default id, then this item does not exist in the registry. This shouldn't happen, but we will let vanilla handle failing this.
			if (itemId.equals(Registry.ITEM.getDefaultId())) {
				return inputPath;
			}

			final String identifier = itemId.getNamespace() + ":" + inputPath;

			if (Identifier.tryParse(identifier) != null) { // Only return if our identifier is valid
				return identifier;
			}
		}

		return inputPath;
	}
}
