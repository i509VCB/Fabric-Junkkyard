package me.i509.junkkyard.ticking.api;

public interface TickManager<S, H extends TickHandler<S>> {
	void register(H handler);

	void refresh(S subject);
}
