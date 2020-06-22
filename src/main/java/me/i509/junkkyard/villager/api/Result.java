package me.i509.junkkyard.villager.api;

import java.util.Optional;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;

public final class Result<T> {
	private static final Result<?> PASS = new Result<>();
	private static final Result<?> FAILURE = new Result<>();

	public static <T> Result<T> success(T value) {
		return new Result<>(value);
	}

	public static <T> Result<T> pass() {
		return (Result<T>) PASS;
	}

	public static <T> Result<T> fail() {
		return (Result<T>) FAILURE;
	}

	/* @Nullable */
	private final T success;

	private Result() {
		this.success = null;
	}

	private Result(T value) {
		this.success = value;
	}

	public Optional<T> success() {
		return Optional.ofNullable(this.success);
	}

	public boolean isFailure() {
		return this == FAILURE;
	}

	public boolean isPassing() {
		return this == PASS;
	}

	public void test() {
		TradeOfferHelper.registerOffers(VillagerProfession.CLERIC, 5, factories -> {

		});
	}
}
