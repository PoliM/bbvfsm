/*******************************************************************************
 *  Copyright 2010, 2011 bbv Software Services AG, Ueli Kurmann
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 * Contributors:
 *     bbv Software Services AG (http://www.bbv.ch), Ueli Kurmann
 *******************************************************************************/
package ch.bbv.fsm.impl.internal.statemachine.state;

import ch.bbv.fsm.HistoryType;
import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.impl.internal.action.ActionHolder;
import ch.bbv.fsm.impl.internal.statemachine.transition.TransitionContext;
import ch.bbv.fsm.impl.internal.statemachine.transition.TransitionDictionary;
import ch.bbv.fsm.impl.internal.statemachine.transition.TransitionResult;
import ch.bbv.fsm.model.State;
import ch.bbv.fsm.model.visitor.Visitor;

/**
 * Represents a state of the state machine.
 * 
 * @author Ueli Kurmann (bbv Software Services AG) (bbv Software Services AG)
 * 
 * @param <TStateMachine>
 *            the type of state machine
 * @param <TState>
 *            the type of the states
 * @param <TEvent>
 *            the type of the events
 */
public interface InternalState<TStateMachine extends StateMachine<TState, TEvent>, TState extends Enum<?>, TEvent extends Enum<?>>
		extends State<TStateMachine, TState, TEvent> {

	/**
	 * Adds a sub state.
	 * 
	 * @param state
	 *            a sub state.
	 */
	void addSubState(InternalState<TStateMachine, TState, TEvent> state);

	/**
	 * Enters this state by its history depending on its
	 * <code>HistoryType</code>. The <code>Entry</code> method has to be called
	 * already.
	 * 
	 * @param stateContext
	 *            the state context.
	 * @return the active state. (depends on this states
	 *         <code>HistoryType</code>)
	 */
	InternalState<TStateMachine, TState, TEvent> enterByHistory(
			StateContext<TStateMachine, TState, TEvent> stateContext);

	/**
	 * Enters this state is deep mode: mode if there is one.
	 * 
	 * @param stateContext
	 *            the event context.
	 * @return the active state.
	 */
	InternalState<TStateMachine, TState, TEvent> enterDeep(
			StateContext<TStateMachine, TState, TEvent> stateContext);

	/**
	 * Enters this state is shallow mode: The entry action is executed and the
	 * initial state is entered in shallow mode if there is one.
	 * 
	 * @param stateContext
	 *            the event context.
	 * @return the active state.
	 */
	InternalState<TStateMachine, TState, TEvent> enterShallow(
			StateContext<TStateMachine, TState, TEvent> stateContext);

	/**
	 * Enters this state.
	 * 
	 * @param stateContext
	 *            the state context.
	 */
	void entry(StateContext<TStateMachine, TState, TEvent> stateContext);

	/**
	 * Exits this state.
	 * 
	 * @param stateContext
	 *            the state context.
	 */
	void exit(StateContext<TStateMachine, TState, TEvent> stateContext);

	/**
	 * Fires the specified event id on this state.
	 * 
	 * @param context
	 *            the event context.
	 * @return the result of the transition.
	 */
	TransitionResult<TStateMachine, TState, TEvent> fire(
			TransitionContext<TStateMachine, TState, TEvent> context);

	/**
	 * Returns the entry action.
	 * 
	 * @return the entry action.
	 */
	ActionHolder<TStateMachine, TState, TEvent> getEntryAction();

	/**
	 * Gets the exit action.
	 * 
	 * @return the exit action.
	 */
	ActionHolder<TStateMachine, TState, TEvent> getExitAction();

	/**
	 * Returns the history type of this state.
	 * 
	 * @return the history type of this state.
	 */
	HistoryType getHistoryType();

	/**
	 * Returns the initial sub-state.
	 * 
	 * @return the initial sub-state or Null if this state has no sub-states.
	 */
	InternalState<TStateMachine, TState, TEvent> getInitialState();

	/**
	 * Returns the sub-states.
	 * 
	 * @return the sub-states.
	 */
	Iterable<State<TStateMachine, TState, TEvent>> getSubStates();

	/**
	 * Returns the super-state. Null if this is a root state.
	 * 
	 * @return the super-state.
	 */
	InternalState<TStateMachine, TState, TEvent> getSuperState();

	/**
	 * Returns the transitions.
	 * 
	 * @return the transitions.
	 */
	TransitionDictionary<TStateMachine, TState, TEvent> getTransitions();

	/**
	 * Sets the entry action.
	 * 
	 * @param <T>
	 * @param action
	 *            the entry action.
	 */
	void setEntryAction(ActionHolder<TStateMachine, TState, TEvent> action);

	/**
	 * Sets the exit action.
	 * 
	 * @param <T>
	 * @param action
	 *            the exit action.
	 */
	void setExitAction(ActionHolder<TStateMachine, TState, TEvent> action);

	/**
	 * Sets the history type of this state.
	 * 
	 * @param historyType
	 *            the history type of this state.
	 */
	void setHistoryType(HistoryType historyType);

	/**
	 * Sets the initial sub-state.
	 * 
	 * @param initialState
	 *            the initial sub-state.
	 */
	void setInitialState(
			InternalState<TStateMachine, TState, TEvent> initialState);

	/**
	 * Sets the super-state.
	 * 
	 * @param superState
	 *            the super-state.
	 */
	void setSuperState(InternalState<TStateMachine, TState, TEvent> superState);

	/**
	 * Accepts a {@link #Visitor}.
	 * 
	 * @param visitor
	 *            the visitor.
	 */
	@Override
	void accept(final Visitor<TStateMachine, TState, TEvent> visitor);

}
