package me.i509.junkkyard.render.mixin;

import java.util.Objects;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;

import me.i509.junkkyard.render.api.RegisterFeatureRendererEvent;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {
	@Shadow protected abstract boolean addFeature(FeatureRenderer<T, M> feature);

	@Inject(method = "<init>", at = @At("TAIL"))
	private void registerFeatures(EntityRenderDispatcher dispatcher, M model, float shadowRadius, CallbackInfo ci) {
		//noinspection unchecked,ConstantConditions,rawtypes
		RegisterFeatureRendererEvent.event((Class<LivingEntityRenderer<T, M>>) (Class) this.getClass()).invoker().registerFeatureRenderers((LivingEntityRenderer<T, M>) (Object) this, featureRenderer -> {
			this.addFeature(Objects.requireNonNull(featureRenderer, "Feature Renderer cannot be null"));
		});
	}
}
