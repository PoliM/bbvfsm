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
package ch.bbv.fsm.impl.internal.statemachine;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.events.StateMachineEventHandler;
import ch.bbv.fsm.impl.internal.driver.Notifier;
import ch.bbv.fsm.impl.internal.statemachine.events.ExceptionEventArgsImpl;
import ch.bbv.fsm.impl.internal.statemachine.events.TransitionCompletedEventArgsImpl;
import ch.bbv.fsm.impl.internal.statemachine.events.TransitionEventArgsImpl;
import ch.bbv.fsm.impl.internal.statemachine.events.TransitionExceptionEventArgsImpl;
import ch.bbv.fsm.impl.internal.statemachine.state.InternalState;
import ch.bbv.fsm.impl.internal.statemachine.state.StateContext;
import ch.bbv.fsm.impl.internal.statemachine.state.StateDictionary;
import ch.bbv.fsm.impl.internal.statemachine.transition.TransitionContext;
import ch.bbv.fsm.impl.internal.statemachine.transition.TransitionResult;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * InternalState Machine Implementation.
 * 
 * @author Ueli Kurmann (bbv Software Services AG)
 * 
 * @param <TStateMachine>
 *            the type of state machine
 * @param <TState>
 *            the type of the states
 * @param <TEvent>
 *            the type of the events
 */
public class StateMachineInterpreter<TStateMachine extends StateMachine<TState, TEvent>, TState extends Enum<?>, TEvent extends Enum<?>>
		implements Notifier<TStateMachine, TState, TEvent> {

	private static final Logger LOG = LoggerFactory.getLogger(StateMachineInterpreter.class);

	/**
	 * Name of this state machine used in log messages.
	 */
	private final String name;

	private final TStateMachine stateMachine;

	/**
	 * The current state.
	 */
	private InternalState<TStateMachine, TState, TEvent> currentState;

	private final Map<InternalState<TStateMachine, TState, TEvent>, InternalState<TStateMachine, TState, TEvent>> superToSubState = Maps.newHashMap();

	private final TState initialStateId;

	/**
	 * The dictionary of all states.
	 */
	private final StateDictionary<TStateMachine, TState, TEvent> states;

	private final List<StateMachineEventHandler<TStateMachine, TState, TEvent>> eventHandler;

	/**
	 * Initializes a new instance of the StateMachineImpl<TState,TEvent> class.
	 * 
	 * @param stateMachine
	 *            the custom's state machine
	 * @param name
	 *            The name of this state machine used in log messages.
	 * @param states
	 *            the states
	 * @param initialState
	 *            the initial state
	 */
	public StateMachineInterpreter(final TStateMachine stateMachine, final String name,
			final StateDictionary<TStateMachine, TState, TEvent> states, final TState initialState) {
		this.name = name;
		this.states = states;
		this.stateMachine = stateMachine;
		this.initialStateId = initialState;
		this.eventHandler = Lists.newArrayList();
	}

	/**
	 * Fires the specified event.
	 * 
	 * @param eventId
	 *            the event id.
	 */
	public void fire(final TEvent eventId) {
		this.fire(eventId, null);
	}

	/**
	 * Fires the specified event.
	 * 
	 * @param eventId
	 *            the event id.
	 * @param eventArguments
	 *            the event arguments.
	 */
	public void fire(final TEvent eventId, final Object[] eventArguments) {
		if (LOG.isDebugEnabled()) {
			LOG.info("Fire event {} on state machine {} with current state {} and event arguments {}.",
					new Object[] { eventId, this.getName(), this.getCurrentStateId(), eventArguments });
		}

		final TransitionContext<TStateMachine, TState, TEvent> context = new TransitionContext<TStateMachine, TState, TEvent>(stateMachine,
				getCurrentState(), eventId, eventArguments, this, this);
		final TransitionResult<TStateMachine, TState, TEvent> result = this.currentState.fire(context);

		if (!result.isFired()) {
			LOG.info("No transition possible.");
			this.onTransitionDeclined(context);
			return;
		}

		this.setCurrentState(result.getNewState());

		LOG.debug("InternalState machine {} performed {}.", this, context.getRecords());

		this.onTransitionCompleted(context);
	}

	/**
	 * Returns the current state.
	 * 
	 * @return the current state.
	 */
	private InternalState<TStateMachine, TState, TEvent> getCurrentState() {
		return this.currentState;
	}

	/**
	 * Gets the id of the current state.
	 * 
	 * @return The id of the current state.
	 */
	public TState getCurrentStateId() {
		if (this.getCurrentState() != null) {
			return this.getCurrentState().getId();
		} else {
			return null;
		}
	}

	/**
	 * Gets the name of this instance.
	 * 
	 * @return The name of this instance.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Initializes the state machine by setting the specified initial state.
	 * 
	 * @param initialState
	 *            the initial state
	 * @param stateContext
	 *            the state context
	 */
	private void initialize(final InternalState<TStateMachine, TState, TEvent> initialState,
			final StateContext<TStateMachine, TState, TEvent> stateContext) {
		if (initialState == null) {
			throw new IllegalArgumentException("initialState; The initial state must not be null.");
		}

		final StateMachineInitializer<TStateMachine, TState, TEvent> initializer = new StateMachineInitializer<TStateMachine, TState, TEvent>(
				initialState, stateContext);
		this.setCurrentState(initializer.enterInitialState());
	}

	/**
	 * Initializes the state machine.
	 */
	public void initialize() {
		LOG.info("InternalState machine {} initializes to state {}.", this, initialStateId);
		final StateContext<TStateMachine, TState, TEvent> stateContext = new StateContext<TStateMachine, TState, TEvent>(stateMachine,
				null, this, this);
		this.initialize(this.states.getState(initialStateId), stateContext);
		LOG.info("InternalState machine {} performed {}.", this, stateContext.getRecords());
	}

	/**
	 * Terminates the state machine.
	 */
	public void terminate() {
		final StateContext<TStateMachine, TState, TEvent> stateContext = new StateContext<TStateMachine, TState, TEvent>(stateMachine,
				null, this, this);
		InternalState<TStateMachine, TState, TEvent> o = getCurrentState();
		while (o != null) {
			o.exit(stateContext);
			o = o.getSuperState();
		}
	}

	/**
	 * Adds an event handler.
	 * 
	 * @param handler
	 *            the event handler.
	 */
	public void addEventHandler(final StateMachineEventHandler<TStateMachine, TState, TEvent> handler) {
		this.eventHandler.add(handler);
	}

	/**
	 * Removes an event handler.
	 * 
	 * @param handler
	 *            the event handle to be removed.
	 */
	public void removeEventHandler(final StateMachineEventHandler<TStateMachine, TState, TEvent> handler) {
		this.eventHandler.remove(handler);
	}

	@Override
	public void onExceptionThrown(final StateContext<TStateMachine, TState, TEvent> stateContext, final Exception exception) {
		for (final StateMachineEventHandler<TStateMachine, TState, TEvent> handler : this.eventHandler) {
			handler.onExceptionThrown(new ExceptionEventArgsImpl<TStateMachine, TState, TEvent>(stateContext, exception));
		}
	}

	@Override
	public void onExceptionThrown(final TransitionContext<TStateMachine, TState, TEvent> transitionContext, final Exception exception) {
		for (final StateMachineEventHandler<TStateMachine, TState, TEvent> handler : this.eventHandler) {
			handler.onTransitionThrowsException(new TransitionExceptionEventArgsImpl<TStateMachine, TState, TEvent>(transitionContext,
					exception));
		}
	}

	@Override
	public void onTransitionBegin(final StateContext<TStateMachine, TState, TEvent> transitionContext) {
		try {
			for (final StateMachineEventHandler<TStateMachine, TState, TEvent> handler : this.eventHandler) {
				handler.onTransitionBegin(new TransitionEventArgsImpl<TStateMachine, TState, TEvent>(transitionContext));
			}
		} catch (final Exception e) {
			onExceptionThrown(transitionContext, e);
		}
	}

	/**
	 * Fires a transition completed event.
	 * 
	 * @param transitionContext
	 *            the transition context
	 */
	protected void onTransitionCompleted(final StateContext<TStateMachine, TState, TEvent> transitionContext) {
		try {
			for (final StateMachineEventHandler<TStateMachine, TState, TEvent> handler : this.eventHandler) {
				handler.onTransitionCompleted(new TransitionCompletedEventArgsImpl<TStateMachine, TState, TEvent>(this.getCurrentStateId(),
						transitionContext));
			}
		} catch (final Exception e) {
			onExceptionThrown(transitionContext, e);
		}
	}

	/**
	 * Fires the transaction declined event.
	 * 
	 * @param transitionContext
	 *            the transition context.
	 */
	protected void onTransitionDeclined(final StateContext<TStateMachine, TState, TEvent> transitionContext) {
		try {
			for (final StateMachineEventHandler<TStateMachine, TState, TEvent> handler : this.eventHandler) {
				handler.onTransitionDeclined(new TransitionEventArgsImpl<TStateMachine, TState, TEvent>(transitionContext));
			}
		} catch (final Exception e) {
			onExceptionThrown(transitionContext, e);
		}

	}

	/**
	 * Sets the current state.
	 * 
	 * @param state
	 *            the current state.
	 */
	private void setCurrentState(final InternalState<TStateMachine, TState, TEvent> state) {
		LOG.info("InternalState machine {} switched to state {}.", this.getName(), state.getId());
		this.currentState = state;
	}

	/**
	 * Returns the last active substate for the given composite state.
	 * 
	 * @param superState
	 *            the super state
	 */
	public InternalState<TStateMachine, TState, TEvent> getLastActiveSubState(final InternalState<TStateMachine, TState, TEvent> superState) {
		return superToSubState.get(superState);
	}

	/**
	 * Sets the last active sub state for the given composite state.
	 * 
	 * @param superState
	 *            the super state
	 * @param subState
	 *            the last active sub state
	 */
	public void setLastActiveSubState(final InternalState<TStateMachine, TState, TEvent> superState,
			final InternalState<TStateMachine, TState, TEvent> subState) {
		superToSubState.put(superState, subState);
	}

	@Override
	public String toString() {
		return this.name;
	}
}
