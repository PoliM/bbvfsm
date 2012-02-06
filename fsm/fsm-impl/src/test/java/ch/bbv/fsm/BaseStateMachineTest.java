/*******************************************************************************
 *  C
import ch.bbv.fsm.impl.SimpleStateMachineDefinition;
opyright 2010, 2011 bbv Software Services AG, Ueli Kurmann
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
package ch.bbv.fsm;

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ch.bbv.fsm.StateMachine.RunningState;
import ch.bbv.fsm.action.Action;
import ch.bbv.fsm.events.ExceptionEventArgs;
import ch.bbv.fsm.events.StateMachineEventAdapter;
import ch.bbv.fsm.events.TransitionCompletedEventArgs;
import ch.bbv.fsm.events.TransitionEventArgs;
import ch.bbv.fsm.impl.SimpleStateMachine;
import ch.bbv.fsm.impl.SimpleStateMachineDefinition;
import ch.bbv.fsm.impl.StatesAndEvents.Events;
import ch.bbv.fsm.impl.StatesAndEvents.States;

import com.google.common.collect.Lists;

/**
 * Base for state machine test fixtures.
 */
public abstract class BaseStateMachineTest {

	private class Handler
			extends
			StateMachineEventAdapter<SimpleStateMachine<States, Events>, States, Events> {

		@Override
		public void onExceptionThrown(
				final ExceptionEventArgs<SimpleStateMachine<States, Events>, States, Events> arg) {
			BaseStateMachineTest.this.exceptions.add(arg.getException());
		}

		@Override
		public void onTransitionBegin(
				final TransitionEventArgs<SimpleStateMachine<States, Events>, States, Events> args) {
			BaseStateMachineTest.this.transitionBeginMessages.add(args);
		}

		@Override
		public void onTransitionCompleted(
				final TransitionCompletedEventArgs<SimpleStateMachine<States, Events>, States, Events> arg) {
			BaseStateMachineTest.this.transitionCompletedMessages.add(arg);
		}

		@Override
		public void onTransitionDeclined(
				final TransitionEventArgs<SimpleStateMachine<States, Events>, States, Events> arg) {
			BaseStateMachineTest.this.transitionDeclinedMessages.add(arg);
		}

	}

	public static class FireAction implements
			Action<SimpleStateMachine<States, Events>, States, Events> {

		@Override
		public void execute(
				final SimpleStateMachine<States, Events> stateMaschine,
				final Object... arguments) {
			stateMaschine.fire(Events.D);
			stateMaschine.firePriority(Events.C);
		}
	}

	private final Class<FireAction> fireAction = FireAction.class;

	/**
	 * The state machine under test.
	 */
	private SimpleStateMachine<States, Events> testee;

	/**
	 * Gets the exceptions that were notified.
	 */
	private List<Exception> exceptions;

	/**
	 * Gets the begin transition messages that were notified.
	 */
	private List<TransitionEventArgs<SimpleStateMachine<States, Events>, States, Events>> transitionBeginMessages;

	/**
	 * Gets the transition completed messages that were notified.
	 */
	private List<TransitionCompletedEventArgs<SimpleStateMachine<States, Events>, States, Events>> transitionCompletedMessages;

	/**
	 * Gets the transition declined messages that were notified.
	 */
	private List<TransitionEventArgs<SimpleStateMachine<States, Events>, States, Events>> transitionDeclinedMessages;

	/**
	 * Checks the begin transition message.
	 */
	protected void checkBeginTransitionMessage(final States origin,
			final Events eventId, final Object[] eventArguments) {
		Assert.assertEquals("wrong number of begin transition messages.", 1,
				this.transitionBeginMessages.size());
		Assert.assertEquals("wrong state in transition begin message.", origin,
				this.transitionBeginMessages.get(0).getStateId());
		Assert.assertEquals("wrong event in transition begin message.",
				eventId, this.transitionBeginMessages.get(0).getEventId());
		Assert.assertArrayEquals(
				"wrong event arguments in transition begin message.",
				eventArguments, this.transitionBeginMessages.get(0)
						.getEventArguments());
	}

	/**
	 * Checks the no declined transition message occurred.
	 */
	protected void checkNoDeclinedTransitionMessage() {
		Assert.assertEquals(0, this.transitionDeclinedMessages.size());
	}

	/**
	 * Checks the no exception message occurred.
	 */
	protected void checkNoExceptionMessage() {
		Assert.assertEquals(0, this.exceptions.size());
	}

	/**
	 * Checks the transition completed message.
	 */
	protected void checkTransitionCompletedMessage(
			final Object[] eventArguments, final States origin,
			final Events eventId, final States newState) {
		Assert.assertEquals(1, this.transitionCompletedMessages.size());
		Assert.assertEquals(origin, this.transitionCompletedMessages.get(0)
				.getStateId());
		Assert.assertEquals(eventId, this.transitionCompletedMessages.get(0)
				.getEventId());
		if (eventArguments != null) {
			Assert.assertArrayEquals(eventArguments,
					this.transitionCompletedMessages.get(0).getEventArguments());
		}

		Assert.assertEquals(newState, this.transitionCompletedMessages.get(0)
				.getNewStateId());
	}

	protected abstract SimpleStateMachine<States, Events> createTestee(
			SimpleStateMachineDefinition<States, Events> definition,
			States initialState);

	protected void initTestee(
			final SimpleStateMachineDefinition<States, Events> definition) {
		definition.addEventHandler(new Handler());
		testee = createTestee(definition, States.A);
		testee.start();
	}

	/**
	 * An event can be fired onto the state machine and all notifications are
	 * signaled.
	 */
	@Test
	public void fireEvent() {
		final SimpleStateMachineDefinition<States, Events> definition = new SimpleStateMachineDefinition<States, Events>(
				"fireEvent", States.A);

		definition.defineHierarchyOn(States.B, States.B1, HistoryType.NONE,
				States.B1, States.B2);
		definition.defineHierarchyOn(States.C, States.C2, HistoryType.SHALLOW,
				States.C1, States.C2);
		definition.defineHierarchyOn(States.C1, States.C1a,
				HistoryType.SHALLOW, States.C1a, States.C1b);
		definition.defineHierarchyOn(States.D, States.D1, HistoryType.DEEP,
				States.D1, States.D2);
		definition.defineHierarchyOn(States.D1, States.D1a, HistoryType.DEEP,
				States.D1a, States.D1b);

		definition.in(States.A).on(Events.B).goTo(States.B);

		final Object[] eventArguments = new Object[] { 1, 2, "test" };

		initTestee(definition);

		testee.fire(Events.B, eventArguments[0], eventArguments[1],
				eventArguments[2]);

		waitUntilAllEventsAreProcessed();

		this.checkBeginTransitionMessage(States.A, Events.B, eventArguments);
		this.checkTransitionCompletedMessage(eventArguments, States.A,
				Events.B, States.B1);
		this.checkNoExceptionMessage();
		this.checkNoDeclinedTransitionMessage();
	}

	/**
	 * With FirePriority, an event can be added to the front of the queued
	 * events.
	 */
	@Test
	public void priorityFire() {
		final int transitions = 3;

		final SimpleStateMachineDefinition<States, Events> definition = new SimpleStateMachineDefinition<States, Events>(
				"priorityFire", States.A);

		definition.in(States.A).on(Events.B).goTo(States.B).execute(fireAction);

		definition.in(States.B).on(Events.C).goTo(States.C);

		definition.in(States.C).on(Events.D).goTo(States.D);

		initTestee(definition);

		testee.fire(Events.B);

		waitUntilAllEventsAreProcessed();

		Assert.assertEquals(transitions,
				this.transitionCompletedMessages.size());
		this.checkNoDeclinedTransitionMessage();
		this.checkNoExceptionMessage();
	}

	@Test(expected = IllegalStateException.class)
	public void startTwice() {
		final SimpleStateMachineDefinition<States, Events> definition = new SimpleStateMachineDefinition<States, Events>(
				"startTwice", States.A);
		initTestee(definition);
		this.testee.start();
	}

	/**
	 * When the state machine is stopped then no events are processed. All
	 * events enqueued are processed when state machine is started.
	 */
	@Test(expected = IllegalStateException.class)
	public void stopAndStart() {

		final SimpleStateMachineDefinition<States, Events> definition = new SimpleStateMachineDefinition<States, Events>(
				"startTwice", States.A);

		definition.in(States.A).on(Events.B).goTo(States.B);

		definition.in(States.B).on(Events.C).goTo(States.C);

		initTestee(definition);

		this.testee.terminate();

		Assert.assertFalse(RunningState.Running.equals(this.testee
				.getRunningState()));

		this.testee.fire(Events.B);
		this.testee.fire(Events.C);

		Assert.assertEquals(0, this.transitionBeginMessages.size());

		this.testee.start();
	}

	/**
	 * Initializes a test.
	 */
	@Before
	public void setup() {
		this.exceptions = Lists.newArrayList();
		this.transitionBeginMessages = Lists.newArrayList();
		this.transitionCompletedMessages = Lists.newArrayList();
		this.transitionDeclinedMessages = Lists.newArrayList();
	}

	/**
	 * Tears down a test.
	 */
	@After
	public void tearDown() {
		this.testee.terminate();
	}

	private void waitUntilAllEventsAreProcessed() {
		while (!this.testee.isIdle()) {
			try {
				Thread.sleep(10);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}
