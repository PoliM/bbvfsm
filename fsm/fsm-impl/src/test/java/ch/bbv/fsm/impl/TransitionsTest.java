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
import ch.bbv.fsm.events.StateMachineEventAdapter;
import ch.bbv.fsm.events.TransitionEventArgs;
import ch.bbv.fsm.impl.StatesAndEvents.Events;
import ch.bbv.fsm.impl.StatesAndEvents.States;

public class TransitionsTest {
	private class Handler extends StateMachineEventAdapter<SimpleStateMachine<States, Events>, States, Events> {

		@Override
		public void onTransitionDeclined(final TransitionEventArgs<SimpleStateMachine<States, Events>, States, Events> arg) {
			TransitionsTest.this.declined = true;

		}
	}

	private Object[] action1Arguments = null;
	private Object[] action2Arguments = null;
	private Boolean executed = false;

	private boolean declined = false;

	/**
	 * Actions on transitions are performed and the event arguments are passed.
	 */
	@Test
	public void executeActions() {

		final Action<SimpleStateMachine<States, Events>, States, Events> action1 = new Action<SimpleStateMachine<States, Events>, States, Events>() {

			@Override
			public void execute(final SimpleStateMachine<States, Events> stateMachine, final Object... arguments) {
				TransitionsTest.this.action1Arguments = arguments;

			}
		};

		final Action<SimpleStateMachine<States, Events>, States, Events> action2 = new Action<SimpleStateMachine<States, Events>, States, Events>() {

			@Override
			public void execute(final SimpleStateMachine<States, Events> stateMachine, final Object... arguments) {
				TransitionsTest.this.action2Arguments = arguments;

			}
		};

		final SimpleStateMachineDefinition<States, Events> stateMachineDefinition = new SimpleStateMachineDefinition<States, Events>(
				"executeActions", States.A);

		stateMachineDefinition.in(States.A).on(Events.B).goTo(States.B).execute(action1).execute(action2);

		final StateMachine<States, Events> fsm = stateMachineDefinition.createPassiveStateMachine("transitionTest", States.A);
		fsm.start();

		final Object[] eventArguments = new Object[] { 1, 2, 3, "test" };
		fsm.fire(Events.B, eventArguments);

		Assert.assertArrayEquals(eventArguments, this.action1Arguments);
		Assert.assertArrayEquals(eventArguments, this.action2Arguments);
	}

	/**
	 * Internal transitions can be executed (internal transition = transition that remains in the same state and does not execute exit and
	 * entry actions.
	 */
	@Test
	public void internalTransition() {

		final Action<SimpleStateMachine<States, Events>, States, Events> action2 = new Action<SimpleStateMachine<States, Events>, States, Events>() {

			@Override
			public void execute(final SimpleStateMachine<States, Events> stateMachine, final Object... arguments) {
				TransitionsTest.this.executed = true;

			}
		};

		final SimpleStateMachineDefinition<States, Events> stateMachineDefinition = new SimpleStateMachineDefinition<States, Events>(
				"internalTransition", States.A);
		stateMachineDefinition.in(States.A).on(Events.A).execute(action2);
		final StateMachine<States, Events> fsm = stateMachineDefinition.createPassiveStateMachine("transitionTest", States.A);
		fsm.start();
		fsm.fire(Events.A);

		Assert.assertTrue(this.executed);
		Assert.assertEquals(States.A, fsm.getCurrentState());
	}

	/**
	 * When no transition for the fired event can be found in the entire hierarchy up from the current state then.
	 */
	@Test
	public void missingTransition() {
		final SimpleStateMachineDefinition<States, Events> stateMachineDefinition = new SimpleStateMachineDefinition<States, Events>(
				"missingTransition", States.A);
		stateMachineDefinition.in(States.A).on(Events.B).goTo(States.B);

		stateMachineDefinition.addEventHandler(new Handler());
		final StateMachine<States, Events> fsm = stateMachineDefinition.createPassiveStateMachine("transitionTest", States.A);
		fsm.start();

		fsm.fire(Events.C);

		Assert.assertTrue(this.declined);
		Assert.assertEquals(States.A, fsm.getCurrentState());
	}
}
