package me.i509.junkkyard.armor.mixin;

import java.util.Map;

import me.i509.junkkyard.armor.CustomArmorModel;
import me.i509.junkkyard.armor.TexturedArmor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmorFeatureRendererMixin<T extends LivingEntity, M extends BipedEntityModel<T>, A extends BipedEntityModel<T>> {
	@Shadow
	@Final
	protected static Map<String, Identifier> ARMOR_TEXTURE_CACHE;
	@Shadow
	protected abstract void renderArmorParts(EquipmentSlot slot, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ArmorItem item, boolean glint, A armorModel, boolean secondLayer, float red, float green, float blue, String suffix);

	/**
	 * Animate the custom armor model if an item is declared to use a CustomArmorModel.
	 */
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;animateModel(Lnet/minecraft/entity/LivingEntity;FFF)V"), method = "renderArmor")
	private void redirectCustomAnimate(BipedEntityModel<T> armorModel, T entity, float limbAngle, float limbDistance, float tickDelta,
			/* PARAMS FROM RENDER ARMOR*/ MatrixStack matrices, VertexConsumerProvider vertexConsumers, T entity$unused, float limbAngle$unused, float limbDistance$unused, float tickDelta$unused, float customAngle$unused, float headYaw$unused, float headPitch$unused, EquipmentSlot slot) {
		ItemStack stack = entity.getEquippedStack(slot);

		if (stack.getItem() instanceof CustomArmorModel) {
			((CustomArmorModel) stack.getItem()).animateModel(entity, limbAngle, limbDistance, tickDelta);
			return;
		}

		// Vanilla
		armorModel.animateModel(entity, limbAngle, limbDistance, tickDelta);
	}

	/**
	 * Set angles on the custom armor model if an item is declared to use a CustomArmorModel.
	 */
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/model/BipedEntityModel;setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V"), method = "renderArmor")
	private void redirectCustomSetAngles(BipedEntityModel<T> armorModel, T entity, float limbAngle, float limbDistance, float customAngle, float headYaw, float headPitch,
			/* PARAMS FROM RENDER ARMOR*/ MatrixStack matrices, VertexConsumerProvider vertexConsumers, T entity$unused, float limbAngle$unused, float limbDistance$unused, float tickDelta, float customAngle$unused, float headYaw$unused, float headPitch$unused, EquipmentSlot slot) {
		ItemStack stack = entity.getEquippedStack(slot);

		if (stack.getItem() instanceof CustomArmorModel) {
			((CustomArmorModel) stack.getItem()).setAngles(entity, limbAngle, limbDistance, customAngle, headYaw, headPitch);
			return;
		}

		// Vanilla
		armorModel.setAngles(entity, limbAngle, limbDistance, customAngle, headPitch, headPitch);
	}

	/**
	 * Render the custom armor model if an item is declared to use a CustomArmorModel.
	 */
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/feature/ArmorFeatureRenderer;renderArmorParts(Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/item/ArmorItem;ZLnet/minecraft/client/render/entity/model/BipedEntityModel;ZFFFLjava/lang/String;)V"), method = "renderArmor")
	private void redirectCustomModelRenderer(ArmorFeatureRenderer<T, M, A> armorFeatureRenderer, EquipmentSlot slot, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ArmorItem item, boolean glint, A armorModel, boolean secondLayer, float red, float green, float blue, String suffix) {
		if (item instanceof CustomArmorModel) {
			((CustomArmorModel) item).renderArmorParts(matrices, vertexConsumers, light, item, glint, armorModel, secondLayer, red, green, blue, suffix);
			return;
		}

		// Vanilla
		this.renderArmorParts(slot, matrices, vertexConsumers, light, item, glint, armorModel, secondLayer, red, green, blue, suffix);
	}

	/**
	 * Custom use namespace of registered item for asset directory.
	 */
	@ModifyArg(at = @At(value = "INVOKE", target = "Ljava/util/Map;computeIfAbsent(Ljava/lang/Object;Ljava/util/function/Function;)Ljava/lang/Object;"), method = "getArmorTexture", index = 0)
	private String getCustomTexture(String inputPath, EquipmentSlot slot, ArmorItem item, boolean secondLayer, /* @Nullable */ String suffix) {
		if (item instanceof TexturedArmor) { // If we implement textured armor, we use the registry namespace of the item for the texture
			Identifier itemId = Registry.ITEM.getId(item);

			// If we get the default id, then this item does not exist in the registry. Weird but okay.
			if (itemId.equals(Registry.ITEM.getDefaultId())) {
				return inputPath;
			}

			return itemId.getNamespace() + ":" + inputPath; // TODO: Or should we construct an identifier here and toString it for safety
		}

		return inputPath;
	}
}
