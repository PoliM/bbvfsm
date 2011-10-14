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

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import ch.bbv.fsm.StateMachine;

/**
 * An active state machine. This state machine reacts to events on a separate worker thread.
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
public class ActiveStateMachineDriver<TStateMachine extends StateMachine<TState, TEvent>, TState extends Enum<?>, TEvent extends Enum<?>>
		extends AbstractStateMachineDriver<TStateMachine, TState, TEvent> {

	private static final int BLOCKING_TIME_ON_EVENT_MS = 10;

	private static final int WAIT_FOR_TERMINATION_MS = 10000;

	/**
	 * List of all queued events.
	 */
	private final BlockingDeque<EventInformation<TEvent>> events;

	private ExecutorService executorService;

	private final Object checkProcessingLock = new Object();

	/**
	 * <code>true</code> while the driver is processing an event.
	 */
	private boolean processing;

	private final Runnable worker = new Runnable() {
		@Override
		public void run() {
			ActiveStateMachineDriver.this.execute();
		}
	};

	/**
	 * Create an active state machine.
	 */
	public ActiveStateMachineDriver() {
		this.events = new LinkedBlockingDeque<EventInformation<TEvent>>();
		this.executorService = Executors.newFixedThreadPool(1);
	}

	/**
	 * Executes all queued events.
	 */
	private void execute() {
		try {
			while (RunningState.Running.equals(getRunningState())) {
				final EventInformation<TEvent> eventToProcess;
				synchronized (checkProcessingLock) {
					eventToProcess = this.getNextEventToProcess();
					processing = eventToProcess != null;
				}
				try {
					if (eventToProcess != null) {
						this.fireEventOnStateMachine(eventToProcess);
					}
				} finally {
					processing = false;
				}
			}
		} catch (InterruptedException e) {
			// Interrupted - just terminate
			return;
		}
	}

	@Override
	public boolean isIdle() {
		synchronized (checkProcessingLock) {
			return !processing & events.size() == 0;
		}
	}

	@Override
	public void fire(final TEvent eventId, final Object... eventArguments) {
		this.events.addLast(new EventInformation<TEvent>(eventId, eventArguments));
	}

	@Override
	public void firePriority(final TEvent eventId, final Object... eventArguments) {
		this.events.addFirst(new EventInformation<TEvent>(eventId, eventArguments));
	}

	/**
	 * Gets the next event to process for the queue.
	 * 
	 * @return The next queued event.
	 * @throws InterruptedException
	 */
	private EventInformation<TEvent> getNextEventToProcess() throws InterruptedException {
		return this.events.pollFirst(BLOCKING_TIME_ON_EVENT_MS, TimeUnit.MILLISECONDS);
	}

	@Override
	public int numberOfQueuedEvents() {
		return this.events.size();
	}

	@Override
	public synchronized void start() {
		super.start();
		this.executorService = Executors.newFixedThreadPool(1);
		this.executorService.execute(this.worker);
	}

	@Override
	public synchronized void terminate() {
		super.terminate();
		this.executorService.shutdown();
		try {
			this.executorService.awaitTermination(WAIT_FOR_TERMINATION_MS, TimeUnit.MILLISECONDS);
		} catch (final InterruptedException e) {
			return;
		}
		super.terminate();
	}
}
