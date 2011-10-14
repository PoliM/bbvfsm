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
package ch.bbv.fsm.impl.internal.driver;

import java.util.LinkedList;

import ch.bbv.fsm.StateMachine;

/**
 * A passive state machine. This state machine reacts to events on the current thread.
 * 
 * @author Ueli Kurmann (bbv Software Services AG) (bbv Software Services AG)
 * 
 * @param <TState>
 *            the enumeration type of the states.
 * @param <TEvent>
 *            the enumeration type of the events.
 * @param <TStateMachine>
 *            the type of state machine
 */
public class PassiveStateMachineDriver<TStateMachine extends StateMachine<TState, TEvent>, TState extends Enum<?>, TEvent extends Enum<?>>
		extends AbstractStateMachineDriver<TStateMachine, TState, TEvent> {

	/**
	 * List of all queued events.
	 */
	private final LinkedList<EventInformation<TEvent>> events;

	/**
	 * Do not process event while already processing an event. This happens if an event is fired in an state's or transition's action.
	 */
	private boolean processing;

	/**
	 * Creates the state machine.
	 */
	public PassiveStateMachineDriver() {
		this.events = new LinkedList<EventInformation<TEvent>>();
	}

	@Override
	public synchronized void fire(final TEvent eventId, final Object... eventArguments) {
		this.events.addLast(new EventInformation<TEvent>(eventId, eventArguments));
		this.execute();
	}

	@Override
	public synchronized void firePriority(final TEvent eventId, final Object... eventArguments) {
		this.events.addFirst(new EventInformation<TEvent>(eventId, eventArguments));
		this.execute();
	}

	@Override
	public synchronized int numberOfQueuedEvents() {
		return this.events.size();
	}

	@Override
	public synchronized boolean isIdle() {
		return numberOfQueuedEvents() == 0;
	}

	@Override
	public synchronized void start() {
		super.start();
		this.execute();
	}

	/**
	 * Executes all queued events.
	 */
	private void execute() {
		if (!processing) {
			try {
				processing = true;
				if (RunningState.Running.equals(getRunningState())) {
					processQueuedEvents();
				}
			} finally {
				processing = false;
			}
		}
	}

	/**
	 * Gets the next event to process for the queue.
	 * 
	 * @return The next queued event.
	 */
	private EventInformation<TEvent> getNextEventToProcess() {
		final EventInformation<TEvent> e = this.events.getFirst();
		this.events.removeFirst();
		return e;
	}

	/**
	 * Processes the queued events.
	 */
	private void processQueuedEvents() {
		while (this.events.size() > 0) {
			final EventInformation<TEvent> eventToProcess = this.getNextEventToProcess();
			this.fireEventOnStateMachine(eventToProcess);
		}
	}
}
