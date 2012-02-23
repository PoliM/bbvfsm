package ch.bbv.fsm.impl;

import java.util.LinkedList;
import java.util.List;

import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.impl.internal.statemachine.state.InternalState;
import ch.bbv.fsm.impl.internal.statemachine.state.StateDictionary;
import ch.bbv.fsm.model.State;
import ch.bbv.fsm.model.StateMachineModel;
import ch.bbv.fsm.model.visitor.Visitor;

/**
 * Implementation of the definition of the finite state machine.
 * 
 * @param <TState>
 *            the type of the states.
 * @param <TEvent>
 *            the type of the events.
 * @param <TStateMachine>
 *            the type of the state machine
 */
public class SimpleStateMachineModel<TStateMachine extends StateMachine<TState, TEvent>, TState extends Enum<?>, TEvent extends Enum<?>>
		implements StateMachineModel<TStateMachine, TState, TEvent> {

	/**
	 * The dictionary of all states.
	 */
	private final StateDictionary<TStateMachine, TState, TEvent> states;

	private final TState initialState;

	/**
	 * Accepts a {@link #Visitor}.
	 * 
	 * @param visitor
	 *            the visitor.
	 */
	@Override
	public void accept(final Visitor<TStateMachine, TState, TEvent> visitor) {

		visitor.visitOnEntry(this);

		for (State<TStateMachine, TState, TEvent> state : this
				.getRootStates()) {
			state.accept(visitor);
		}

		visitor.visitOnExit(this);
	}

	/**
	 * @return The dictionary of all states.
	 */
	protected StateDictionary<TStateMachine, TState, TEvent> getStates() {
		return states;
	}

	/**
	 * @param states
	 *            the states to use.
	 * @param initialState
	 *            the initial InternalState.
	 */
	public SimpleStateMachineModel(
			final StateDictionary<TStateMachine, TState, TEvent> states,
			final TState initialState) {

		this.states = new StateDictionary<TStateMachine, TState, TEvent>();
		this.initialState = initialState;
	}

	@Override
	public final TState getInitialState() {
		return this.initialState;
	}

	/**
	 * @return
	 */
	private List<State<TStateMachine, TState, TEvent>> getRootStates() {

		List<State<TStateMachine, TState, TEvent>> rootStates = new LinkedList<State<TStateMachine, TState, TEvent>>();

		for (InternalState<TStateMachine, TState, TEvent> state : this.states
				.getStates()) {

			if (isStateInRootSuperState(state)) {
				rootStates.add(state);
			}
		}

		return rootStates;
	}

	/**
	 * @param state
	 * @return
	 */
	private boolean isStateInRootSuperState(
			final InternalState<TStateMachine, TState, TEvent> state) {
		return !hasSuperState(state);
	}

	/**
	 * @param state
	 * @return
	 */
	private boolean hasSuperState(
			final InternalState<TStateMachine, TState, TEvent> state) {
		return state.getSuperState() != null;
	}

}
