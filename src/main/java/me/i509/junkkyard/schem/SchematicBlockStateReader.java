package me.i509.junkkyard.schem;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.util.Identifier;

final class SchematicBlockStateReader {
	private final String id;
	private final Map<String, String> properties;

	SchematicBlockStateReader(String id) {
		this.id = id;
		this.properties = Collections.emptyMap();
	}

	SchematicBlockStateReader(String id, Map<String, String> properties) {
		this.id = id;
		this.properties = Collections.unmodifiableMap(properties);
	}

	static SchematicBlockStateReader read(String blockState) {
		final String[] split = blockState.split("\\[");

		// No properties, just a block with 1 state
		if (split.length == 1) {
			return new SchematicBlockStateReader(blockState);
		} else if (split.length == 2) { // We have some properties
			final String id = split[0];
			final Map<String, String> properties = new HashMap<>();

			// Pop the "]" off the end of the string
			final String propertiesString = split[1].substring(0, split[1].length() - 1);

			// split at "," to seperate each property key + value
			for (String propertyKeyValue : propertiesString.split(",")) {
				// "=" splits key and value
				final String[] keyValue = propertyKeyValue.split("=");

				// Invalid property definition
				if (keyValue.length != 2) {
					throw new IllegalArgumentException(); // TODO: Explain
				}

				properties.put(keyValue[0], keyValue[1]);
			}

			return new SchematicBlockStateReader(id, properties);
		}

		throw new IllegalArgumentException(); // TODO: Explain
	}

	public String getId() {
		return this.id;
	}

	public Map<String, String> getProperties() {
		return Collections.unmodifiableMap(this.properties);
	}
}
