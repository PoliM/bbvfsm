/*******************************************************************************
 *  Copyright 2010, 2011 bbv Software Services AG, Mario Martinez
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
 *     bbv Software Services AG (http://www.bbv.ch), Mario Martinez
 *******************************************************************************/

package ch.bbv.fsm.acceptance.function.definition;

import java.util.LinkedList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.action.Action;
import ch.bbv.fsm.guard.Function;
import ch.bbv.fsm.impl.AbstractStateMachine;
import ch.bbv.fsm.impl.AbstractStateMachineDefinition;
import ch.bbv.fsm.impl.StatesAndEvents.Events;
import ch.bbv.fsm.impl.StatesAndEvents.States;

/**
 * @author Mario Martinez
 * 
 */
public class FunctionDefinitionTest {

	private class FunctionDefinitionTestStateMachine
			extends
			AbstractStateMachine<FunctionDefinitionTestStateMachine, States, Events> {

		private final StringBuffer log = new StringBuffer();

		private final List<Function<FunctionDefinitionTestStateMachine, States, Events, Object[], Boolean>> callingActions = new LinkedList<Function<FunctionDefinitionTestStateMachine, States, Events, Object[], Boolean>>();

		public String consumeLog() {
			return log.toString();
		}

		public void log(final String msg) {
			this.log.append(msg);
		}

		public void addCallingAction(
				final Function<FunctionDefinitionTestStateMachine, States, Events, Object[], Boolean> callingAction) {
			callingActions.add(callingAction);
		}

		public List<Function<FunctionDefinitionTestStateMachine, States, Events, Object[], Boolean>> getCallingActions() {
			return callingActions;
		}

		protected FunctionDefinitionTestStateMachine(
				final StateMachine<States, Events> driver) {
			super(driver);
		}

	}

	private class FunctionDefinitionTestStateMachineDefinition
			extends
			AbstractStateMachineDefinition<FunctionDefinitionTestStateMachine, States, Events> {

		public FunctionDefinitionTestStateMachineDefinition(final String name,
				final States initialState) {

			super("FunctionDefinitionTestStateMachine", States.A);
		}

		@Override
		protected FunctionDefinitionTestStateMachine createStateMachine(
				final StateMachine<States, Events> driver) {
			return new FunctionDefinitionTestStateMachine(driver);
		}

	}

	public static class DoNothing implements
			Action<FunctionDefinitionTestStateMachine, States, Events> {

		@Override
		public void execute(
				final FunctionDefinitionTestStateMachine stateMachine,
				final Object... arguments) {

			return;
		}
	}

	public static class WriteLogFunction
			implements
			Function<FunctionDefinitionTestStateMachine, States, Events, Object[], Boolean> {

		@Override
		public Boolean execute(
				final FunctionDefinitionTestStateMachine stateMachine,
				final Object[] parameter) {

			stateMachine.addCallingAction(this);
			stateMachine
					.log("execute(): FunctionDefinitionTest.WriteLogFunction.class");

			return Boolean.TRUE;
		}

	}

	@Test
	public void functionWhenFunctionToExceuteThenExecuteOK() {

		final FunctionDefinitionTestStateMachineDefinition stateMachineDefinition = new FunctionDefinitionTestStateMachineDefinition(
				"simpleFSM", States.A);
		FunctionDefinitionTestStateMachine stateMachine = stateMachineDefinition
				.createPassiveStateMachine("simpleFSM", States.A);

		stateMachineDefinition.in(States.A).on(Events.A).goTo(States.B)
				.execute(FunctionDefinitionTest.DoNothing.class)
				.onlyIf(FunctionDefinitionTest.WriteLogFunction.class);

		stateMachine.start();
		stateMachine.fire(Events.A);

		Assert.assertEquals(stateMachine.consumeLog(),
				"execute(): FunctionDefinitionTest.WriteLogFunction.class");

	}

	// Test multiple State Machine instances

	@Test
	public void multipleFunctionWhenMultipleStateMachinesWithSameDefinitionThenDifferentFunctionInstancesAreExecuted() {

		final FunctionDefinitionTestStateMachineDefinition multipleStateMachineDefinition = new FunctionDefinitionTestStateMachineDefinition(
				"sampleDef", States.A);

		final FunctionDefinitionTestStateMachine testee1 = multipleStateMachineDefinition
				.createPassiveStateMachine("sample", States.A);
		final FunctionDefinitionTestStateMachine testee2 = multipleStateMachineDefinition
				.createPassiveStateMachine("sample", States.A);

		multipleStateMachineDefinition.in(States.A).on(Events.A)
				.execute(FunctionDefinitionTest.DoNothing.class)
				.onlyIf(FunctionDefinitionTest.WriteLogFunction.class);

		testee1.start();
		testee2.start();

		testee1.fire(Events.A);
		testee2.fire(Events.A);

		Assert.assertEquals("Invalid size", 1, testee1.getCallingActions()
				.size());
		Assert.assertEquals("Invalid size", 1, testee2.getCallingActions()
				.size());
		Assert.assertNotSame("Different Action Objects", testee1
				.getCallingActions().get(0), testee2.getCallingActions().get(0));

	}
}
