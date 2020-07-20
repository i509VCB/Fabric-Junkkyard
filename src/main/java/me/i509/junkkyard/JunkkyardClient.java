package me.i509.junkkyard;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

import net.minecraft.block.Blocks;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.ArmorStandEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.ArmorStandArmorEntityModel;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;

import me.i509.junkkyard.render.api.RegisterFeatureRendererEvent;

public class JunkkyardClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		RegisterFeatureRendererEvent.event(ArmorStandEntityRenderer.class).register((entityRenderer, acceptor) -> {
			acceptor.accept(new FeatureRenderer<ArmorStandEntity, ArmorStandArmorEntityModel>(entityRenderer) {
				@Override
				public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, ArmorStandEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
					matrices.push();
					final ModelPart modelPart = new ModelPart(20, 20, 0, 0).addCuboid(1.0F, 1.0F, 1.0F, 10.0F, 5.0F, 2.0F);

					matrices.scale(20, 20, 20);
					modelPart.render(matrices, vertexConsumers.getBuffer(RenderLayer.getSolid()), light, OverlayTexture.DEFAULT_UV);
					//MinecraftClient.getInstance().textRenderer.draw(matrices, "yeet", 100, 40, Formatting.GREEN.getColorValue());
					matrices.pop();
				}
			});
		});
	}

	public void otherTest() {
		HudRenderCallback.EVENT.register((matrices, tickDelta) -> {
			final MinecraftClient client = MinecraftClient.getInstance();
			final ClientPlayerEntity player = client.player;
			client.getProfiler().push("render minimap");

			matrices.push();
			// We need to disable culling
			RenderSystem.disableCull();
			//matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180.0F));
			matrices.scale(8, 8, 8);
			matrices.translate(10, 10, -1);
			//matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(MathHelper.wrapDegrees(player.getRoll())));

			matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(MathHelper.wrapDegrees(player.getYaw(tickDelta))));
			matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(MathHelper.wrapDegrees(player.getPitch(tickDelta) / 4)));

			final VertexConsumerProvider.Immediate immediate = client.getBufferBuilders().getEntityVertexConsumers();

			immediate.draw(RenderLayer.getEntitySolid(SpriteAtlasTexture.BLOCK_ATLAS_TEX));
			immediate.draw(RenderLayer.getEntityCutout(SpriteAtlasTexture.BLOCK_ATLAS_TEX));
			immediate.draw(RenderLayer.getEntityCutoutNoCull(SpriteAtlasTexture.BLOCK_ATLAS_TEX));
			immediate.draw(RenderLayer.getEntitySmoothCutout(SpriteAtlasTexture.BLOCK_ATLAS_TEX));

			client.getBlockRenderManager().renderBlockAsEntity(Blocks.STONE.getDefaultState(), matrices, immediate, 15728880, OverlayTexture.DEFAULT_UV);
			matrices.translate(0, 1, 0);
			client.getBlockRenderManager().renderBlockAsEntity(Blocks.STONE_BRICK_SLAB.getDefaultState(), matrices, immediate, 15728880, OverlayTexture.DEFAULT_UV);
			matrices.translate(0, 0, -1);
			client.getBlockRenderManager().renderBlockAsEntity(Blocks.LIME_SHULKER_BOX.getDefaultState().with(ShulkerBoxBlock.FACING, Direction.EAST), matrices, immediate, 15728880, OverlayTexture.DEFAULT_UV);
			immediate.draw();

			// Make sure we turn it back on lol
			RenderSystem.enableCull();
			matrices.pop();
			client.getProfiler().pop();

			// TODO: Render a block.
		});
	}
}
