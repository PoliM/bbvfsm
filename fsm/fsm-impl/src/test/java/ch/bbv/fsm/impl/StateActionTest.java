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

	private class StateActionTestStateMachine extends
			AbstractStateMachine<StateActionTestStateMachine, States, Events> {

		private Boolean entered;

		private Object[] arguments;

		protected StateActionTestStateMachine(
				final StateMachine<States, Events> driver) {
			super(driver);
		}

		public Boolean getEntered() {
			return entered;
		}

		public void setEntered(final Boolean entered) {
			this.entered = entered;
		}

		public Object[] getArguments() {
			return arguments;
		}

		public void setArguments(final Object[] arguments) {
			this.arguments = arguments;
		}

	}

	private class StateActionTestStateMachineDefinition
			extends
			AbstractStateMachineDefinition<StateActionTestStateMachine, States, Events> {

		public StateActionTestStateMachineDefinition() {
			super("StateActionTestStateMachineDefinition", States.A);
		}

		@Override
		protected StateActionTestStateMachine createStateMachine(
				final StateMachine<States, Events> driver) {
			return new StateActionTestStateMachine(driver);
		}

	}

	public static class ActionClass implements
			Action<StateActionTestStateMachine, States, Events> {

		@Override
		public void execute(final StateActionTestStateMachine stateMachine,
				final Object... arguments) {

			stateMachine.setEntered(true);

		}
	}

	public static class ActionWithParameterClass implements
			Action<StateActionTestStateMachine, States, Events> {

		@Override
		public void execute(final StateActionTestStateMachine stateMachine,
				final Object... arguments) {
			stateMachine.setArguments(arguments);
		}
	}

	/**
	 * Entry actions are executed when a state is entered.
	 */
	@Test
	public void entryAction() {

		final StateActionTestStateMachineDefinition stateMachineDefinition = new StateActionTestStateMachineDefinition();

		stateMachineDefinition.in(States.A).executeOnEntry(ActionClass.class);

		final StateActionTestStateMachine fsm = stateMachineDefinition
				.createPassiveStateMachine("entryAction", States.A);
		fsm.start();

		Assert.assertTrue(fsm.getEntered());
	}

	/**
	 * Exit actions are executed when a state is left.
	 */
	@Test
	public void exitAction() {

		final StateActionTestStateMachineDefinition stateMachineDefinition = new StateActionTestStateMachineDefinition();

		stateMachineDefinition.in(States.A).executeOnExit(ActionClass.class)
				.on(Events.B).goTo(States.B);

		final StateActionTestStateMachine fsm = stateMachineDefinition
				.createPassiveStateMachine("exitAction", States.A);
		fsm.start();
		fsm.fire(Events.B);

		Assert.assertTrue(fsm.getEntered());
	}

	/**
	 * Entry actions can be parametrized.
	 */
	@Test
	public void parameterizedEntryAction() {

		final StateActionTestStateMachineDefinition stateMachineDefinition = new StateActionTestStateMachineDefinition();

		stateMachineDefinition.in(States.A).executeOnEntry(
				ActionWithParameterClass.class, 3);

		final StateActionTestStateMachine fsm = stateMachineDefinition
				.createPassiveStateMachine("parameterizedEntryAction", States.A);

		fsm.start();

		Assert.assertEquals(3, fsm.getArguments()[0]);
	}

	/**
	 * Exit actions can be parametrized.
	 */
	@Test
	public void parametrizedExitAction() {

		final StateActionTestStateMachineDefinition stateMachineDefinition = new StateActionTestStateMachineDefinition();

		stateMachineDefinition.in(States.A)
				.executeOnExit(ActionWithParameterClass.class, 3).on(Events.B)
				.goTo(States.B);

		final StateActionTestStateMachine fsm = stateMachineDefinition
				.createPassiveStateMachine("exitAction", States.A);
		fsm.start();
		fsm.fire(Events.B);

		Assert.assertEquals(3, fsm.getArguments()[0]);
	}
}
