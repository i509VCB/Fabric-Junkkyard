package me.i509.junkkyard.schem;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.mojang.serialization.Dynamic;

import net.minecraft.block.BlockState;
import net.minecraft.datafixer.NbtOps;
import net.minecraft.datafixer.Schemas;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.Tag;
import net.minecraft.util.collection.Int2ObjectBiMap;
import net.minecraft.world.biome.Biome;

public final class Schematic implements SchematicVisitor {
	private SchematicInfo info;
	private SchematicMetadata metadata = EmptySchematicMetadata.INSTANCE;
	private byte[] blockData = new byte[0];
	private Int2ObjectBiMap<BlockState> blockPalette = new Int2ObjectBiMap<>(64); // Allocate a sane default for now
	private byte[] biomeData = new byte[0];
	private Int2ObjectBiMap<Biome> biomePalette = new Int2ObjectBiMap<>(64); // Allocate a sane default for now
	private List<SchematicBlockEntity> blockEntities = new ArrayList<>();
	/**
	 * Entities in this schematic. Only supported in {@link Version#TWO}.
	 */
	private List<SchematicEntity> entities = new ArrayList<>();

	public static Schematic create(Schematic.Version version) {
		return new Schematic(Objects.requireNonNull(version, "Schematic version cannot be null"));
	}

	/**
	 * Loads a schematic
	 *
	 * @param path the path of the schematic
	 * @return a schematic
	 * @throws IOException if the schematic could not be read
	 * @throws SchematicReader.SchematicReadException if any issues occur while reading the schematic
	 */
	public static Schematic load(Path path) throws IOException, SchematicReader.SchematicReadException {
		final Schematic schematic = new Schematic();
		SchematicReader.accept(path, schematic);

		return schematic;
	}

	// Internal constructor
	private Schematic() {
	}

	private Schematic(Version version) {
	}

	public SchematicInfo getInfo() {
		return this.info;
	}

	public SchematicMetadata getMetadata() {
		return this.metadata;
	}

	public BlockState getBlockState(int x, int y, int z) {
		this.validateLocation(x, y, z);
		final byte blockDatum = this.blockData[this.wrapLocation(x, y, z)];

		return this.blockPalette.get(blockDatum);
	}

	public Biome getBiome(int x, int z) {
		// TODO: Wrap location and apply to palette
		return null;
	}

	public List<SchematicEntity> getEntities() {
		if (this.supports(Capability.ENTITIES)) {
			return Collections.unmodifiableList(this.entities);
		}

		return Collections.emptyList();
	}

	public List<SchematicBlockEntity> getBlockEntities() {
		return Collections.unmodifiableList(this.blockEntities);
	}

	public boolean supports(Capability capability) {
		return this.info.getVersion().supports(capability);
	}

	// VISITOR IMPLEMENTATION

	@Override
	public void visitInfo(SchematicInfo info) {
		this.info = info;
	}

	@Override
	public void visitMetadata(SchematicMetadata metadata) {
		this.metadata = metadata;
	}

	@Override
	public void visitBlockEntity(SchematicBlockEntity blockEntity) {
		this.blockEntities.add(blockEntity);
	}

	@Override
	public void visitEntity(SchematicEntity entity) {
		// TODO: Verify version of schematic before visiting any entity
		this.entities.add(entity);
	}

	@Override
	public BlockPaletteVisitor visitBlockPalette(int size) {
		return null;
	}

	@Override
	public void visitBlockData(byte[] blockData) {
		this.blockData = blockData;
	}

	@Override
	public BiomePaletteVisitor visitBiomePalette(int size) {
		return null; // TODO: Implement
	}

	@Override
	public void visitBiomeData(byte[] biomeData) {
		this.biomeData = biomeData;
	}

	// TODO: Explain each error
	private void validateLocation(int x, int y, int z) {
		final SchematicInfo info = this.info;

		if (x > info.getWidth() || 0 > x) {
			throw new IllegalArgumentException();
		}

		if (y > info.getHeight() || 0 > y) {
			throw new IllegalArgumentException();
		}

		if (z > info.getLength() || 0 > z) {
			throw new IllegalArgumentException();
		}
	}

	private int wrapLocation(int x, int y, int z) {
		// x + (z * Width) + (y * Width * Length)
		return x + ((z) * this.info.getWidth()) + (y * this.info.getWidth() * this.info.getLength());
	}

	public enum Version {
		ONE(
			1,
			Capability.BLOCK_ENTITIES
		),
		TWO(
			2,
			Capability.BLOCK_ENTITIES,
			Capability.ENTITIES,
			Capability.BIOMES
		);

		private final int version;
		private final EnumSet<Capability> capabilities;

		Version(int version, Capability... capabilities) {
			this.version = version;
			this.capabilities = EnumSet.copyOf(Arrays.asList(capabilities));
		}

		public boolean supports(Capability capability) {
			return this.capabilities.contains(capability);
		}

		public int getNumericalVersion() {
			return this.version;
		}
	}

	public enum Capability {
		/**
		 * The schematic supports block entities.
		 */
		BLOCK_ENTITIES,
		/**
		 * The schematic supports entities.
		 */
		ENTITIES,
		/**
		 * The schematic supports biomes.
		 */
		BIOMES
	}

	private class BlockPaletteVisitorImpl implements BlockPaletteVisitor {
		@Override
		public void visitBlockEntry(String registryId, Map<String, String> properties, int id) {
			// Assemble a rudimentary block state in tag form
			final CompoundTag tag = new CompoundTag();
			final CompoundTag propertiesTag = new CompoundTag();

			tag.putString("Name", registryId);

			for (Map.Entry<String, String> propertyEntry : properties.entrySet()) {
				propertiesTag.putString(propertyEntry.getKey(), propertyEntry.getValue());
			}

			tag.put("Properties", propertiesTag);

			// Data fix the block state to latest
			final CompoundTag fixedState = (CompoundTag) SchematicReader.fixBlockState(Schemas.getFixer(), new Dynamic<>(NbtOps.INSTANCE, tag), Schematic.this.getInfo().getDataVersion()).getValue();
			Schematic.this.blockPalette.put(NbtHelper.toBlockState(fixedState), id);
		}
	}

	private class BiomePaletteVisitorImpl implements BiomePaletteVisitor {
		@Override
		public void visitBiome(String registryId, int id) {
			// TODO:
		}
	}
}
