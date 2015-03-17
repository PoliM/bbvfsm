/*******************************************************************************
 * Copyright 2010, 2011 bbv Software Services AG, Ueli Kurmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 * Contributors: bbv Software Services AG (http://www.bbv.ch), Ueli Kurmann
 *******************************************************************************/
package ch.bbv.fsm.dsl;

import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.action.FsmAction0;
import ch.bbv.fsm.action.FsmAction1;
import ch.bbv.fsm.action.FsmAction2;

/**
 * Possibilities to execute an action.
 *
 * @param <TStateMachine>
 *            the type of state machine
 * @param <TState>
 *            the type of the states.
 * @param <TEvent>
 *            the type of the events.
 */
public interface ExecuteSyntax<TStateMachine extends StateMachine<TState, TEvent>, TState extends Enum<?>, TEvent extends Enum<?>>
		extends GuardSyntax<TStateMachine, TState, TEvent> {

	/**
	 * Defines the actions to execute on a transition.
	 *
	 * @param action
	 *            The action class.
	 */
	ExecuteSyntax<TStateMachine, TState, TEvent> execute(
			FsmAction0<TStateMachine, TState, TEvent> action);

	/**
	 * Defines the actions to execute on a transition.
	 * 
	 * @param action
	 * @param <T>
	 * 
	 * @return
	 */
	<T> ExecuteSyntax<TStateMachine, TState, TEvent> execute(
			FsmAction1<TStateMachine, TState, TEvent, T> action);

	/**
	 * Defines the actions to execute on a transition.
	 * 
	 * @param action
	 * @param <T1>
	 * @param <T2>
	 * @return
	 */
	<T1, T2> ExecuteSyntax<TStateMachine, TState, TEvent> execute(
			FsmAction2<TStateMachine, TState, TEvent, T1, T2> action);

}
