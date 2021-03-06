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
package ch.bbv.fsm.dsl;

import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.action.FsmAction0;
import ch.bbv.fsm.action.FsmAction1;
import ch.bbv.fsm.action.FsmAction2;

/**
 * Possibilities to execute an action on exit.
 * 
 * @param <TStateMachine>
 *            the type of the state machine
 * @param <TState>
 *            the type of the states.
 * @param <TEvent>
 *            the type of the events.
 */
public interface ExitActionSyntax<TStateMachine extends StateMachine<TState, TEvent>, TState extends Enum<?>, TEvent extends Enum<?>>
		extends EventSyntax<TStateMachine, TState, TEvent> {

	/**
	 * Defines an exit action.
	 * 
	 * @param actionClass
	 *            the {@link FsmAction0} Class
	 * @return the EventSyntax
	 */
	EventSyntax<TStateMachine, TState, TEvent> executeOnExit(
			FsmAction0<TStateMachine, TState, TEvent> actionClass);

	/**
	 * Defines an entry action.
	 * 
	 * @param <T>
	 *            The return type of the action.
	 * @param actionClass
	 *            The {@link FsmAction1} class.
	 * @param parameter
	 *            (necessary?)
	 * @return the EventSyntax
	 */
	<T> EventSyntax<TStateMachine, TState, TEvent> executeOnExit(
			FsmAction1<TStateMachine, TState, TEvent, T> actionClass,
			T parameter);

	/**
	 *  Defines an entry action.
	 * @param actionClass the action. 
	 * @param <T1> the type of the first parameter.
	 * @param <T2> the type of the second parameter.
	 * @param parameter1 the value of the first parameter.
	 * @param parameter2 the value of the second parameter.
	 * @return
	 */
	<T1, T2> EventSyntax<TStateMachine, TState, TEvent> executeOnExit(
			FsmAction2<TStateMachine, TState, TEvent, T1, T2> actionClass,
			T1 parameter1, T2 parameter2);

}
