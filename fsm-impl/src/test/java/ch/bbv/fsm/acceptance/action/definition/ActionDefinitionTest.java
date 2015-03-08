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
				.execute(new ActionDefinition());

		Assert.assertTrue(true);

	}

	@Test
	public void actionDefinitionWhenActionIsInnerClassAndStaticThenExecute() {

		final SimpleStateMachineDefinition multipleStateMachineDefinition = new SimpleStateMachineDefinition();

		multipleStateMachineDefinition.in(State.state_init).on(Event.move)
				.execute(new ActionDefinition.ActionDefinitionInnerStatic());

		Assert.assertTrue(true);

	}

	@Test
	public void actionDefinitionWhenActionIsInnerClassAndStaticSecondLevelThenOK() {

		final SimpleStateMachineDefinition multipleStateMachineDefinition = new SimpleStateMachineDefinition();

		multipleStateMachineDefinition
				.in(State.state_init)
				.on(Event.move)
				.execute(
						new ActionDefinition.ActionDefinitionInnerStatic.ActionDefinitionInnerStaticSecondLevel());

		Assert.assertTrue(true);

	}

	// Test execute / Entry / Exit

	@Test
	public void actionExecutionWhenExecuteThenExecute() {

		final SimpleStateMachineDefinition simpleStateMachineDefinition = new SimpleStateMachineDefinition();
		final SimpleStateMachine testee = simpleStateMachineDefinition
				.createPassiveStateMachine("sample", State.state_init);

		// InternalState Machine Behavior
		simpleStateMachineDefinition.in(State.state_init).on(Event.move)
				.execute(new ActionDefinition());

		testee.start();
		testee.fire(Event.move);

		Assert.assertEquals("execute: ActionDefinition.execute()",
				testee.consumeLog());

	}

	@Test
	public void actionExecutionWhenExecuteOnEntryThenExecute() {

		final SimpleStateMachineDefinition simpleStateMachineDefinition = new SimpleStateMachineDefinition();

		// InternalState Machine Behavior
		simpleStateMachineDefinition.in(State.state_init).executeOnEntry(
				new ActionDefinition.ActionDefinitionInnerStatic());

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

		// InternalState Machine Behavior
		simpleStateMachineDefinition.in(State.state_init).executeOnEntry(
				new ActionDefinition.ActionDefinitionEntryWithParameter(),
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

		// InternalState Machine Behavior
		simpleStateMachineDefinition.in(State.state_init).executeOnExit(
				new ActionDefinition.ActionDefinitionInnerStatic());
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

		// InternalState Machine Behavior
		simpleStateMachineDefinition.in(State.state_init).executeOnExit(
				new ActionDefinition.ActionDefinitionEntryWithParameter(),
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

}
