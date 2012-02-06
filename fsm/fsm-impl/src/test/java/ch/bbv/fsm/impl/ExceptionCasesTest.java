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
package ch.bbv.fsm.impl;

import org.junit.Assert;
import org.junit.Test;

import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.action.Action;
import ch.bbv.fsm.events.ExceptionEventArgs;
import ch.bbv.fsm.events.StateMachineEventAdapter;
import ch.bbv.fsm.events.TransitionEventArgs;
import ch.bbv.fsm.events.TransitionExceptionEventArgs;
import ch.bbv.fsm.guard.Function;
import ch.bbv.fsm.impl.StatesAndEvents.Events;
import ch.bbv.fsm.impl.StatesAndEvents.States;

public class ExceptionCasesTest {
	private class Handler
			extends
			StateMachineEventAdapter<SimpleStateMachine<States, Events>, States, Events> {

		@Override
		public void onExceptionThrown(
				final ExceptionEventArgs<SimpleStateMachine<States, Events>, States, Events> eventArgs) {
			if (eventArgs != null) {
				ExceptionCasesTest.this.recordedException = eventArgs
						.getException();
			}

		}

		@Override
		public void onTransitionDeclined(
				final TransitionEventArgs<SimpleStateMachine<States, Events>, States, Events> arg) {
			ExceptionCasesTest.this.transitionDeclined = true;

		}

		@Override
		public void onTransitionThrowsException(
				final TransitionExceptionEventArgs<SimpleStateMachine<States, Events>, States, Events> eventArgs) {

			ExceptionCasesTest.this.recordedStateId = eventArgs.getStateId();
			ExceptionCasesTest.this.recordedEventId = eventArgs.getEventId();
			ExceptionCasesTest.this.recordedEventArguments = eventArgs
					.getEventArguments();
			ExceptionCasesTest.this.recordedException = eventArgs
					.getException();

		}

	}

	public static class ThrowExceptionAction implements
			Action<SimpleStateMachine<States, Events>, States, Events> {

		private static RuntimeException exception = new RuntimeException();

		@Override
		public void execute(
				final SimpleStateMachine<States, Events> stateMachine,
				final Object... arguments) {

			throw exception;
		}

		/**
		 * @return the exception
		 */
		public static RuntimeException getException() {
			return exception;
		}

	}

	/**
	 * The state that was provided in the
	 * {@link ch.bbv.fsm.events.StateMachineEventHandler#onExceptionThrown(ExceptionEventArgs)}
	 * event.
	 */
	private States recordedStateId;

	/**
	 * The event that was provided in the
	 * {@link ch.bbv.fsm.events.StateMachineEventHandler#onExceptionThrown(ExceptionEventArgs)}
	 * event.
	 */
	private Events recordedEventId;

	/**
	 * The event arguments that was provided in the
	 * {@link ch.bbv.fsm.events.StateMachineEventHandler#onExceptionThrown(ExceptionEventArgs)}
	 * event.
	 */
	private Object[] recordedEventArguments;

	/**
	 * The exception that was provided in the
	 * {@link ch.bbv.fsm.events.StateMachineEventHandler#onExceptionThrown(ExceptionEventArgs)}
	 * event.
	 */
	private Exception recordedException;

	private boolean transitionDeclined;

	private final Class<ThrowExceptionAction> throwException = ThrowExceptionAction.class;

	/**
	 * Asserts that the correct exception was notified.
	 */
	private void assertException(final States expectedStateId,
			final Events expectedEventId,
			final Object[] expectedEventArguments,
			final Exception expectedException) {
		Assert.assertEquals(expectedStateId, this.recordedStateId);
		Assert.assertEquals(expectedEventId, this.recordedEventId);
		Assert.assertArrayEquals(expectedEventArguments,
				this.recordedEventArguments);
		Assert.assertEquals(expectedException, this.recordedException);
	}

	/**
	 * When a transition throws an exception then the exception is catched and
	 * the
	 * {@link ch.bbv.fsm.events.StateMachineEventHandler#onExceptionThrown(ExceptionEventArgs)}
	 * event is fired. The transition is executed and the state machine is in
	 * the target state.
	 */
	@Test
	public void exceptionThrowingAction() {

		final Object[] eventArguments = new Object[] { 1, 2, "test" };

		final SimpleStateMachineDefinition<States, Events> def = new SimpleStateMachineDefinition<States, Events>(
				"exceptionThrowingAction", States.A);

		def.in(States.A).on(Events.B).goTo(States.B).execute(throwException);
		def.addEventHandler(new Handler());
		final StateMachine<States, Events> testee = def
				.createPassiveStateMachine("testee", States.A);
		testee.start();
		testee.fire(Events.B, eventArguments);

		this.assertException(States.A, Events.B, eventArguments,
				ThrowExceptionAction.getException());
		Assert.assertEquals(States.B, testee.getCurrentState());
	}

	/**
	 * When an exception is thrown in an entry action then it is notified and
	 * the state is entered anyway.
	 */
	@Test
	public void exceptionThrowingEntryAction() {

		final Object[] eventArguments = new Object[] { 1, 2, "test" };

		final SimpleStateMachineDefinition<States, Events> def = new SimpleStateMachineDefinition<States, Events>(
				"exceptionThrowingEntryAction", States.A);
		def.addEventHandler(new Handler());
		def.in(States.A).on(Events.B).goTo(States.B);

		def.in(States.B).executeOnEntry(throwException);

		final StateMachine<States, Events> testee = def
				.createPassiveStateMachine("testee", States.A);
		testee.start();
		testee.fire(Events.B, eventArguments);

		Assert.assertEquals(ThrowExceptionAction.getException(),
				this.recordedException);
		Assert.assertEquals(States.B, testee.getCurrentState());
	}

	/**
	 * When an exception is thrown in an entry action then it is notified and
	 * the state is entered anyway.
	 */
	@Test
	public void exceptionThrowingExitAction() {

		final Object[] eventArguments = new Object[] { 1, 2, "test" };

		final SimpleStateMachineDefinition<States, Events> def = new SimpleStateMachineDefinition<States, Events>(
				"exceptionThrowingExitAction", States.A);
		def.addEventHandler(new Handler());
		def.in(States.A).executeOnExit(throwException).on(Events.B)
				.goTo(States.B);

		final StateMachine<States, Events> testee = def
				.createPassiveStateMachine("testee", States.A);
		testee.start();
		testee.fire(Events.B, eventArguments);

		Assert.assertEquals(ThrowExceptionAction.getException(),
				this.recordedException);
		Assert.assertEquals(States.B, testee.getCurrentState());
	}

	/**
	 * When a guard throws an exception then it is catched and the
	 * {@link ch.bbv.fsm.events.StateMachineEventHandler#onExceptionThrown(ExceptionEventArgs)}
	 * event is fired. The transition is not executed and if there is no other
	 * transition then the state machine remains in the same state.
	 */
	@Test
	public void exceptionThrowingGuard() {
		final Object[] eventArguments = new Object[] { 1, 2, "test" };
		final RuntimeException e = new RuntimeException();

		final Function<SimpleStateMachine<States, Events>, States, Events, Object[], Boolean> f1 = new Function<SimpleStateMachine<States, Events>, States, Events, Object[], Boolean>() {
			@Override
			public Boolean execute(
					final SimpleStateMachine<States, Events> stateMachine,
					final Object[] parameter) {
				throw e;
			}
		};

		final SimpleStateMachineDefinition<States, Events> def = new SimpleStateMachineDefinition<States, Events>(
				"exceptionThrowingGuard", States.A);
		def.in(States.A).on(Events.B).goTo(States.B).onlyIf(f1);

		def.addEventHandler(new Handler());

		final StateMachine<States, Events> testee = def
				.createPassiveStateMachine("testee", States.A);
		testee.start();
		testee.fire(Events.B, eventArguments);

		this.assertException(States.A, Events.B, eventArguments, e);
		Assert.assertEquals(States.A, testee.getCurrentState());
		Assert.assertTrue(this.transitionDeclined);

	}
}
