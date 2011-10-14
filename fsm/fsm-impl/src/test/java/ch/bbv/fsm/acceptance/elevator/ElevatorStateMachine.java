package ch.bbv.fsm.acceptance.elevator;

import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.acceptance.elevator.ElevatorStateMachineDefinition.Event;
import ch.bbv.fsm.acceptance.elevator.ElevatorStateMachineDefinition.State;
import ch.bbv.fsm.impl.AbstractStateMachine;

public class ElevatorStateMachine extends
		AbstractStateMachine<ElevatorStateMachine, ElevatorStateMachineDefinition.State, ElevatorStateMachineDefinition.Event> {

	ElevatorStateMachine(final StateMachine<State, Event> driver) {
		super(driver);
	}

}
