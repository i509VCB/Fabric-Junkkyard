package me.i509.junkkyard.actor.api;

import static com.google.common.base.Preconditions.checkNotNull;

import me.i509.junkkyard.actor.api.context.ActionContext;

public final class Action {
	private ActionType type;
	private Actor actor;
	private ActionContext context;

	public static Action create(ActionType type, Actor actor, ActionContext context) {
		return new Action(
				checkNotNull(type, "Type cannot be null"),
				checkNotNull(actor, "Actor cannot be null"),
				checkNotNull(context, "Context cannot be null")
		);
	}

	private Action(ActionType type, Actor actor, ActionContext context) {
		this.type = type;
		this.actor = actor;
		this.context = context;
	}

	/**
	 * Gets the id of this action.
	 */
	public ActionType getType() {
		return this.type;
	}

	/**
	 * Gets the actor which has caused this action.
	 */
	public Actor getActor() {
		return this.actor;
	}

	/**
	 * Gets the context in which this action has occurred.
	 */
	public ActionContext getContext() {
		return this.context;
	}
}
