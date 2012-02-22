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
package ch.bbv.fsm.impl.internal.statemachine.state;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.bbv.fsm.HistoryType;
import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.impl.internal.action.ActionHolder;
import ch.bbv.fsm.impl.internal.statemachine.state.StateContext.RecordType;
import ch.bbv.fsm.impl.internal.statemachine.transition.Transition;
import ch.bbv.fsm.impl.internal.statemachine.transition.TransitionContext;
import ch.bbv.fsm.impl.internal.statemachine.transition.TransitionDictionary;
import ch.bbv.fsm.impl.internal.statemachine.transition.TransitionDictionaryImpl;
import ch.bbv.fsm.impl.internal.statemachine.transition.TransitionResult;
import ch.bbv.fsm.impl.internal.statemachine.transition.TransitionResultImpl;
import ch.bbv.fsm.model.TransitionInfo;
import ch.bbv.fsm.model.visitor.Visitor;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

/**
 * Implementation of the state.
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
public class InternalStateImpl<TStateMachine extends StateMachine<TState, TEvent>, TState extends Enum<?>, TEvent extends Enum<?>>
		implements InternalState<TStateMachine, TState, TEvent> {

	private static final Logger LOG = LoggerFactory.getLogger(InternalStateImpl.class);

	/**
	 * The level of this state within the state hierarchy [1..maxLevel].
	 */
	private int level;

	private final List<InternalState<TStateMachine, TState, TEvent>> subStates;

	/**
	 * The super-state of this state. Null for states with <code>level</code>
	 * equal to 1.
	 */
	private InternalState<TStateMachine, TState, TEvent> superState;

	/**
	 * Collection of transitions that start in this .
	 * (Transition<TState,TEvent>.getSource() is equal to this state)
	 */
	private final TransitionDictionary<TStateMachine, TState, TEvent> transitions;

	/**
	 * The initial sub-state of this state.
	 */
	private InternalState<TStateMachine, TState, TEvent> initialState;

	/**
	 * The HistoryType of this state.
	 */
	private HistoryType historyType = HistoryType.NONE;

	/**
	 * The unique state id.
	 */
	private final TState id;

	/**
	 * The entry action.
	 */
	private ActionHolder<TStateMachine, TState, TEvent> entryAction;

	/**
	 * The exit action.
	 */
	private ActionHolder<TStateMachine, TState, TEvent> exitAction;

	/**
	 * Initializes a new instance of the state.
	 * 
	 * @param id
	 *            the unique id of the state.
	 */
	public InternalStateImpl(final TState id) {
		this.id = id;
		this.level = 1;

		this.subStates = Lists.newArrayList();
		this.transitions = new TransitionDictionaryImpl<TStateMachine, TState, TEvent>(
				this);
	}

	@Override
	public void addSubState(final InternalState<TStateMachine, TState, TEvent> state) {
		this.subStates.add(state);

	}

	@Override
	public void accept(final Visitor<TStateMachine, TState, TEvent> visitor) {

		visitor.visitOnEntry(this);

		for (TransitionInfo<TStateMachine, TState, TEvent> transition : this
				.getTransitions().getTransitions()) {
			transition.accept(visitor);
		}
		for (InternalState<TStateMachine, TState, TEvent> subState : this
				.getSubStates()) {
			subState.accept(visitor);
		}

		visitor.visitOnExit(this);
	}

	/**
	 * Throws an exception if the new initial state is not a sub-state of this
	 * instance.
	 * 
	 * @param value
	 */
	private void checkInitialStateIsASubState(
			final InternalState<TStateMachine, TState, TEvent> value) {
		if (value.getSuperState() != this) {
			throw new IllegalArgumentException(
					String.format(
							"InternalState {0} cannot be the initial state of super state {1} because it is not a direct sub-state.",
							value, this));
		}
	}

	/**
	 * Throws an exception if the new initial state is this instance.
	 * 
	 * @param newInitialState
	 *            the new initial state.
	 */
	private void checkInitialStateIsNotThisInstance(
			final InternalState<TStateMachine, TState, TEvent> newInitialState) {
		if (this == newInitialState) {
			throw new IllegalArgumentException(String.format(
					"InternalState {0} cannot be the initial sub-state to itself.",
					this));
		}
	}

	/**
	 * Throws an exception if the new super state is this instance.
	 * 
	 * @param newSuperState
	 *            the super state.
	 */
	private void checkSuperStateIsNotThisInstance(
			final InternalState<TStateMachine, TState, TEvent> newSuperState) {
		if (this == newSuperState) {
			throw new IllegalArgumentException(String.format(

			"InternalState {0} cannot be its own super-state.", this));
		}
	}

	@Override
	public InternalState<TStateMachine, TState, TEvent> enterByHistory(
			final StateContext<TStateMachine, TState, TEvent> stateContext) {

		InternalState<TStateMachine, TState, TEvent> result = this;

		switch (this.historyType) {
		case NONE:
			result = enterHistoryNone(stateContext);
			break;
		case SHALLOW:
			result = enterHistoryShallow(stateContext);
			break;
		case DEEP:
			result = enterHistoryDeep(stateContext);
			break;
		default:
			throw new IllegalArgumentException("Unknown HistoryType : "
					+ historyType);
		}

		return result;
	}

	@Override
	public InternalState<TStateMachine, TState, TEvent> enterDeep(
			final StateContext<TStateMachine, TState, TEvent> stateContext) {
		this.entry(stateContext);
		final InternalState<TStateMachine, TState, TEvent> lastActiveState = stateContext
				.getLastActiveSubState(this);
		return lastActiveState == null ? this : lastActiveState
				.enterDeep(stateContext);
	}

	/**
	 * Enters this instance with history type = deep.
	 * 
	 * @param stateContext
	 *            the state context.
	 * @return the state
	 */
	private InternalState<TStateMachine, TState, TEvent> enterHistoryDeep(
			final StateContext<TStateMachine, TState, TEvent> stateContext) {
		final InternalState<TStateMachine, TState, TEvent> lastActiveState = stateContext
				.getLastActiveSubState(this);
		return lastActiveState != null ? lastActiveState
				.enterDeep(stateContext) : this;
	}

	/**
	 * Enters with history type = none.
	 * 
	 * @param stateContext
	 *            state context
	 * @return the entered state.
	 */
	private InternalState<TStateMachine, TState, TEvent> enterHistoryNone(
			final StateContext<TStateMachine, TState, TEvent> stateContext) {
		return this.initialState != null ? this.getInitialState().enterShallow(
				stateContext) : this;
	}

	/**
	 * Enters this instance with history type = shallow.
	 * 
	 * @param stateContext
	 *            state context
	 * @return the entered state
	 */
	private InternalState<TStateMachine, TState, TEvent> enterHistoryShallow(
			final StateContext<TStateMachine, TState, TEvent> stateContext) {
		final InternalState<TStateMachine, TState, TEvent> lastActiveState = stateContext
				.getLastActiveSubState(this);
		return lastActiveState != null ? lastActiveState
				.enterShallow(stateContext) : this;
	}

	@Override
	public InternalState<TStateMachine, TState, TEvent> enterShallow(
			final StateContext<TStateMachine, TState, TEvent> stateContext) {
		this.entry(stateContext);

		return this.initialState == null ? this : this.initialState
				.enterShallow(stateContext);

	}

	@Override
	public void entry(
			final StateContext<TStateMachine, TState, TEvent> stateContext) {
		stateContext.addRecord(this.getId(), RecordType.Enter);
		if (this.entryAction != null) {
			try {
				this.entryAction.execute(stateContext);
			} catch (final Exception e) {
				handleException(e, stateContext);
			}
		}

	}

	@Override
	public void exit(
			final StateContext<TStateMachine, TState, TEvent> stateContext) {
		stateContext.addRecord(this.getId(), StateContext.RecordType.Exit);
		if (this.exitAction != null) {
			try {
				this.exitAction.execute(stateContext);
			} catch (final Exception e) {
				handleException(e, stateContext);
			}
		}
		this.setThisStateAsLastStateOfSuperState(stateContext);
	}

	private void setThisStateAsLastStateOfSuperState(
			final StateContext<TStateMachine, TState, TEvent> stateContext) {
		if (superState != null
				&& !HistoryType.NONE.equals(superState.getHistoryType())) {
			stateContext.setLastActiveSubState(superState, this);
		}
	}

	@Override
	public TransitionResult<TStateMachine, TState, TEvent> fire(
			final TransitionContext<TStateMachine, TState, TEvent> context) {
		@SuppressWarnings("unchecked")
		TransitionResult<TStateMachine, TState, TEvent> result = TransitionResultImpl
				.getNotFired();

		final List<Transition<TStateMachine, TState, TEvent>> transitionsForEvent = this.transitions
				.getTransitions(context.getEventId());
		if (transitionsForEvent != null) {
			for (final Transition<TStateMachine, TState, TEvent> transition : transitionsForEvent) {
				result = transition.fire(context);
				if (result.isFired()) {
					return result;
				}
			}
		}
		LOG.info("No transition available in this state ({}).", this.getId());

		if (this.getSuperState() != null) {
			LOG.info("Fire the same event on the super state.");
			result = this.getSuperState().fire(context);
		}

		return result;
	}

	@Override
	public ActionHolder<TStateMachine, TState, TEvent> getEntryAction() {
		return this.entryAction;
	}

	@Override
	public ActionHolder<TStateMachine, TState, TEvent> getExitAction() {
		return this.exitAction;
	}

	@Override
	public HistoryType getHistoryType() {
		return this.historyType;
	}

	@Override
	public TState getId() {
		return this.id;
	}

	@Override
	public InternalState<TStateMachine, TState, TEvent> getInitialState() {
		return this.initialState;
	}

	@Override
	public int getLevel() {
		return this.level;
	}

	@Override
	public List<InternalState<TStateMachine, TState, TEvent>> getSubStates() {
		return ImmutableList.copyOf(this.subStates);
	}

	@Override
	public InternalState<TStateMachine, TState, TEvent> getSuperState() {
		return this.superState;
	}

	@Override
	public TransitionDictionary<TStateMachine, TState, TEvent> getTransitions() {
		return this.transitions;
	}

	/**
	 * Handles the specific exception.
	 * 
	 * @param exception
	 *            the exception
	 * @param stateContext
	 *            the state context.
	 */
	private void handleException(final Exception exception,
			final StateContext<TStateMachine, TState, TEvent> stateContext) {
		stateContext.getExceptions().add(exception);
		stateContext.getNotifier().onExceptionThrown(stateContext, exception);
	}

	@Override
	public void setEntryAction(
			final ActionHolder<TStateMachine, TState, TEvent> action) {
		this.entryAction = action;

	}

	@Override
	public void setExitAction(
			final ActionHolder<TStateMachine, TState, TEvent> action) {
		this.exitAction = action;

	}

	@Override
	public void setHistoryType(final HistoryType historyType) {
		this.historyType = historyType;

	}

	/**
	 * Sets the initial level depending on the level of the super state of this
	 * instance.
	 */
	private void setInitialLevel() {
		this.setLevel(this.superState != null ? this.superState.getLevel() + 1
				: 1);
	}

	@Override
	public void setInitialState(
			final InternalState<TStateMachine, TState, TEvent> initialState) {
		this.checkInitialStateIsNotThisInstance(initialState);
		this.checkInitialStateIsASubState(initialState);
		this.initialState = initialState;
	}

	@Override
	public void setLevel(final int level) {
		this.level = level;
		this.setLevelOfSubStates();
	}

	/**
	 * Sets the level of all sub states.
	 */
	private void setLevelOfSubStates() {
		for (final InternalState<TStateMachine, TState, TEvent> state : this
				.getSubStates()) {
			state.setLevel(this.level + 1);
		}
	}

	@Override
	public void setSuperState(
			final InternalState<TStateMachine, TState, TEvent> superState) {
		this.checkSuperStateIsNotThisInstance(superState);
		this.superState = superState;
		this.setInitialLevel();

	}

	@Override
	public String toString() {
		return this.id.toString();
	}
}
