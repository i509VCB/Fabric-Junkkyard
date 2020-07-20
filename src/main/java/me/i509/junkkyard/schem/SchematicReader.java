package me.i509.junkkyard.schem;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.Dynamic;
import net.fabricmc.fabric.api.util.NbtType;

import net.minecraft.SharedConstants;
import net.minecraft.datafixer.TypeReferences;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.util.Identifier;

public final class SchematicReader {
	/**
	 * Loads and visits a schematic using the supplied visitor.
	 *
	 * <p>The version of the schematic is automatically determined when the schematic is loaded.
	 *
	 * @param path the path of the schematic
	 * @param visitor the visitor used to visit the schematic
	 * @throws IOException if the schematic could not be read
	 * @throws SchematicReadException if the schematic is invalid
	 */
	public static void accept(Path path, SchematicVisitor visitor) throws IOException, SchematicReadException {
		try (InputStream pathStream = Files.newInputStream(path)) {
			try (GZIPInputStream gzipInputStream = new GZIPInputStream(pathStream)) {
				final CompoundTag tag = NbtIo.readCompressed(gzipInputStream);

				// This field is constant no matter the version
				final int version = SchematicReader.getIntOrThrow(tag, "Version");

				switch (version) {
				case 1:
					acceptV1(tag, visitor);
					break;
				case 2:
					acceptV2(tag, visitor);
					break;
				default:
					throw new SchematicReadException(); // TODO: Describe error
				}
			}
		}
	}

	private static void acceptV1(CompoundTag tag, SchematicVisitor visitor) throws SchematicReadException {
		throw new UnsupportedOperationException("Cannot read V1 Schematic yet!");
	}

	private static void acceptV2(CompoundTag tag, SchematicVisitor visitor) throws SchematicReadException {
		visitor.visitInfo(SchematicReader.readV2Info(tag));
		visitor.visitMetadata(SchematicReader.readV2Metadata(tag));

		// Block palette
		int paletteMax = 0xFFFF;

		if (tag.contains("PaletteMax", NbtType.INT)) {
			paletteMax = tag.getInt("PaletteMax");
		}

		if (tag.contains("Palette", NbtType.COMPOUND)) {
			final SchematicVisitor.BlockPaletteVisitor paletteVisitor = visitor.visitBlockPalette(paletteMax);
			final CompoundTag palette = tag.getCompound("Palette");

			for (String key : palette.getKeys()) {
				final SchematicBlockStateReader reader = SchematicBlockStateReader.read(key);
				paletteVisitor.visitBlockEntry(reader.getId(), reader.getProperties(), palette.getInt(key));
			}
		}

		// Biome palette
		int biomePaletteMax = 0xFFFF;

		if (tag.contains("BiomePaletteMax", NbtType.INT)) {
			biomePaletteMax = tag.getInt("BiomePaletteMax");
		}

		if (tag.contains("BiomePalette", NbtType.COMPOUND)) {
			final SchematicVisitor.BiomePaletteVisitor paletteVisitor = visitor.visitBiomePalette(biomePaletteMax);
			final CompoundTag palette = tag.getCompound("BiomePalette");

			for (String key : palette.getKeys()) {
				paletteVisitor.visitBiome(key, palette.getInt(key));
			}
		}

		if (tag.contains("BlockData", NbtType.BYTE_ARRAY)) {
			final byte[] blockData = tag.getByteArray("BlockData");
			visitor.visitBlockData(blockData);
		}

		if (tag.contains("BiomeData", NbtType.BYTE_ARRAY)) {
			final byte[] biomeData = tag.getByteArray("BiomeData");
			visitor.visitBiomeData(biomeData);
		}

		if (tag.contains("BlockEntities", NbtType.LIST)) {
			final ListTag blockEntities = tag.getList("BlockEntities", NbtType.COMPOUND);

			for (int i = 0; i < blockEntities.size(); i++) {
				final CompoundTag blockEntityTag = blockEntities.getCompound(i);

				SchematicReader.readV2BlockEntity(blockEntityTag);
			}
		}

		if (tag.contains("Entities", NbtType.LIST)) {
			final ListTag entities = tag.getList("Entities", NbtType.COMPOUND);

			for (int i = 0; i < entities.size(); i++) {
				final CompoundTag entityTag = entities.getCompound(i);
			}
		}
	}

	private static SchematicInfo readV2Info(CompoundTag tag) throws SchematicReadException {
		final int dataVersion = SchematicReader.getIntOrThrow(tag, "DataVersion");
		final short width = SchematicReader.getShortOrThrow(tag, "Width");
		final short height = SchematicReader.getShortOrThrow(tag, "Height");
		final short length = SchematicReader.getShortOrThrow(tag, "Length");

		int xOffset = 0;
		int yOffset = 0;
		int zOffset = 0;

		if (tag.contains("Offset" ,NbtType.INT_ARRAY)) {
			final int[] offsets = tag.getIntArray("Offset");

			// Ignore offset if any of the array entries are missing
			if (offsets.length == 3) {
				xOffset = offsets[0];
				yOffset = offsets[1];
				zOffset = offsets[2];
			}
		}

		return new SchematicInfoImpl(Schematic.Version.TWO, dataVersion, width, height, length, xOffset, yOffset, zOffset);
	}

	private static SchematicMetadata readV2Metadata(CompoundTag tag) {
		if (tag.contains("Metadata", NbtType.COMPOUND)) {
			final CompoundTag metadataTag = tag.getCompound("Metadata");
			/* @Nullable */ String name = null;
			/* @Nullable */ String author = null;
			/* @Nullable */ Instant date = null;
			final List<String> requiredMods = new ArrayList<>();

			if (metadataTag.contains("Name", NbtType.STRING)) {
				name = metadataTag.getString("Name");
			}

			if (metadataTag.contains("Author", NbtType.STRING)) {
				author = metadataTag.getString("Author");
			}

			if (metadataTag.contains("Date", NbtType.LONG)) {
				date = Instant.ofEpochMilli(metadataTag.getLong("Date"));
			}

			if (metadataTag.contains("RequiredMods", NbtType.LIST)) {
				final ListTag requiredModsList = metadataTag.getList("RequiredMods", NbtType.STRING);

				for (int i = 0; i < requiredModsList.size(); i++) {
					requiredMods.add(requiredModsList.getString(i));
				}
			}

			return new SchematicMetadataImpl(name, author, date, requiredMods);
		}

		return EmptySchematicMetadata.INSTANCE;
	}

	private static SchematicBlockEntity readV2BlockEntity(CompoundTag blockEntityTag) throws SchematicReadException {
		if (!blockEntityTag.contains("Pos", NbtType.INT_ARRAY)) {
			throw new SchematicReadException();
		}

		final int[] pos = blockEntityTag.getIntArray("Pos");

		if (pos.length != 3) {
			throw new SchematicReadException();
		}

		blockEntityTag.remove("Pos");

		if (!blockEntityTag.contains("Id", NbtType.STRING)) {
			throw new SchematicReadException();
		}

		final String id = blockEntityTag.getString("Id");
		blockEntityTag.remove("Id");

		return new SchematicBlockEntityImpl(pos[0], pos[1], pos[2], id, blockEntityTag);
	}

	private static int getIntOrThrow(CompoundTag tag, String key) throws SchematicReadException {
		if (tag.contains(key, NbtType.INT)) {
			return tag.getInt(key);
		}

		throw new SchematicReadException();
	}

	private static short getShortOrThrow(CompoundTag tag, String key) throws SchematicReadException {
		if (tag.contains(key, NbtType.SHORT)) {
			return tag.getShort(key);
		}

		throw new SchematicReadException();
	}

	static <T> Dynamic<T> fixBlockState(DataFixer dataFixer, Dynamic<T> dynamic, int dataVersion) {
		return dataFixer.update(TypeReferences.BLOCK_STATE, dynamic, dataVersion, SharedConstants.getGameVersion().getWorldVersion());
	}

	static <T> Dynamic<T> fixBlockEntity(DataFixer dataFixer, Dynamic<T> dynamic, int dataVersion) {
		return dataFixer.update(TypeReferences.BLOCK_ENTITY, dynamic, dataVersion, SharedConstants.getGameVersion().getWorldVersion());
	}

	static <T> Dynamic<T> fixBiome(DataFixer dataFixer, Dynamic<T> dynamic, int dataVersion) {
		return dataFixer.update(TypeReferences.BIOME, dynamic, dataVersion, SharedConstants.getGameVersion().getWorldVersion());
	}

	static <T> Dynamic<T> fixEntity(DataFixer dataFixer, Dynamic<T> dynamic, int dataVersion) {
		return dataFixer.update(TypeReferences.ENTITY, dynamic, dataVersion, SharedConstants.getGameVersion().getWorldVersion());
	}

	public static class SchematicReadException extends Exception {
		// TODO: Extra stuff
	}
}
