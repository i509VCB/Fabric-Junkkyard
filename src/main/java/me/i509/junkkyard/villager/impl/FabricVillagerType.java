package me.i509.junkkyard.villager.impl;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerType;

public final class FabricVillagerType implements VillagerType {
	private final Identifier id;
	private final Item fishermanBoatItem;

	public FabricVillagerType(Identifier id, Item fishermanBoatItem) {
		this.id = id;
		this.fishermanBoatItem = fishermanBoatItem;
	}

	@Override
	public String toString() {
		return "FabricVillagerType[id=" + this.id.toString() + ", fishermanItem=" + this.getFishermanBoatItem() + "]";
	}

	public Item getFishermanBoatItem() {
		return this.fishermanBoatItem;
	}
}
