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
package ch.bbv.fsm.impl.documentation.scxml;

import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.StateMachineDefinition;
import ch.bbv.fsm.documentation.DocumentationGenerator;
import ch.bbv.fsm.impl.AbstractStateMachineDefinition;
import ch.bbv.fsm.impl.SimpleStateMachine;

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
public class SCXMLGenerator<TStateMachine extends StateMachine<TState, TEvent>, TState extends Enum<?>, TEvent extends Enum<?>>
		implements
		DocumentationGenerator<StringBuffer, TStateMachine, TState, TEvent> {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public StringBuffer generateDocumentation(
			final StateMachineDefinition<TStateMachine, TState, TEvent> stateMachineDefinition) {

		SCXMLVisitor<SimpleStateMachine<TState, TEvent>, TState, TEvent> visitor = new SCXMLVisitor<SimpleStateMachine<TState, TEvent>, TState, TEvent>();

		((AbstractStateMachineDefinition) stateMachineDefinition)
				.accept(visitor);

		return visitor.getScxml();
	}

	@Override
	public StringBuffer generateDecisionTables(
			final StateMachineDefinition<TStateMachine, TState, TEvent> stateMachineDefinition) {

		throw new RuntimeException("Operation not implemented.");
	}
}