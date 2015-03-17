package ch.bbv.fsm.action;

import ch.bbv.fsm.StateMachine;

/**
 * A functional interface that can execute actions without a parameter.
 *
 * @param <FSM>
 *            The concrete type of the FSM.
 */
@FunctionalInterface
public interface FsmAction0<TStateMachine extends StateMachine<TState, TEvent>, TState extends Enum<?>, TEvent extends Enum<?>> {

	/**
	 * The functional interface method.
	 * 
	 * @param fsm
	 *            The instance of the FSM on which the method will be called.
	 */
	void exec(TStateMachine fsm);
}