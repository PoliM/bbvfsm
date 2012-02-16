package ch.bbv.fsm.impl;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.bbv.fsm.HistoryType;
import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.StateMachineDefinition;
import ch.bbv.fsm.documentation.DocumentationGenerator;
import ch.bbv.fsm.dsl.EntryActionSyntax;
import ch.bbv.fsm.events.StateMachineEventHandler;
import ch.bbv.fsm.impl.internal.aop.CallInterceptorBuilder;
import ch.bbv.fsm.impl.internal.driver.ActiveStateMachineDriver;
import ch.bbv.fsm.impl.internal.driver.Notifier;
import ch.bbv.fsm.impl.internal.driver.PassiveStateMachineDriver;
import ch.bbv.fsm.impl.internal.dsl.StateBuilder;
import ch.bbv.fsm.impl.internal.model.visitor.Visitor;
import ch.bbv.fsm.impl.internal.statemachine.events.ExceptionEventArgsImpl;
import ch.bbv.fsm.impl.internal.statemachine.events.TransitionEventArgsImpl;
import ch.bbv.fsm.impl.internal.statemachine.events.TransitionExceptionEventArgsImpl;
import ch.bbv.fsm.impl.internal.statemachine.state.State;
import ch.bbv.fsm.impl.internal.statemachine.state.StateContext;
import ch.bbv.fsm.impl.internal.statemachine.state.StateDictionary;
import ch.bbv.fsm.impl.internal.statemachine.transition.TransitionContext;

import com.google.common.collect.Lists;

/**
 * Implementation of the definition of the finite state machine.
 * 
 * @param <TState>
 *            the type of the states.
 * @param <TEvent>
 *            the type of the events.
 * @param <TStateMachine>
 *            the type of the state machine
 */
public abstract class AbstractStateMachineDefinition<TStateMachine extends AbstractStateMachine<TStateMachine, TState, TEvent>, TState extends Enum<?>, TEvent extends Enum<?>>
		implements StateMachineDefinition<TStateMachine, TState, TEvent>,
		Notifier<TStateMachine, TState, TEvent> {

	private static final Logger LOG = LoggerFactory
			.getLogger(AbstractStateMachineDefinition.class);

	/**
	 * The dictionary of all states.
	 */
	private StateDictionary<TStateMachine, TState, TEvent> states;

	/**
	 * Name of this state machine used in log messages.
	 */
	private String name;

	private final List<StateMachineEventHandler<TStateMachine, TState, TEvent>> eventHandler;

	private final TState initialState;

	/**
	 * Initializes the passive state machine.
	 * 
	 * @param initialState
	 *            the initial state to use
	 */
	public AbstractStateMachineDefinition(final TState initialState) {
		this(AbstractStateMachineDefinition.class.getSimpleName(), initialState);
	}

	/**
	 * Initializes the state machine.
	 * 
	 * @param name
	 *            the name of the state machine used in the logs.
	 * @param initialState
	 *            the initial state to use
	 */
	public AbstractStateMachineDefinition(final String name,
			final TState initialState) {
		this.name = name;
		this.states = new StateDictionary<TStateMachine, TState, TEvent>();
		this.eventHandler = Lists.newArrayList();
		this.initialState = initialState;
	}

	@Override
	public <TDocumentationFormat> TDocumentationFormat generateDocumentation(
			final DocumentationGenerator<TDocumentationFormat, TStateMachine, TState, TEvent> documentGenerator) {

		return documentGenerator.generateDocumentation(this);
	}

	@Override
	public <TDocumentationFormat> TDocumentationFormat generateDecisionTableDocumentation(
			final DocumentationGenerator<TDocumentationFormat, TStateMachine, TState, TEvent> documentGenerator) {

		throw new RuntimeException("Operation not implemented.");
	}

	@Override
	public final TState getInitialState() {
		return initialState;
	}

	@Override
	public void defineHierarchyOn(final TState superStateId,
			final TState initialSubStateId, final HistoryType historyType,
			final TState... subStateIds) {
		final State<TStateMachine, TState, TEvent> superState = this.states
				.getState(superStateId);
		superState.setHistoryType(historyType);

		for (final TState subStateId : subStateIds) {
			final State<TStateMachine, TState, TEvent> subState = this.states
					.getState(subStateId);
			subState.setSuperState(superState);
			superState.addSubState(subState);
		}

		superState.setInitialState(this.states.getState(initialSubStateId));
	}

	@Override
	public EntryActionSyntax<TStateMachine, TState, TEvent> in(
			final TState state) {
		final State<TStateMachine, TState, TEvent> newState = this.states
				.getState(state);
		return new StateBuilder<TStateMachine, TState, TEvent>(newState,
				this.states);
	}

	@Override
	public void addEventHandler(
			final StateMachineEventHandler<TStateMachine, TState, TEvent> handler) {
		this.eventHandler.add(handler);
	}

	@Override
	public void removeEventHandler(
			final StateMachineEventHandler<TStateMachine, TState, TEvent> handler) {
		this.eventHandler.remove(handler);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public TStateMachine createActiveStateMachine(final String name,
			final TState initialState) {
		final ActiveStateMachineDriver<TStateMachine, TState, TEvent> activeStateMachine = new ActiveStateMachineDriver<TStateMachine, TState, TEvent>();
		final TStateMachine stateMachine = createStateMachine(activeStateMachine);
		activeStateMachine.initialize(stateMachine, name, states, initialState,
				eventHandler);
		return stateMachine;
	}

	@Override
	public TStateMachine createActiveStateMachine(final String name) {
		final ActiveStateMachineDriver<TStateMachine, TState, TEvent> activeStateMachine = new ActiveStateMachineDriver<TStateMachine, TState, TEvent>();
		final TStateMachine stateMachine = createStateMachine(activeStateMachine);
		activeStateMachine.initialize(stateMachine, name, states, initialState,
				eventHandler);
		return stateMachine;
	}

	@Override
	public TStateMachine createPassiveStateMachine(final String name,
			final TState initialState) {
		final PassiveStateMachineDriver<TStateMachine, TState, TEvent> passiveStateMachine = new PassiveStateMachineDriver<TStateMachine, TState, TEvent>();
		final TStateMachine stateMachine = createStateMachine(passiveStateMachine);
		passiveStateMachine.initialize(stateMachine, name, states,
				initialState, eventHandler);
		return stateMachine;
	}

	@Override
	public TStateMachine createPassiveStateMachine(final String name) {
		final PassiveStateMachineDriver<TStateMachine, TState, TEvent> passiveStateMachine = new PassiveStateMachineDriver<TStateMachine, TState, TEvent>();
		final TStateMachine stateMachine = createStateMachine(passiveStateMachine);
		passiveStateMachine.initialize(stateMachine, name, states,
				initialState, eventHandler);
		return stateMachine;
	}

	@Override
	public void onExceptionThrown(
			final StateContext<TStateMachine, TState, TEvent> stateContext,
			final Exception exception) {
		try {
			for (final StateMachineEventHandler<TStateMachine, TState, TEvent> handler : this.eventHandler) {
				handler.onExceptionThrown(new ExceptionEventArgsImpl<TStateMachine, TState, TEvent>(
						stateContext, exception));
			}
		} catch (final Exception e) {
			LOG.error("Exception during event handler.", e);
		}

	}

	@Override
	public void onExceptionThrown(
			final TransitionContext<TStateMachine, TState, TEvent> transitionContext,
			final Exception exception) {
		try {
			for (final StateMachineEventHandler<TStateMachine, TState, TEvent> handler : this.eventHandler) {
				handler.onTransitionThrowsException(new TransitionExceptionEventArgsImpl<TStateMachine, TState, TEvent>(
						transitionContext, exception));
			}
		} catch (final Exception e) {
			LOG.error("Exception during event handler.", e);
		}

	}

	@Override
	public void onTransitionBegin(
			final StateContext<TStateMachine, TState, TEvent> transitionContext) {
		try {
			for (final StateMachineEventHandler<TStateMachine, TState, TEvent> handler : this.eventHandler) {
				handler.onTransitionBegin(new TransitionEventArgsImpl<TStateMachine, TState, TEvent>(
						transitionContext));
			}
		} catch (final Exception e) {
			onExceptionThrown(transitionContext, e);
		}
	}

	@SuppressWarnings("unchecked")
	protected TStateMachine getPrototype() {
		return (TStateMachine) CallInterceptorBuilder.build(createStateMachine(
				null).getClass());
	}

	protected abstract TStateMachine createStateMachine(
			StateMachine<TState, TEvent> driver);

	/**
	 * Accepts a {@link #Visitor}.
	 * 
	 * @param visitor
	 *            the visitor.
	 */
	public void accept(final Visitor<TStateMachine, TState, TEvent> visitor) {

		visitor.visitOnEntry(this);

		for (State<TStateMachine, TState, TEvent> state : getRootStates()) {
			state.accept(visitor);
		}

		visitor.visitOnExit(this);
	}

	/**
	 * @return
	 */
	private List<State<TStateMachine, TState, TEvent>> getRootStates() {

		List<State<TStateMachine, TState, TEvent>> rootStates = new LinkedList<State<TStateMachine, TState, TEvent>>();

		for (State<TStateMachine, TState, TEvent> state : this.states
				.getStates()) {

			if (isStateInRootSuperState(state)) {
				rootStates.add(state);
			}
		}

		return rootStates;
	}

	/**
	 * @param state
	 * @return
	 */
	private boolean isStateInRootSuperState(
			final State<TStateMachine, TState, TEvent> state) {
		return !hasSuperState(state);
	}

	/**
	 * @param state
	 * @return
	 */
	private boolean hasSuperState(
			final State<TStateMachine, TState, TEvent> state) {
		return state.getSuperState() != null;
	}

}
