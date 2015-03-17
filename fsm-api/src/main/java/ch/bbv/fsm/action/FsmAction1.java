package ch.bbv.fsm.action;

import ch.bbv.fsm.StateMachine;

/**
 * A functional interface that can execute actions with one parameter.
 *
 * @param <FSM>
 *            The concrete type of the FSM.
 * @param <P1>
 *            The type of the first parameter.
 */
@FunctionalInterface
public interface FsmAction1<TStateMachine extends StateMachine<TState, TEvent>, TState extends Enum<?>, TEvent extends Enum<?>, P1> {

	/**
	 * The functional interface method.
	 * 
	 * @param fsm
	 *            The instance of the FSM on which the method will be called.
	 * @param p1
	 *            The first parameter to the referenced method.
	 */
	void exec(TStateMachine fsm, P1 p1);
}