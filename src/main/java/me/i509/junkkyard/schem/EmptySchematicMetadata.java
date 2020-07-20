package me.i509.junkkyard.schem;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

final class EmptySchematicMetadata implements SchematicMetadata {
	public static final SchematicMetadata INSTANCE = new EmptySchematicMetadata();

	private EmptySchematicMetadata() {
	}

	@Override
	public Optional<String> getName() {
		return Optional.empty();
	}

	@Override
	public Optional<String> getAuthor() {
		return Optional.empty();
	}

	@Override
	public Optional<Instant> getCreationDate() {
		return Optional.empty();
	}

	@Override
	public List<String> getRequiredMods() {
		return Collections.emptyList();
	}
}
