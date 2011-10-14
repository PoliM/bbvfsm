package ch.bbv.fsm.impl;

import ch.bbv.fsm.StateMachine;

/**
 * A simple state machine's definition. This is used if no special type should be created.
 * 
 * @param <TState>
 *            the type of the states
 * @param <TEvent>
 *            the type of the events
 */
public class SimpleStateMachineDefinition<TState extends Enum<?>, TEvent extends Enum<?>> extends
		AbstractStateMachineDefinition<SimpleStateMachine<TState, TEvent>, TState, TEvent> {

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            the name of the definition
	 * @param initialState
	 *            the default inital state to use
	 */
	public SimpleStateMachineDefinition(final String name, final TState initialState) {
		super(name, initialState);
	}

	@Override
	protected SimpleStateMachine<TState, TEvent> createStateMachine(final StateMachine<TState, TEvent> driver) {
		return new SimpleStateMachine<TState, TEvent>(driver);
	}

}
