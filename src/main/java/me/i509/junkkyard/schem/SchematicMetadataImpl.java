package me.i509.junkkyard.schem;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

final class SchematicMetadataImpl implements SchematicMetadata {
	private final String name;
	private final String author;
	private final Instant creationDate;
	private final List<String> requiredMods;

	SchematicMetadataImpl(String name, String author, Instant creationDate, List<String> requiredMods) {
		this.name = name;
		this.author = author;
		this.creationDate = creationDate;
		this.requiredMods = requiredMods;
	}

	@Override
	public Optional<String> getName() {
		return Optional.ofNullable(this.name);
	}

	@Override
	public Optional<String> getAuthor() {
		return Optional.ofNullable(this.author);
	}

	@Override
	public Optional<Instant> getCreationDate() {
		return Optional.ofNullable(this.creationDate);
	}

	@Override
	public List<String> getRequiredMods() {
		return Collections.unmodifiableList(this.requiredMods);
	}
}
