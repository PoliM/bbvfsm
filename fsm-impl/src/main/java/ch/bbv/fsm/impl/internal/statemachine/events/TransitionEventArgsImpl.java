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
package ch.bbv.fsm.impl.internal.statemachine.events;

import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.events.TransitionEventArgs;
import ch.bbv.fsm.impl.internal.statemachine.state.StateContext;
import ch.bbv.fsm.impl.internal.statemachine.transition.TransitionContext;

/**
 * See {@link ContextEventArgsImpl}.
 * 
 * @param <TState>
 *            the state enumeration
 * @param <TEvent>
 *            the event enumeration
 * @param <TStateMachine>
 *            the type of the state machine
 */
public class TransitionEventArgsImpl<TStateMachine extends StateMachine<TState, TEvent>, TState extends Enum<?>, TEvent extends Enum<?>>
		extends ContextEventArgsImpl<TStateMachine, TState, TEvent> implements TransitionEventArgs<TStateMachine, TState, TEvent> {
	/**
	 * Initializes a new instance.
	 * 
	 * @param transitionContext
	 *            the transition context.
	 */
	public TransitionEventArgsImpl(final StateContext<TStateMachine, TState, TEvent> transitionContext) {
		super(transitionContext);
	}

	@Override
	public Object[] getEventArguments() {
		return this.getTransitionContext().getEventArguments();
	}

	@Override
	public TEvent getEventId() {
		return this.getTransitionContext().getEventId();
	}

	@Override
	public TState getStateId() {
		return this.getTransitionContext().getState().getId();
	}

	/**
	 * Returns the transition context.
	 * 
	 * @return the transition context.
	 */
	private TransitionContext<TStateMachine, TState, TEvent> getTransitionContext() {
		return (TransitionContext<TStateMachine, TState, TEvent>) this.getStateContext();
	}

	/**
	 * Represents the transition as a string.
	 */
	@Override
	public String toString() {
		return String.format("Transition from state %s on event %s.", this.getStateId(), this.getEventId());
	}
}
