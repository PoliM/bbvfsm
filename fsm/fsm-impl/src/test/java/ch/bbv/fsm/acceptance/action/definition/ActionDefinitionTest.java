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

package ch.bbv.fsm.acceptance.action.definition;

import junit.framework.Assert;

import org.junit.Test;

import ch.bbv.fsm.acceptance.action.definition.SimpleStateMachineDefinition.Event;
import ch.bbv.fsm.acceptance.action.definition.SimpleStateMachineDefinition.State;
import ch.bbv.fsm.dsl.IllegalActionClassDefinitionException;

/**
 * @author Mario Martinez
 * 
 */
public class ActionDefinitionTest {

	// Test valid action class definition

	@Test
	public void actionDefinitionWhenActionIsRegularClassThenOK() {

		final SimpleStateMachineDefinition multipleStateMachineDefinition = new SimpleStateMachineDefinition();

		multipleStateMachineDefinition.in(State.state_init).on(Event.move)
				.execute(ActionDefinition.class);

		Assert.assertTrue(true);

	}

	@Test
	public void actionDefinitionWhenActionIsInnerClassAndStaticThenExecute() {

		final SimpleStateMachineDefinition multipleStateMachineDefinition = new SimpleStateMachineDefinition();

		multipleStateMachineDefinition.in(State.state_init).on(Event.move)
				.execute(ActionDefinition.ActionDefinitionInnerStatic.class);

		Assert.assertTrue(true);

	}

	@Test
	public void actionDefinitionWhenActionIsInnerClassAndStaticSecondLevelThenOK() {

		final SimpleStateMachineDefinition multipleStateMachineDefinition = new SimpleStateMachineDefinition();

		multipleStateMachineDefinition
				.in(State.state_init)
				.on(Event.move)
				.execute(
						ActionDefinition.ActionDefinitionInnerStatic.ActionDefinitionInnerStaticSecondLevel.class);

		Assert.assertTrue(true);

	}

	// Test invalid action class definition

	@Test(expected = IllegalActionClassDefinitionException.class)
	public void actionDefinitionWhenInnerNotStaticClassDefinitionThenException() {

		final SimpleStateMachineDefinition multipleStateMachineDefinition = new SimpleStateMachineDefinition();

		multipleStateMachineDefinition.in(State.state_init).on(Event.move)
				.execute(ActionDefinition.ActionDefinitionInner.class);

	}

	@Test(expected = IllegalActionClassDefinitionException.class)
	public void actionDefinitionWhenInnerStaticClassAndNotDefaultConstructorThenException() {

		final SimpleStateMachineDefinition multipleStateMachineDefinition = new SimpleStateMachineDefinition();

		multipleStateMachineDefinition
				.in(State.state_init)
				.on(Event.move)
				.execute(
						ActionDefinition.ActionDefinitionInnerWithoutDefaultConstructor.class);

	}

	// Test execute / Entry / Exit

	@Test
	public void actionExecutionWhenExecuteThenExecute() {

		final SimpleStateMachineDefinition simpleStateMachineDefinition = new SimpleStateMachineDefinition();
		final SimpleStateMachine testee = simpleStateMachineDefinition
				.createPassiveStateMachine("sample", State.state_init);

		// State Machine Behavior
		simpleStateMachineDefinition.in(State.state_init).on(Event.move)
				.execute(ActionDefinition.class);

		testee.start();
		testee.fire(Event.move);

		Assert.assertEquals("execute: ActionDefinition.execute()",
				testee.consumeLog());

	}

	@Test
	public void actionExecutionWhenExecuteOnEntryThenExecute() {

		final SimpleStateMachineDefinition simpleStateMachineDefinition = new SimpleStateMachineDefinition();

		// State Machine Behavior
		simpleStateMachineDefinition.in(State.state_init).executeOnEntry(
				ActionDefinition.ActionDefinitionInnerStatic.class);

		final SimpleStateMachine testee = simpleStateMachineDefinition
				.createPassiveStateMachine("sample", State.state_init);

		testee.start();
		testee.fire(Event.move);
		testee.terminate();

		Assert.assertEquals(
				"execute: ActionDefinition$ActionDefinitionInnerStatic.execute()",
				testee.consumeLog());

	}

	@Test
	public void actionExecutionWhenExecuteOnEntryWithParameterThenExecute() {

		final SimpleStateMachineDefinition simpleStateMachineDefinition = new SimpleStateMachineDefinition();

		// State Machine Behavior
		simpleStateMachineDefinition.in(State.state_init).executeOnEntry(
				ActionDefinition.ActionDefinitionEntryWithParameter.class,
				"parameter1");

		final SimpleStateMachine testee = simpleStateMachineDefinition
				.createPassiveStateMachine("sample", State.state_init);

		testee.start();
		testee.fire(Event.move);
		testee.terminate();

		Assert.assertEquals(
				"execute: ActionDefinition$ActionDefinitionEntryWithParameter.execute(parameter1)",
				testee.consumeLog());

	}

	@Test
	public void actionExecutionWhenExecuteOnExitThenExecute() {

		final SimpleStateMachineDefinition simpleStateMachineDefinition = new SimpleStateMachineDefinition();

		// State Machine Behavior
		simpleStateMachineDefinition.in(State.state_init).executeOnExit(
				ActionDefinition.ActionDefinitionInnerStatic.class);
		simpleStateMachineDefinition.in(State.state_init).on(Event.move)
				.goTo(State.state_entry);

		final SimpleStateMachine testee = simpleStateMachineDefinition
				.createPassiveStateMachine("sample", State.state_init);

		testee.start();
		testee.fire(Event.move);
		testee.terminate();

		Assert.assertEquals(
				"execute: ActionDefinition$ActionDefinitionInnerStatic.execute()",
				testee.consumeLog());

	}

	@Test
	public void actionExecutionWhenExecuteOnExitWithParameterThenExecute() {

		final SimpleStateMachineDefinition simpleStateMachineDefinition = new SimpleStateMachineDefinition();

		// State Machine Behavior
		simpleStateMachineDefinition.in(State.state_init).executeOnExit(
				ActionDefinition.ActionDefinitionEntryWithParameter.class,
				"parameter1");
		simpleStateMachineDefinition.in(State.state_init).on(Event.move)
				.goTo(State.state_entry);

		final SimpleStateMachine testee = simpleStateMachineDefinition
				.createPassiveStateMachine("sample", State.state_init);

		testee.start();
		testee.fire(Event.move);
		testee.terminate();

		Assert.assertEquals(
				"execute: ActionDefinition$ActionDefinitionEntryWithParameter.execute(parameter1)",
				testee.consumeLog());
	}

	// Test multiple State Machine instances

	@Test
	public void multipleActionWhenMultipleStateMachinesWithSameDefinitionThenDifferentActionInstancesAreExecuted() {

		final SimpleStateMachineDefinition multipleStateMachineDefinition = new SimpleStateMachineDefinition();

		final SimpleStateMachine testee1 = multipleStateMachineDefinition
				.createPassiveStateMachine("sample", State.state_init);
		final SimpleStateMachine testee2 = multipleStateMachineDefinition
				.createPassiveStateMachine("sample", State.state_init);

		multipleStateMachineDefinition.in(State.state_init).executeOnEntry(
				ActionDefinition.class);

		testee1.start();
		testee2.start();

		Assert.assertEquals("Invalid size", 1, testee1.getCallingActions()
				.size());
		Assert.assertEquals("Invalid size", 1, testee2.getCallingActions()
				.size());
		Assert.assertNotSame("Different Action Objects", testee1
				.getCallingActions().get(0), testee2.getCallingActions().get(0));

	}
}
