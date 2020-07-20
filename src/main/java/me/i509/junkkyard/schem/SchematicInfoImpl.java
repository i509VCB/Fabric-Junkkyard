package me.i509.junkkyard.schem;

final class SchematicInfoImpl implements SchematicInfo {
	private final Schematic.Version version;
	private final short width, height, length;
	private final int dataVersion, xOffset, yOffset, zOffset;

	SchematicInfoImpl(Schematic.Version version, int dataVersion, short width, short height, short length, int xOffset, int yOffset, int zOffset) {
		this.version = version;
		this.dataVersion = dataVersion;
		this.width = width;
		this.height = height;
		this.length = length;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.zOffset = zOffset;
	}

	@Override
	public Schematic.Version getVersion() {
		return this.version;
	}

	@Override
	public int getDataVersion() {
		return this.dataVersion;
	}

	@Override
	public short getWidth() {
		return this.width;
	}

	@Override
	public short getHeight() {
		return this.height;
	}

	@Override
	public short getLength() {
		return this.length;
	}

	@Override
	public int getXOffset() {
		return this.xOffset;
	}

	@Override
	public int getYOffset() {
		return this.yOffset;
	}

	@Override
	public int getZOffset() {
		return this.zOffset;
	}
}
