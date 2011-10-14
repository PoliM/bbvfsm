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
package ch.bbv.fsm.impl.internal.statemachine.transition;

import java.util.List;

import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.action.Action;
import ch.bbv.fsm.guard.Function;
import ch.bbv.fsm.impl.internal.statemachine.state.State;

/**
 * The transition between two states.
 * 
 * @author Ueli Kurmann (bbv Software Services AG) (bbv Software Services AG)s
 * 
 * @param <TStateMachine>
 *            the type of state machine
 * @param <TState>
 *            the type of the states
 * @param <TEvent>
 *            the type of the events
 */
public interface Transition<TStateMachine extends StateMachine<TState, TEvent>, TState extends Enum<?>, TEvent extends Enum<?>> {

	/**
	 * Fires the transition.
	 * 
	 * @param context
	 *            the event context.
	 * @return The result of the transition.
	 */
	TransitionResult<TStateMachine, TState, TEvent> fire(TransitionContext<TStateMachine, TState, TEvent> context);

	/**
	 * Gets the actions of this transition.
	 * 
	 * @return the actions of this transition.
	 */
	List<Action<TStateMachine, TState, TEvent>> getActions();

	/**
	 * Gets or sets the guard of this transition.
	 * 
	 * @return the guard.
	 */
	Function<TStateMachine, TState, TEvent, Object[], Boolean> getGuard();

	/**
	 * Returns the source state of the transition.
	 * 
	 * @return the source state of the transition.
	 */
	State<TStateMachine, TState, TEvent> getSource();

	/**
	 * Gets the target state of the transition.
	 * 
	 * @return the target state of the transition.
	 */

	State<TStateMachine, TState, TEvent> getTarget();

	/**
	 * Sets the guard function.
	 * 
	 * @param guard
	 *            the guard function.
	 */
	void setGuard(Function<TStateMachine, TState, TEvent, Object[], Boolean> guard);

	/**
	 * Sets the source state of the transition.
	 * 
	 * @param sourceState
	 *            the source state of the transition.
	 */
	void setSource(State<TStateMachine, TState, TEvent> sourceState);

	/**
	 * Sets the target state.
	 * 
	 * @param target
	 *            the target state.
	 */
	void setTarget(State<TStateMachine, TState, TEvent> target);

	/**
	 * Sets the target state of the transition.
	 * 
	 * @param targetState
	 *            the target state of the transition.
	 */
	void setTargetState(State<TStateMachine, TState, TEvent> targetState);

}
