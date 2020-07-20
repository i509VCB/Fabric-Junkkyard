package me.i509.junkkyard.ticking.impl;

import java.util.Set;

import me.i509.junkkyard.ticking.api.TickHandler;

public interface TickManagerSubject<S, H extends TickHandler<S>> {
	void beforeTick();

	void afterTick();

	void refresh(Set<H> handlers);
}
