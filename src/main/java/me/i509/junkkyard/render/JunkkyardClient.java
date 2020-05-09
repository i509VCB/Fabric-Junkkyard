package me.i509.junkkyard.render;

import com.mojang.blaze3d.systems.RenderSystem;
import me.i509.junkkyard.blockentity.api.ServerBlockEntityEvents;
import me.i509.junkkyard.entity.client.api.ClientEntityEvents;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.event.server.ServerStopCallback;
import net.fabricmc.fabric.api.event.server.ServerTickCallback;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;

public class JunkkyardClient implements ClientModInitializer {
	private int clientEntities;
	private int clientBlockEntities;

	@Override
	public void onInitializeClient() {
		ServerBlockEntityEvents.LOAD.register((entity, world) -> {
			this.clientBlockEntities++;
			System.out.println(this.clientBlockEntities + " -> LDC :: " + Registry.BLOCK_ENTITY_TYPE.getId(entity.getType()).toString());
		});

		ServerBlockEntityEvents.UNLOAD.register((blockEntity, world) -> {
			this.clientBlockEntities--;
			System.out.println(this.clientBlockEntities + " -> ULC :: " + Registry.BLOCK_ENTITY_TYPE.getId(blockEntity.getType()).toString());
		});

		// TODO: Bind this to `disconnect from server instead?` -- TEMP: TAIL of MinecraftClient#disconnect(Screen)
		ServerStopCallback.EVENT.register(minecraftServer -> {
			if (!minecraftServer.isDedicated()) { // fixme: Use ClientNetworking#PLAY_DISCONNECTED instead of the server stop callback, and then stop tracking here
				this.clientBlockEntities = 0; // All blockentities are unloaded, and should stop being tracked
			}
		});

		ServerTickCallback.EVENT.register(minecraftServer -> {
			if (minecraftServer.getTicks() % 10 == 0) {
				System.out.println(minecraftServer.getTicks() + " :C: " + this.clientBlockEntities);

				int actual = 0;

				for (ServerWorld world : minecraftServer.getWorlds()) {
					// TODO: Query all block entities
					actual += world.blockEntities.size(); // I hope people are nice and don't add there
				}

				System.out.println("actualC -- " + actual);
			}
		});

		/*
		ClientEntityEvents.LOAD.register((entity, world) -> {
			this.clientEntities++;
			System.out.println(this.clientEntities + " -> LD :: " + Registry.ENTITY_TYPE.getId(entity.getType()).toString());
		});

		ClientEntityEvents.UNLOAD.register((entity, world) -> {
			this.clientEntities--;
			System.out.println(this.clientEntities + " -> UL :: " + Registry.ENTITY_TYPE.getId(entity.getType()).toString());
		});

		// TODO: Bind this to `disconnect from server instead?` -- TEMP: TAIL of MinecraftClient#disconnect(Screen)
		ServerStopCallback.EVENT.register(minecraftServer -> {
			if (!minecraftServer.isDedicated()) { // fixme: Use ClientNetworking#PLAY_DISCONNECTED instead of the server stop callback, and then stop tracking here
				this.clientEntities = 0; // We need to stop tracking all entities since we are being disconnected
			}
			//this.clientEntities = 0; // All entities are unloaded, and should stop being tracked
		});
		 */

		// TODO:
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
