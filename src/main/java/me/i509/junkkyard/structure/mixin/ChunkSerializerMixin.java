package me.i509.junkkyard.structure.mixin;

import java.util.Map;

import com.google.common.collect.BiMap;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import net.minecraft.world.ChunkSerializer;

@Mixin(ChunkSerializer.class)
public abstract class ChunkSerializerMixin {
	@Shadow @Final private static Logger LOGGER;

	/**
	 * @author i509VCB
	 * @reason Emit log error when we find a missing structure.
	 */
	@Redirect(method = "readStructureReferences", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/BiMap;get(Ljava/lang/Object;)Ljava/lang/Object;"))
	private static <K, V> V emitMissingStructureMessage(BiMap<K, V> map, K key) {
		if (!map.containsKey(key)) {
			LOGGER.error("Found missing structure reference {}! Ignoring this structure.", key);
		}

		return map.get(key);
	}

	/**
	 * @author i509VCB
	 * @reason Do not load missing structures. Missing structures can cause lag, endless logs of exceptions and make chunks unable to save.
	 */
	@Redirect(method = "readStructureReferences", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
	private static <K, V> V ignoreMissingStructures(Map<K, V> map, K key, V value) {
		if (key == null) {
			// The key is null, do not add to the map
			return null;
		}

		return map.put(key, value);
	}
}
