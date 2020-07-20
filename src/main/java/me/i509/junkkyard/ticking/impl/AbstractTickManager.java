package me.i509.junkkyard.ticking.impl;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import net.minecraft.entity.Entity;

import me.i509.junkkyard.ticking.api.BlockEntityTickManager;
import me.i509.junkkyard.ticking.api.EntityTickManager;
import me.i509.junkkyard.ticking.api.TickHandler;
import me.i509.junkkyard.ticking.api.TickManager;

public abstract class AbstractTickManager<S, H extends TickHandler<S>> implements TickManager<S, H> {
	protected Set<H> handlers = new HashSet<>();

	protected AbstractTickManager() {
	}

	@Override
	public void register(H handler) {
		Objects.requireNonNull(handler, "Tick Handler cannot be null");
		this.handlers.add(handler);
	}

	static class Entity extends AbstractTickManager<net.minecraft.entity.Entity, TickHandler.EntityHandler> implements EntityTickManager {
		@Override
		public void refresh(net.minecraft.entity.Entity subject) {
			//noinspection unchecked
			((TickManagerSubject<net.minecraft.entity.Entity, TickHandler.EntityHandler>) subject).refresh(this.handlers);
		}
	}

	static class BlockEntity extends AbstractTickManager<net.minecraft.block.entity.BlockEntity, TickHandler.BlockEntityHandler> implements BlockEntityTickManager {
		@Override
		public void refresh(net.minecraft.block.entity.BlockEntity subject) {
			//noinspection unchecked
			((TickManagerSubject<net.minecraft.block.entity.BlockEntity, TickHandler.BlockEntityHandler>) subject).refresh(this.handlers);
		}
	}
}
