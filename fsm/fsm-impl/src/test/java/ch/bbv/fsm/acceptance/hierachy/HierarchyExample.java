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
package ch.bbv.fsm.acceptance.hierachy;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import ch.bbv.fsm.HistoryType;
import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.impl.SimpleStateMachineDefinition;

/**
 * Sample showing the usage of state machine.
 */
public class HierarchyExample {

	public enum Events {
		toA, toB, toB2, toC, toD, toD2
	};

	public enum States {
		A, B, B_1, B_2, C, D, D_1, D_2
	}

	private SimpleStateMachineDefinition<States, Events> hierarchyExampleStateMachineDefinition;

	@Before
	public void setup() {
		hierarchyExampleStateMachineDefinition = new SimpleStateMachineDefinition<HierarchyExample.States, HierarchyExample.Events>(
				"hierarchyExampleStateMachine", States.A);

		hierarchyExampleStateMachineDefinition.defineHierarchyOn(States.B, States.B_1, HistoryType.NONE, States.B_1, States.B_2);
		hierarchyExampleStateMachineDefinition.defineHierarchyOn(States.D, States.D_1, HistoryType.SHALLOW, States.D_1, States.D_2);

		hierarchyExampleStateMachineDefinition.in(States.A).on(Events.toB).goTo(States.B);
		hierarchyExampleStateMachineDefinition.in(States.B).on(Events.toB).goTo(States.B);
		hierarchyExampleStateMachineDefinition.in(States.B).on(Events.toC).goTo(States.C);
		hierarchyExampleStateMachineDefinition.in(States.C).on(Events.toD).goTo(States.D);
		hierarchyExampleStateMachineDefinition.in(States.D).on(Events.toA).goTo(States.A);
		hierarchyExampleStateMachineDefinition.in(States.B_1).on(Events.toB2).goTo(States.B_2);
		hierarchyExampleStateMachineDefinition.in(States.D_1).on(Events.toD2).goTo(States.D_2);
	}

	@Test
	public void testDeep() {
		final StateMachine<States, Events> testee = hierarchyExampleStateMachineDefinition.createPassiveStateMachine("testDeep", States.A);
		testee.start();

		testee.fire(Events.toB, true);
		testee.fire(Events.toC);
		testee.fire(Events.toD);
		final States stateD1 = testee.getCurrentState();
		testee.fire(Events.toD2);
		final States stateD2 = testee.getCurrentState();
		testee.fire(Events.toA);
		testee.fire(Events.toB);
		testee.fire(Events.toC);
		testee.fire(Events.toD);
		final States stateD2_2 = testee.getCurrentState();

		testee.terminate();

		Assert.assertEquals(States.D_1, stateD1);
		Assert.assertEquals(States.D_2, stateD2);
		Assert.assertEquals(States.D_2, stateD2_2);
	}

	@Test
	public void testGoDownAndEventsInSuperState() {
		final StateMachine<States, Events> testee = hierarchyExampleStateMachineDefinition.createPassiveStateMachine(
				"testGoDownAndEventsInSuperState", States.A);

		testee.start();
		testee.fire(Events.toB, true);
		final States stateB1 = testee.getCurrentState();
		testee.fire(Events.toC);
		final States stateC = testee.getCurrentState();

		testee.terminate();

		Assert.assertEquals(States.B_1, stateB1);
		Assert.assertEquals(States.C, stateC);
	}

	@Test
	public void testShallow() {
		final StateMachine<States, Events> testee = hierarchyExampleStateMachineDefinition.createPassiveStateMachine("testShallow",
				States.A);

		testee.start();
		testee.fire(Events.toB, true);
		final States stateB1 = testee.getCurrentState();
		testee.fire(Events.toB2);
		final States stateB2 = testee.getCurrentState();
		testee.fire(Events.toC);
		final States stateC = testee.getCurrentState();
		testee.fire(Events.toD);
		testee.fire(Events.toA);
		testee.fire(Events.toB);
		final States stateB1_2 = testee.getCurrentState();
		testee.terminate();

		Assert.assertEquals(States.B_1, stateB1);
		Assert.assertEquals(States.B_2, stateB2);
		Assert.assertEquals(States.C, stateC);
		Assert.assertEquals(States.B_1, stateB1_2);
	}
}
