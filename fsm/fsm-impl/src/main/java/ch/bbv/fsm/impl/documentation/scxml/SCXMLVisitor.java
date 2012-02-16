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
import ch.bbv.fsm.impl.internal.model.visitor.Visitor;
import ch.bbv.fsm.impl.internal.statemachine.state.State;
import ch.bbv.fsm.impl.internal.statemachine.transition.TransitionInfo;

/**
 * @author Mario Martinez (bbv Software Services AG)
 * 
 * 
 * @param <TStateMachine>
 *            the type of state machine
 * @param <TState>
 *            the type of the states
 * @param <TEvent>
 *            the type of the events
 */
public class SCXMLVisitor<TStateMachine extends StateMachine<TState, TEvent>, TState extends Enum<?>, TEvent extends Enum<?>>
		implements Visitor<TStateMachine, TState, TEvent> {

	private final StringBuffer scxml = new StringBuffer();

	/**
	 * @return
	 */
	public StringBuffer getScxml() {
		return scxml;
	}

	@Override
	public void visitOnEntry(
			final StateMachineDefinition<TStateMachine, TState, TEvent> visitable) {

		write("<scxml xmlns=\"http://www.w3.org/2005/07/scxml\" initial="
				+ adaptName(visitable.getInitialState().toString()));
		write(" version=\"1.0\">");
	}

	@Override
	public void visitOnExit(
			final StateMachineDefinition<TStateMachine, TState, TEvent> visitable) {

		write("</scxml>");
	}

	@Override
	public void visitOnEntry(
			final State<TStateMachine, TState, TEvent> visitable) {

		write("<state id= " + adaptName(visitable.getId().toString()) + ">");
	}

	@Override
	public void visitOnExit(final State<TStateMachine, TState, TEvent> visitable) {

		write("</state>");
	}

	@Override
	public void visitOnEntry(
			final TransitionInfo<TStateMachine, TState, TEvent> visitable) {

		write("<transition event=" + adaptName("fake") + " target="
				+ adaptName(visitable.getTarget().getId().toString()) + "/>");
	}

	@Override
	public void visitOnExit(
			final TransitionInfo<TStateMachine, TState, TEvent> visitable) {
	}

	private String adaptName(final String id) {
		return "\"" + id + "\"";
	}

	private void write(final String msg) {

		scxml.append(msg).append("\n");
	}
}
