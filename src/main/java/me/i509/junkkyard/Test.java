package me.i509.junkkyard;

import net.minecraft.util.Identifier;

import me.i509.junkkyard.schem.SchematicBlockStateReader;

public class Test {
	public static void main(String[] args) {
		check("minecraft:diamond_ore");
		check("air");
		check("minecraft:planks[variant=oak]");
	}

	private static void check(String id) {
		final SchematicBlockStateReader state = SchematicBlockStateReader.fromString(id);
		if (!state.getId().equals(new Identifier(id))) {
			throw new AssertionError("INVALID -- " + id);
		}
	}
}
