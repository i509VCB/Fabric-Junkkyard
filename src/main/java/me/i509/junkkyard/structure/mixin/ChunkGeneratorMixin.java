package me.i509.junkkyard.structure.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.world.gen.chunk.ChunkGenerator;

@Mixin(ChunkGenerator.class)
public abstract class ChunkGeneratorMixin {
	//@Redirect(method = "addStructureReferences", at = @At("CONSTANT"))
	private void ignoreMissingStructures() {

	}
}
