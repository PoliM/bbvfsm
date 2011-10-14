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
import ch.bbv.fsm.events.StateMachineEventAdapter;
import ch.bbv.fsm.events.TransitionEventArgs;
import ch.bbv.fsm.guard.Function;
import ch.bbv.fsm.impl.StatesAndEvents.Events;
import ch.bbv.fsm.impl.StatesAndEvents.States;

public class GuardTest {
	private class Handler extends StateMachineEventAdapter<SimpleStateMachine<States, Events>, States, Events> {

		@Override
		public void onTransitionDeclined(final TransitionEventArgs<SimpleStateMachine<States, Events>, States, Events> arg) {
			GuardTest.this.transitionDeclined = true;
		}
	}

	private boolean transitionDeclined = false;

	/**
	 * When all guards deny the execution of its transition then ???
	 */
	@Test
	public void allGuardsReturnFalse() {

		final Function<SimpleStateMachine<States, Events>, States, Events, Object[], Boolean> f1 = new Function<SimpleStateMachine<States, Events>, States, Events, Object[], Boolean>() {

			@Override
			public Boolean execute(final SimpleStateMachine<States, Events> stateMachine, final Object[] parameter) {
				return false;
			}
		};

		final SimpleStateMachineDefinition<States, Events> stateMachineDefinition = new SimpleStateMachineDefinition<States, Events>(
				"allGuardsReturnFalse", States.A);

		stateMachineDefinition.addEventHandler(new Handler());

		stateMachineDefinition.in(States.A).on(Events.A).goTo(States.B).onlyIf(f1).on(Events.A).goTo(States.C).onlyIf(f1);

		final StateMachine<States, Events> fsm = stateMachineDefinition.createPassiveStateMachine("allGuardsReturnFalse", States.A);
		fsm.start();

		fsm.fire(Events.A);

		Assert.assertEquals(States.A, fsm.getCurrentState());
		Assert.assertTrue(this.transitionDeclined);
	}

	/**
	 * The event arguments are passed to the guard.
	 */
	@Test
	public void eventArgumentsArePassedToTheGuard() {
		final Object[] originalEventArguments = new Object[] { 1, 2, "test" };

		final Object[][] eventArguments = new Object[1][];

		final Function<SimpleStateMachine<States, Events>, States, Events, Object[], Boolean> f1 = new Function<SimpleStateMachine<States, Events>, States, Events, Object[], Boolean>() {

			@Override
			public Boolean execute(final SimpleStateMachine<States, Events> stateMachine, final Object[] parameter) {
				eventArguments[0] = parameter;
				return true;
			}

		};
		final SimpleStateMachineDefinition<States, Events> stateMachineDefinition = new SimpleStateMachineDefinition<States, Events>(
				"eventArgumentsArePassedToTheGuard", States.A);

		stateMachineDefinition.in(States.A).on(Events.A).goTo(States.B).onlyIf(f1);

		final StateMachine<States, Events> fsm = stateMachineDefinition.createPassiveStateMachine("allGuardsReturnFalse", States.A);
		fsm.start();

		fsm.fire(Events.A, originalEventArguments);

		Assert.assertSame(originalEventArguments, eventArguments[0]);
	}

	/**
	 * Only the transition with a guard returning true is executed and the event arguments are passed to the guard.
	 */
	@Test
	public void transitionWithGuardReturningTrueIsExecuted() {
		final Function<SimpleStateMachine<States, Events>, States, Events, Object[], Boolean> f1 = new Function<SimpleStateMachine<States, Events>, States, Events, Object[], Boolean>() {

			@Override
			public Boolean execute(final SimpleStateMachine<States, Events> stateMachine, final Object[] parameter) {
				return false;
			}

		};

		final Function<SimpleStateMachine<States, Events>, States, Events, Object[], Boolean> f2 = new Function<SimpleStateMachine<States, Events>, States, Events, Object[], Boolean>() {

			@Override
			public Boolean execute(final SimpleStateMachine<States, Events> stateMachine, final Object[] parameter) {
				return true;
			}

		};

		final SimpleStateMachineDefinition<States, Events> stateMachineDefinition = new SimpleStateMachineDefinition<States, Events>(
				"transitionWithGuardReturningTrueIsExecuted", States.A);

		stateMachineDefinition.in(States.A).on(Events.A).goTo(States.B).onlyIf(f1).on(Events.A).goTo(States.C).onlyIf(f2);

		final StateMachine<States, Events> fsm = stateMachineDefinition.createPassiveStateMachine("allGuardsReturnFalse", States.A);
		fsm.start();

		fsm.fire(Events.A);

		Assert.assertEquals(States.C, fsm.getCurrentState());
	}
}
