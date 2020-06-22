package me.i509.junkkyard.screen.impl;

import java.util.AbstractList;
import java.util.List;

import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.AbstractButtonWidget;

public final class ButtonList<T extends AbstractButtonWidget> extends AbstractList<T> {
	private final List<T> buttons;
	private final List<Element> children;

	public ButtonList(List<T> buttons, List<Element> children) {
		this.buttons = buttons;
		this.children = children;
	}

	@Override
	public T get(int index) {
		return this.buttons.get(index);
	}

	@Override
	public T set(int index, T element) {
		remove(element); // verify / ensure no duplicates

		final T existingButton = this.buttons.get(index);

		int elementIndex = this.children.indexOf(existingButton);
		if (elementIndex > -1) {
			this.children.set(elementIndex, element);
		}

		return this.buttons.set(index, element);
	}

	@Override
	public void add(int index, T element) {
		this.rangeCheckForAdd(index); // verify index bounds
		this.remove(element); // ensure no duplicates

		this.buttons.add(index, element);
		this.children.add(Math.min(this.children.size(), index), element);
	}

	@Override
	public T remove(int index) {
		rangeCheck(index); // verify index bounds

		final T removedButton = this.buttons.remove(index);
		index = this.children.indexOf(removedButton);

		if (index > -1) {
			this.children.remove(index);
		}

		return removedButton;
	}

	@Override
	public int size() {
		return this.buttons.size();
	}

	private void rangeCheck(int index) {
		if (index >= this.size()) {
			throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
		}
	}

	private void rangeCheckForAdd(int index) {
		if (index > this.size() || index < 0) {
			throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
		}
	}

	private String outOfBoundsMsg(int index) {
		return "Index: " + index + ", Size: "+ size();
	}
}
