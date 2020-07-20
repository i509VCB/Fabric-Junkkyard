package me.i509.junkkyard.schem;

import java.util.Map;

/**
 * A visitor used to visit a schematic.
 *
 * <p>This visitor is designed for use with version two of the Sponge Schematic Specification, but is compatable with version one.
 * @see <a href="https://github.com/SpongePowered/Schematic-Specification/blob/master/versions/schematic-2.md">Sponge Schematic Specification Version Two</a>
 */
public interface SchematicVisitor {
	/**
	 * Visit the schematic info.
	 *
	 * @param info the schematic info
	 */
	void visitInfo(SchematicInfo info);

	/**
	 * Visit the schematic metadata.
	 *
	 * @param metadata the schematic metadata
	 */
	void visitMetadata(SchematicMetadata metadata);

	/**
	 * Visit a schematic block entity.
	 *
	 * @param blockEntity the block entity
	 */
	void visitBlockEntity(SchematicBlockEntity blockEntity);

	/**
	 * Visit a schematic entity.
	 *
	 * @param entity the entity
	 */
	void visitEntity(SchematicEntity entity);

	/**
	 * Visit a block palette.
	 *
	 * @param size the size of the palette
	 * @return a palette visitor to visit the palette with.
	 */
	BlockPaletteVisitor visitBlockPalette(int size);

	void visitBlockData(byte[] blockData);

	/**
	 * Visit a biome palette.
	 *
	 * @param size the size of the palette
	 * @return a palette visitor to visit the palette with.
	 */
	BiomePaletteVisitor visitBiomePalette(int size);

	void visitBiomeData(byte[] biomeData);

	interface BlockPaletteVisitor {
		/**
		 * Visit a palette entry
		 *
		 * @param registryId the registry id of the block
		 * @param properties the properties of the block state
		 * @param id the packed numerical id used in the schematic data.
		 */
		void visitBlockEntry(String registryId, Map<String, String> properties, int id);
	}

	interface BiomePaletteVisitor {
		void visitBiome(String registryId, int id);
	}
}
