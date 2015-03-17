package ch.bbv.fsm.action;

import ch.bbv.fsm.StateMachine;


/**
 * A functional interface that can execute actions without a parameter.
 *
 * @param <TStateMachine>
 * @param <TState>
 * @param <TEvent>
 * @param <P1>
 * @param <P2>
 */
@FunctionalInterface
public interface FsmAction2<TStateMachine extends StateMachine<TState, TEvent>, TState extends Enum<?>, TEvent extends Enum<?>, P1, P2> {

	/**
	 * The functional interface method.
	 * 
	 * @param fsm
	 *            The instance of the FSM on which the method will be called.
	 * @param p1
	 *            The first parameter to the referenced method.
	 * @param p2
	 *            The second parameter to the referenced method.
	 */
	void exec(TStateMachine fsm, P1 p1, P2 p2);
}