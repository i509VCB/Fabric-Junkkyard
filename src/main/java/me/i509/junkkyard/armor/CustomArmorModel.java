package me.i509.junkkyard.armor;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ArmorItem;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterfaces;

/**
 * Specifies that a piece of armor uses a custom model.
 *
 * <p> The item this interface is implemented on <strong>must</strong> be a {@link ArmorItem} or the model will be ignored.
 *
 * <p>You should make sure this interface is only implemented on the client using {@link EnvironmentInterfaces}</p>
 */
public interface CustomArmorModel {
	@Environment(EnvType.CLIENT)
	<T extends LivingEntity> void animateModel(T entity, float limbAngle, float limbDistance, float tickDelta);

	@Environment(EnvType.CLIENT)
	<T extends LivingEntity> void setAngles(T entity, float limbAngle, float limbDistance, float customAngle, float headYaw, float headPitch);

	@Environment(EnvType.CLIENT)
	<T extends LivingEntity, A extends BipedEntityModel<T>> void renderArmorParts(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ArmorItem item, boolean glint, A armorModel, boolean secondLayer, float red, float green, float blue, String suffix);
}
