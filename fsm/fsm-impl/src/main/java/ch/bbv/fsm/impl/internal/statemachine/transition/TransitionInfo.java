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

import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.impl.internal.statemachine.state.State;

/**
 * Describes a transition.
 * 
 * @author Ueli Kurmann (bbv Software Services AG) (bbv Software Services AG)
 * 
 * @param <TStateMachine>
 *            the type of state machine
 * @param <TState>
 *            the type of the states
 * @param <TEvent>
 *            the type of the events
 */
class TransitionInfo<TStateMachine extends StateMachine<TState, TEvent>, TState extends Enum<?>, TEvent extends Enum<?>> {
	private TEvent eventId;
	private State<TStateMachine, TState, TEvent> source;
	private State<TStateMachine, TState, TEvent> target;
	private boolean hasGuard;
	private int actions;

	/**
	 * Creates a new instance.
	 * 
	 * @param eventId
	 *            the event id.
	 * @param source
	 *            the source state
	 * @param target
	 *            the target state
	 * @param hasGuard
	 *            true if a guard exists
	 * @param actions
	 *            the number of actions
	 */
	public TransitionInfo(final TEvent eventId, final State<TStateMachine, TState, TEvent> source, final State<TStateMachine, TState, TEvent> target, final boolean hasGuard,
			final int actions) {
		this.eventId = eventId;
		this.source = source;
		this.target = target;
		this.hasGuard = hasGuard;
		this.actions = actions;
	}

	/**
	 * Returns the number of actions.
	 * 
	 * @return the number of actions.
	 */
	public int getActions() {
		return this.actions;
	}

	/**
	 * Returns the event id.
	 * 
	 * @return the event id.
	 */
	public TEvent getEventId() {
		return this.eventId;
	}

	/**
	 * Returns the source state.
	 * 
	 * @return the source state.
	 */
	public State<TStateMachine, TState, TEvent> getSource() {
		return this.source;
	}

	/**
	 * Returns the target state.
	 * 
	 * @return the target state.
	 */
	public State<TStateMachine, TState, TEvent> getTarget() {
		return this.target;
	}

	/**
	 * Returns true if this transition has a guard.
	 * 
	 * @return true if this transition has a guard.
	 */
	public boolean hasGuard() {
		return this.hasGuard;
	}

	/**
	 * Sets the number of actions.
	 * 
	 * @param numberOfActions
	 *            the number of actions.
	 */
	public void setActions(final int numberOfActions) {
		this.actions = numberOfActions;
	}

	/**
	 * Sets the event id.
	 * 
	 * @param eventId
	 *            the event id.
	 */
	public void setEventId(final TEvent eventId) {
		this.eventId = eventId;
	}

	/**
	 * Sets the guard flag.
	 * 
	 * @param guard
	 *            true if has a guard
	 */
	public void setGuard(final boolean guard) {
		this.hasGuard = guard;
	}

	/**
	 * Sets the source state.
	 * 
	 * @param source
	 *            the source state.
	 */
	public void setSource(final State<TStateMachine, TState, TEvent> source) {
		this.source = source;
	}

	/**
	 * Sets the target state.
	 * 
	 * @param target
	 *            the target state.
	 */
	public void setTarget(final State<TStateMachine, TState, TEvent> target) {
		this.target = target;
	}

}
