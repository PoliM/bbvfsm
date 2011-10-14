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
package ch.bbv.fsm.events;

import ch.bbv.fsm.StateMachine;

/**
 * Event handler of the state machine.
 * 
 * @author Ueli Kurmann (bbv Software Services AG) (bbv Software Services AG)
 * @param <TState>
 *            the type of the states.
 * @param <TEvent>
 *            the type of the events.
 * @param <TStateMachine>
 *            the type of the state machine
 */
public interface StateMachineEventHandler<TStateMachine extends StateMachine<TState, TEvent>, TState extends Enum<?>, TEvent extends Enum<?>> {

	/**
	 * Occurs when an exception was thrown inside the state machine.
	 * 
	 * @param arg
	 *            the event argument.
	 */
	void onExceptionThrown(ExceptionEventArgs<TStateMachine, TState, TEvent> arg);

	/**
	 * Occurs when a transition begins.
	 * 
	 * @param args
	 *            the event argument.
	 */
	void onTransitionBegin(TransitionEventArgs<TStateMachine, TState, TEvent> args);

	/**
	 * Occurs when a transition completed.
	 * 
	 * @param arg
	 *            the completion event
	 */
	void onTransitionCompleted(TransitionCompletedEventArgs<TStateMachine, TState, TEvent> arg);

	/**
	 * Occurs when no transition could be executed.
	 * 
	 * @param arg
	 *            the event argument.
	 */
	void onTransitionDeclined(TransitionEventArgs<TStateMachine, TState, TEvent> arg);

	/**
	 * Occurs when an exception was thrown inside a transition of the state machine.
	 * 
	 * @param arg
	 *            the event argument.
	 */
	void onTransitionThrowsException(TransitionExceptionEventArgs<TStateMachine, TState, TEvent> arg);

}
