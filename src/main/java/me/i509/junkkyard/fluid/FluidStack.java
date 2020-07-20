package me.i509.junkkyard.fluid;

import net.minecraft.fluid.Fluid;

/**
 * Represents a type of fluid and an amount of fluid.
 */
public interface FluidStack {
	Fluid getFluid();

	Fraction getAmount();

	Fraction add(Fraction fraction);

	Fraction remove(Fraction fraction);

	StackType getType();

	enum StackType {
		BOUNDED,
		UNBOUNDED
	}
}
