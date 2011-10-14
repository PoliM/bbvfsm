package ch.bbv.fsm.impl;

import ch.bbv.fsm.StateMachine;

/**
 * Base class for finite state machine implementations.
 * 
 * @param <TState>
 *            the type of the states.
 * @param <TEvent>
 *            the type of the events.
 * @param <TStateMachine>
 *            the type of state machine
 */
public class AbstractStateMachine<TStateMachine extends StateMachine<TState, TEvent>, TState extends Enum<?>, TEvent extends Enum<?>>
		implements StateMachine<TState, TEvent> {

	private final StateMachine<TState, TEvent> driver;

	/**
	 * Create a state machine.
	 * 
	 * @param driver
	 *            the executor of the state event machine.
	 */
	protected AbstractStateMachine(final StateMachine<TState, TEvent> driver) {
		this.driver = driver;
	}

	@Override
	public final void fire(final TEvent eventId, final Object... eventArguments) {
		driver.fire(eventId, eventArguments);
	}

	@Override
	public final void firePriority(final TEvent eventId, final Object... eventArguments) {
		driver.firePriority(eventId, eventArguments);
	}

	@Override
	public final int numberOfQueuedEvents() {
		return driver.numberOfQueuedEvents();
	}

	@Override
	public final void start() {
		driver.start();
	}

	@Override
	public final void terminate() {
		driver.terminate();
	}

	@Override
	public boolean isIdle() {
		return driver.isIdle();
	}

	@Override
	public final TState getCurrentState() {
		return driver.getCurrentState();
	}

	@Override
	public ch.bbv.fsm.StateMachine.RunningState getRunningState() {
		return driver.getRunningState();
	}

}