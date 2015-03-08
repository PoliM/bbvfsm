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
import ch.bbv.fsm.events.TransitionExceptionEventArgs;
import ch.bbv.fsm.impl.internal.statemachine.state.StateContext;

/**
 * See {@link TransitionExceptionEventArgs}.
 * 
 * @param <TState>
 *            the state enumeration
 * @param <TEvent>
 *            the event enumeration
 * @param <TStateMachine>
 *            the type of the state machine
 */
public class TransitionExceptionEventArgsImpl<TStateMachine extends StateMachine<TState, TEvent>, TState extends Enum<?>, TEvent extends Enum<?>>
		extends TransitionEventArgsImpl<TStateMachine, TState, TEvent> implements
		TransitionExceptionEventArgs<TStateMachine, TState, TEvent> {

	/**
	 * The exception.
	 */
	private final Exception exception;

	/**
	 * Constructor.
	 * 
	 * @param context
	 *            the event context
	 * @param exception
	 *            the exception
	 */
	public TransitionExceptionEventArgsImpl(final StateContext<TStateMachine, TState, TEvent> context, final Exception exception) {
		super(context);
		this.exception = exception;
	}

	@Override
	public Exception getException() {
		return this.exception;
	}
}
