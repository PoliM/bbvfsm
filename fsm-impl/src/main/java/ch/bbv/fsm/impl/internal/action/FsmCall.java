package ch.bbv.fsm.impl.internal.action;

import ch.bbv.fsm.StateMachine;

/**
 * Defines the API of the Call implementations.
 *
 * @param <TStateMachine>
 *            the state machine.
 * @param <TState>
 *            the type of the states.
 * @param <TEvent>
 *            the type of the events.
 */
public interface FsmCall<TStateMachine extends StateMachine<TState, TEvent>, TState extends Enum<?>, TEvent extends Enum<?>> {

	/**
	 * The execute method.
	 * 
	 * @param fsm
	 *            the state machine.
	 */
	void execOn(TStateMachine fsm);

	/**
	 * The execute method.
	 * 
	 * @param fsm
	 *            the state machine.
	 * @param args
	 *            the parameters.
	 */
	void execOn(TStateMachine fsm, Object... args);
}
