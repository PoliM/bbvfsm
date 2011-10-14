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
import ch.bbv.fsm.guard.Function;

/**
 * Guard a transition.
 * 
 * @param <TState>
 *            the type of the states.
 * @param <TEvent>
 *            the type of the events.
 * @param <TStateMachine>
 *            the type of state machine
 */
public interface GuardSyntax<TStateMachine extends StateMachine<TState, TEvent>, TState extends Enum<?>, TEvent extends Enum<?>> extends
		EventSyntax<TStateMachine, TState, TEvent> {

	/**
	 * Defines a guard for a transition. A boolean itself is not valid. There needs to be a function call using from(owner).function
	 * returning a boolean.
	 * 
	 * @param guard
	 *            function call using the from construct.
	 * @return the event syntax.
	 */
	EventSyntax<TStateMachine, TState, TEvent> onlyIf(boolean guard);

	/**
	 * Defines a guard for a transition.
	 * 
	 * @param guard
	 *            the guard.
	 * @return Event syntax.
	 */
	EventSyntax<TStateMachine, TState, TEvent> onlyIf(Function<TStateMachine, TState, TEvent, Object[], Boolean> guard);
}
