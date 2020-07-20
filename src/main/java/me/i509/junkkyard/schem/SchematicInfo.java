package me.i509.junkkyard.schem;

/**
 * Represents version data related to a schematic.
 */
public interface SchematicInfo {
	/**
	 * Gets the version of the schematic format.
	 *
	 * @return the version of the schematic
	 */
	Schematic.Version getVersion();

	/**
	 * Gets the data version of Minecraft used to create the schematic.
	 *
	 * @return the data version this schematic was created on.
	 */
	int getDataVersion();

	/**
	 * Gets the width of the schematic.
	 *
	 * <p>This is also the size of the area in the X-axis.
	 *
	 * @return the width of this schematic
	 */
	short getWidth();

	/**
	 * Gets the height of the schematic.
	 *
	 * <p>This is also the size of the area in the Y-axis.
	 *
	 * @return the height of this schematic
	 */
	short getHeight();

	/**
	 * Gets the length of the schematic.
	 *
	 * <p>This is also the size of the area in the Z-axis.
	 *
	 * @return the length of this schematic
	 */
	short getLength();

	/**
	 * Gets the schematic's relative offset on the X-axis.
	 *
	 * @return the schematic's relative offset on the X-axis
	 */
	int getXOffset();

	/**
	 * Gets the schematic's relative offset on the Y-axis.
	 *
	 * @return the schematic's relative offset on the Y-axis
	 */
	int getYOffset();

	/**
	 * Gets the schematic's relative offset on the Z-axis.
	 *
	 * @return the schematic's relative offset on the Z-axis
	 */
	int getZOffset();
}
