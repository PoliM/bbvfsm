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
package ch.bbv.fsm.acceptance.elevator;

import org.junit.Test;

import ch.bbv.fsm.acceptance.elevator.ElevatorStateMachineDefinition.Event;
import ch.bbv.fsm.acceptance.elevator.ElevatorStateMachineDefinition.State;

/**
 * Sample showing usage of state machine.
 */
public class ElevatorStateMachineAcceptanceTest {

	private final ElevatorStateMachineDefinition elevatorStateMachineDefinition = new ElevatorStateMachineDefinition();

	/**
	 * Unit test showing a sample of the state machine usage.
	 */
	@Test
	public void sample() {
		final ElevatorStateMachine testee = elevatorStateMachineDefinition.createPassiveStateMachine("sample", State.OnFloor);

		testee.fire(Event.ErrorOccured);
		testee.fire(Event.Reset);

		testee.start();

		testee.fire(Event.OpenDoor);
		testee.fire(Event.CloseDoor);
		testee.fire(Event.GoUp);
		testee.fire(Event.Stop);
		testee.fire(Event.OpenDoor);

		testee.terminate();
	}
}
