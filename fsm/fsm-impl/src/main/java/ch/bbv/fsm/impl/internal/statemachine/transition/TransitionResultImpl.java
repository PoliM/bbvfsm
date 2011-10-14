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
import ch.bbv.fsm.impl.internal.statemachine.state.State;

/**
 * The implementation of {@link TransitionResult}.
 * 
 * @param <TStateMachine>
 *            the type of state machine
 * @param <TState>
 *            the type of the states
 * @param <TEvent>
 *            the type of the events
 */
public class TransitionResultImpl<TStateMachine extends StateMachine<TState, TEvent>, TState extends Enum<?>, TEvent extends Enum<?>>
		implements TransitionResult<TStateMachine, TState, TEvent> {

	/**
	 * This value represents that no transition was fired.
	 */
	private final boolean fired;
	private final State<TStateMachine, TState, TEvent> newState;
	private final List<? extends Throwable> exceptions;

	/**
	 * Initializes a new instance of the TransitionResultImpl class.
	 * 
	 * @param fired
	 *            true the transition was fired.
	 * @param newState
	 *            the new state
	 * @param exceptions
	 *            the exceptions
	 */
	public TransitionResultImpl(final boolean fired, final State<TStateMachine, TState, TEvent> newState,
			final List<? extends Throwable> exceptions) {
		this.fired = fired;
		this.newState = newState;
		this.exceptions = exceptions;
	}

	/**
	 * Returns the list of exceptions.
	 * 
	 * @return the list of exceptions.
	 */
	public List<? extends Throwable> getExceptions() {
		return this.exceptions;
	}

	@Override
	public State<TStateMachine, TState, TEvent> getNewState() {
		return this.newState;
	}

	@Override
	public boolean isFired() {
		return this.fired;
	}

	/**
	 * Creates a not fired result.
	 * 
	 * @param <TStateMachine>
	 *            the type of state machine
	 * @param <TState>
	 *            the type of the states
	 * @param <TEvent>
	 *            the type of the events
	 */
	public static <TStateMachine extends StateMachine<TState, TEvent>, TState extends Enum<?>, TEvent extends Enum<?>> TransitionResult<TStateMachine, TState, TEvent> getNotFired() {
		return new TransitionResultImpl<TStateMachine, TState, TEvent>(false, null, null);

	}
}
