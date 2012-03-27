package ch.bbv.fsm.acceptance.elevator;

import ch.bbv.fsm.HistoryType;
import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.action.Action;
import ch.bbv.fsm.guard.Function;
import ch.bbv.fsm.impl.AbstractStateMachineDefinition;

public class ElevatorStateMachineDefinition
		extends
		AbstractStateMachineDefinition<ElevatorStateMachine, ElevatorStateMachineDefinition.State, ElevatorStateMachineDefinition.Event> {

	/**
	 * Elevator states.
	 */
	enum State {
		/** Elevator has an Error. */
		Error,

		/** Elevator is healthy, i.e. no error */
		Healthy,

		/** The elevator is moving (either up or down). */
		Moving,

		/** The elevator is moving down. */
		MovingUp,

		/** The elevator is moving down. */
		MovingDown,

		/** The elevator is standing on a floor. */
		OnFloor,

		/** The door is closed while standing still. */
		DoorClosed,

		/** The dor is open while standing still. */
		DoorOpen
	}

	/**
	 * Elevator Events.
	 */
	enum Event {
		/** An error occurred. */
		ErrorOccured,

		/** Reset after error. */
		Reset,

		/** Open the door. */
		OpenDoor,

		/** Close the door. */
		CloseDoor,

		/** Move elevator up. */
		GoUp,

		/** Move elevator down. */
		GoDown,

		/** Stop the elevator. */
		Stop
	}

	/**
	 * Announces the floor.
	 */
	public static class AnnounceFloorAction implements
			Action<ElevatorStateMachine, State, Event> {

		@Override
		public void execute(final ElevatorStateMachine stateMachine,
				final Object... arguments) {
			System.out.println("announceFloor: 1");
		}
	}

	/**
	 * Announces that the elevator is overloaded.
	 */
	public static class AnnounceOverloadAction implements
			Action<ElevatorStateMachine, State, Event> {

		@Override
		public void execute(final ElevatorStateMachine stateMachine,
				final Object... arguments) {
			System.out.println("announceOverload...");
		}
	}

	/**
	 * Checks whether the elevator is overloaded.
	 */
	public static class CheckOverloadFunction implements
			Function<ElevatorStateMachine, State, Event, Object[], Boolean> {
		@Override
		public Boolean execute(final ElevatorStateMachine stateMachine,
				final Object[] arguments) {
			return true;
		};
	};

	public ElevatorStateMachineDefinition() {
		super(State.Healthy);
		define();
	}

	private void define() {
		defineHierarchyOn(State.Healthy, State.OnFloor, HistoryType.DEEP,
				State.OnFloor, State.Moving);
		defineHierarchyOn(State.Moving, State.MovingUp, HistoryType.SHALLOW,
				State.MovingUp, State.MovingDown);
		defineHierarchyOn(State.OnFloor, State.DoorClosed, HistoryType.NONE,
				State.DoorClosed, State.DoorOpen);

		in(State.Healthy).on(Event.ErrorOccured).goTo(State.Error);
		in(State.Error).on(Event.Reset).goTo(State.Healthy);
		in(State.OnFloor)
				.executeOnEntry(
						ElevatorStateMachineDefinition.AnnounceFloorAction.class)
				.on(Event.CloseDoor)
				.goTo(State.DoorClosed)
				.on(Event.OpenDoor)
				.goTo(State.DoorOpen)
				.on(Event.GoUp)
				.goTo(State.MovingUp)
				.onlyIf(ElevatorStateMachineDefinition.CheckOverloadFunction.class)
				.on(Event.GoUp)
				.execute(
						ElevatorStateMachineDefinition.AnnounceOverloadAction.class)
				.on(Event.GoDown)
				.goTo(State.MovingDown)
				.onlyIf(ElevatorStateMachineDefinition.CheckOverloadFunction.class);

		in(State.Moving).on(Event.Stop).goTo(State.OnFloor);
	}

	@Override
	protected ElevatorStateMachine createStateMachine(
			final StateMachine<State, Event> driver) {
		return new ElevatorStateMachine(driver);
	}
}
