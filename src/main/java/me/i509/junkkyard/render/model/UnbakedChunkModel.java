package me.i509.junkkyard.render.model;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelBakeSettings;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.UnbakedModel;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Function;

public class UnbakedChunkModel implements UnbakedModel {
	@Override
	public Collection<Identifier> getModelDependencies() {
		return Collections.emptyList(); // Required?
	}

	@Override
	public Collection<SpriteIdentifier> getTextureDependencies(Function<Identifier, UnbakedModel> unbakedModelGetter, Set<Pair<String, String>> unresolvedTextureReferences) {
		return Collections.emptyList(); // Required?
	}

	@Override
	public BakedModel bake(ModelLoader loader, Function<SpriteIdentifier, Sprite> textureGetter, ModelBakeSettings rotationContainer, Identifier modelId) {
		MeshBuilder mb = RendererAccess.INSTANCE.getRenderer().meshBuilder();
		Mesh blockMesh = generateMesh(mb);

		return new ChunkBakedModel(blockMesh);
	}

	private Mesh generateMesh(MeshBuilder mb) {
		QuadEmitter emitter = mb.getEmitter();
		emitter.square(Direction.NORTH, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

		return mb.build();
	}
}
