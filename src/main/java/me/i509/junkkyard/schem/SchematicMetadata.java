package me.i509.junkkyard.schem;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Specifies additional meta information about a schematic.
 */
public interface SchematicMetadata {
	/**
	 * Gets the name of the schematic.
	 *
	 * @return the name of the schematic
	 */
	Optional<String> getName();

	/**
	 * Gets the name of the author of the schematic
	 *
	 * @return the name of the author
	 */
	Optional<String> getAuthor();

	/**
	 * The instant the schematic was created at.
	 *
	 * @return the instant the schematic was created
	 */
	Optional<Instant> getCreationDate();

	/**
	 * All mod ids which refer to mods that are present in the schematic.
	 *
	 * @return a list of required mods
	 */
	List<String> getRequiredMods();
}
