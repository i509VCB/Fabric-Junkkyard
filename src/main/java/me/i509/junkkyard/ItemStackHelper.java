package me.i509.junkkyard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.JsonParseException;
import net.fabricmc.fabric.api.util.NbtType;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public final class ItemStackHelper {
	public static List<MutableText> getLore(ItemStack itemStack) {
		if (itemStack == null || itemStack.isEmpty()) {
			return Collections.emptyList();
		}

		if (itemStack.hasTag()) {
			//noinspection ConstantConditions
			if (itemStack.getTag().contains("display", NbtType.COMPOUND)) {
				final CompoundTag displayTag = itemStack.getSubTag("display");

				//noinspection ConstantConditions
				if (displayTag.getType("Lore") == NbtType.LIST) {
					final ListTag loreTag = displayTag.getList("Lore", NbtType.STRING);

					if (!loreTag.isEmpty()) {
						final List<MutableText> lore = new ArrayList<>();

						for (int idx = 0; idx < loreTag.size(); idx++) {
							try {
								final String jsonText = loreTag.getString(idx);
								final MutableText mutableText = Text.Serializer.fromJson(jsonText);

								if (mutableText != null) {
									lore.add(mutableText);
								}
							} catch (JsonParseException e) {
								displayTag.remove("Lore");
							}
						}

						return lore;
					}
				}
			}
		}

		return Collections.emptyList();
	}

	public void tesst() {
		ItemStack stack = new ItemStack(Items.ITEM_FRAME);
		for (MutableText mutableText : ItemStackHelper.getLore(stack)) {

		}
	}

	private ItemStackHelper() {
	}
}
