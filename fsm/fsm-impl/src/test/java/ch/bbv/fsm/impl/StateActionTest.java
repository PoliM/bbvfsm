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
import ch.bbv.fsm.impl.StatesAndEvents.Events;
import ch.bbv.fsm.impl.StatesAndEvents.States;

public class StateActionTest {
	/**
	 * Entry actions are executed when a state is entered.
	 */
	@Test
	public void entryAction() {
		final SimpleStateMachineDefinition<States, Events> stateMachineDefinition = new SimpleStateMachineDefinition<States, Events>("entryAction", States.A);

		final boolean[] entered = new boolean[1];

		final Action<SimpleStateMachine<States, Events>, States, Events> action = new Action<SimpleStateMachine<States, Events>, States, Events>() {

			@Override
			public void execute(final SimpleStateMachine<States, Events> stateMachine, final Object... arguments) {
				entered[0] = true;
			}

		};

		stateMachineDefinition.in(States.A).executeOnEntry(action);

		final StateMachine<States, Events> fsm = stateMachineDefinition.createPassiveStateMachine("entryAction", States.A);
		fsm.start();

		Assert.assertTrue(entered[0]);
	}

	/**
	 * Exit actions are executed when a state is left.
	 */
	@Test
	public void exitAction() {
		final SimpleStateMachineDefinition<States, Events> stateMachineDefinition = new SimpleStateMachineDefinition<States, Events>("exitAction", States.A);

		final boolean[] entered = new boolean[1];

		final Action<SimpleStateMachine<States, Events>, States, Events> action = new Action<SimpleStateMachine<States, Events>, States, Events>() {

			@Override
			public void execute(final SimpleStateMachine<States, Events> stateMachine, final Object... arguments) {
				entered[0] = true;
			}

		};

		stateMachineDefinition.in(States.A).executeOnExit(action).on(Events.B).goTo(States.B);

		final StateMachine<States, Events> fsm = stateMachineDefinition.createPassiveStateMachine("exitAction", States.A);
		fsm.start();
		fsm.fire(Events.B);

		Assert.assertTrue(entered[0]);
	}

	/**
	 * Entry actions can be parametrized.
	 */
	@Test
	public void parameterizedEntryAction() {
		final SimpleStateMachineDefinition<States, Events> stateMachineDefinition = new SimpleStateMachineDefinition<States, Events>("parameterizedEntryAction", States.A);

		final int[] argument = new int[1];
		argument[0] = 0;

		final Action<SimpleStateMachine<States, Events>, States, Events> action = new Action<SimpleStateMachine<States, Events>, States, Events>() {

			@Override
			public void execute(final SimpleStateMachine<States, Events> stateMachine, final Object... arguments) {
				argument[0] = (Integer) arguments[0];
			}

		};

		stateMachineDefinition.in(States.A).executeOnEntry(action, 3);

		final StateMachine<States, Events> fsm = stateMachineDefinition.createPassiveStateMachine("parameterizedEntryAction", States.A);

		fsm.start();

		Assert.assertEquals(3, argument[0]);
	}

	/**
	 * Exit actions can be parametrized.
	 */
	@Test
	public void parametrizedExitAction() {
		final SimpleStateMachineDefinition<States, Events> stateMachineDefinition = new SimpleStateMachineDefinition<States, Events>("parametrizedExitAction", States.A);

		final int[] argument = new int[1];
		argument[0] = 0;

		final Action<SimpleStateMachine<States, Events>, States, Events> action = new Action<SimpleStateMachine<States, Events>, States, Events>() {

			@Override
			public void execute(final SimpleStateMachine<States, Events> stateMachine, final Object... arguments) {
				argument[0] = (Integer) arguments[0];
			}

		};

		stateMachineDefinition.in(States.A).executeOnExit(action, 3).on(Events.B).goTo(States.B);

		final StateMachine<States, Events> fsm = stateMachineDefinition.createPassiveStateMachine("exitAction", States.A);
		fsm.start();
		fsm.fire(Events.B);

		Assert.assertEquals(3, argument[0]);
	}
}
