package ch.bbv.fsm.impl;

import ch.bbv.fsm.StateMachine;

/**
 * Simple state machine (used together with {@link SimpleStateMachineDefinition}.
 * 
 * @param <TState>
 *            the type of the states
 * @param <TEvent>
 *            the type of the events
 */
public class SimpleStateMachine<TState extends Enum<?>, TEvent extends Enum<?>> extends AbstractStateMachine<SimpleStateMachine<TState, TEvent>, TState, TEvent> {

	protected SimpleStateMachine(final StateMachine<TState, TEvent> driver) {
		super(driver);
	}
}
