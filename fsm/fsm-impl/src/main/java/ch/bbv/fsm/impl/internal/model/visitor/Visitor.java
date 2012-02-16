/*******************************************************************************
 *  Copyright 2010, 2011 bbv Software Services AG, Mario Martinez
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
 *     bbv Software Services AG (http://www.bbv.ch), Mario Martinez
 *******************************************************************************/
package ch.bbv.fsm.impl.internal.model.visitor;

import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.StateMachineDefinition;
import ch.bbv.fsm.impl.internal.statemachine.state.State;
import ch.bbv.fsm.impl.internal.statemachine.transition.TransitionInfo;

/**
 * @author Mario Martinez (bbv Software Services AG)
 * 
 * @param <TStateMachine>
 *            the type of state machine
 * @param <TState>
 *            the type of the states
 * @param <TEvent>
 *            the type of the events
 */
public interface Visitor<TStateMachine extends StateMachine<TState, TEvent>, TState extends Enum<?>, TEvent extends Enum<?>> {

	/**
	 * @param visitable
	 *            the element to be visited.
	 */
	void visitOnEntry(
			StateMachineDefinition<TStateMachine, TState, TEvent> visitable);

	/**
	 * @param visitable
	 *            the element to be visited.
	 */
	void visitOnExit(
			StateMachineDefinition<TStateMachine, TState, TEvent> visitable);

	/**
	 * @param visitable
	 *            the element to be visited.
	 */
	void visitOnEntry(State<TStateMachine, TState, TEvent> visitable);

	/**
	 * @param visitable
	 *            the element to be visited.
	 */
	void visitOnExit(State<TStateMachine, TState, TEvent> visitable);

	/**
	 * @param visitable
	 *            the element to be visited.
	 */
	void visitOnEntry(TransitionInfo<TStateMachine, TState, TEvent> visitable);

	/**
	 * @param visitable
	 *            the element to be visited.
	 */
	void visitOnExit(TransitionInfo<TStateMachine, TState, TEvent> visitable);

}
