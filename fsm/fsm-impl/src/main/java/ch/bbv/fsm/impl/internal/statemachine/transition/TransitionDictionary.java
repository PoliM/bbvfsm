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

/**
 * Interface for the mapping between states and their transitions.
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
public interface TransitionDictionary<TStateMachine extends StateMachine<TState, TEvent>, TState extends Enum<?>, TEvent extends Enum<?>> {

	/**
	 * Adds a transition to an event.
	 * 
	 * @param eventId
	 *            the event id
	 * @param transition
	 *            the transition
	 */
	void add(TEvent eventId, Transition<TStateMachine, TState, TEvent> transition);

	/**
	 * Returns all transitions.
	 * 
	 * @return all transitions.
	 */
	List<InternalTransitionInfo<TStateMachine, TState, TEvent>> getTransitions();

	/**
	 * Returns a list of transitions for the given event.
	 * 
	 * @param eventId
	 *            the event id
	 * @return a list of transitions
	 */
	List<Transition<TStateMachine, TState, TEvent>> getTransitions(TEvent eventId);

}
