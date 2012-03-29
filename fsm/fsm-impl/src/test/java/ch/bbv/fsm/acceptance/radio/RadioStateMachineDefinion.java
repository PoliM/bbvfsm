package ch.bbv.fsm.acceptance.radio;

import ch.bbv.fsm.HistoryType;
import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.impl.AbstractStateMachineDefinition;

public class RadioStateMachineDefinion
		extends
		AbstractStateMachineDefinition<RadioStateMachine, RadioStateMachineDefinion.State, RadioStateMachineDefinion.Event> {

	public enum State {
		Off, // Radio is turned off
		On, // Radio is turned on
		Maintenance, // Selfcheck radio
		// 'On' sub-states
		FM, // Playing on FM
		AM, // Playing on AM
		// 'AM' sub-states
		Play, // Play the current frequency
		AutoTune, // Search for station

	}

	public enum Event {
		TogglePower, // Switch on an off
		ToggleMode, // Switch between AM and FM
		StationLost, // Not station on this AM frequency
		StationFound // Found a station's frequency
	}

	private final HistoryType historyTypeForOn;
	private final HistoryType historyTypeForAM;

	public RadioStateMachineDefinion() {
		this(HistoryType.DEEP, HistoryType.NONE);
	}

	public RadioStateMachineDefinion(final HistoryType historyTypeForOn,
			final HistoryType historyTypeForAM) {
		super(State.Off);
		this.historyTypeForOn = historyTypeForOn;
		this.historyTypeForAM = historyTypeForAM;
		define();
	}

	@Override
	protected RadioStateMachine createStateMachine(
			final StateMachine<State, Event> driver) {
		return new RadioStateMachine(driver);
	}

	private void define() {
		final RadioStateMachine radioStateMachinePrototype = getPrototype();

		in(State.Off).on(Event.TogglePower).goTo(State.On)
				.execute(radioStateMachinePrototype.logTransitionFromOffToOn())
				.onlyIf(radioStateMachinePrototype.isUserMode());
		in(State.Off).executeOnEntry(radioStateMachinePrototype.logOffEntry());
		in(State.Off).executeOnExit(radioStateMachinePrototype.logOffExit());

		in(State.On).on(Event.TogglePower).goTo(State.Off)
				.execute(radioStateMachinePrototype.logTransitionFromOnToOff());
		in(State.On).executeOnEntry(radioStateMachinePrototype.logOnEntry());
		in(State.On).executeOnExit(radioStateMachinePrototype.logOnExit());

		in(State.Off)
				.on(Event.TogglePower)
				.goTo(State.Maintenance)
				.execute(
						radioStateMachinePrototype
								.logTransitionOffToMaintenance())
				.onlyIf(radioStateMachinePrototype.isMaintenanceMode());

		in(State.Maintenance)
				.on(Event.TogglePower)
				.goTo(State.Off)
				.execute(
						radioStateMachinePrototype
								.logTransitionFromMaintenanceToOff());
		in(State.Maintenance).executeOnEntry(
				radioStateMachinePrototype.logMaintenanceEntry());
		in(State.Maintenance).executeOnExit(
				radioStateMachinePrototype.logMaintenanceExit());

		defineOn(radioStateMachinePrototype);
	}

	private void defineOn(final RadioStateMachine radioStateMachinePrototype) {
		defineHierarchyOn(State.On, State.FM, historyTypeForOn, State.FM,
				State.AM);

		in(State.FM).on(Event.ToggleMode).goTo(State.AM)
				.execute(radioStateMachinePrototype.logTransitionFromFMToAM());
		in(State.FM).executeOnEntry(radioStateMachinePrototype.logFMEntry());
		in(State.FM).executeOnExit(radioStateMachinePrototype.logFMExit());

		in(State.AM).on(Event.ToggleMode).goTo(State.FM)
				.execute(radioStateMachinePrototype.logTransitionFromAMToFM());
		in(State.AM).executeOnEntry(radioStateMachinePrototype.logAMEntry());
		in(State.AM).executeOnExit(radioStateMachinePrototype.logAMExit());

		defineAM(radioStateMachinePrototype);
	}

	private void defineAM(final RadioStateMachine radioStateMachinePrototype) {
		defineHierarchyOn(State.AM, State.Play, historyTypeForAM, State.Play,
				State.AutoTune);

		in(State.Play)
				.on(Event.StationLost)
				.goTo(State.AutoTune)
				.execute(
						radioStateMachinePrototype
								.logTransitionFromPlayToAutoTune());
		in(State.Play)
				.executeOnEntry(radioStateMachinePrototype.logPlayEntry());
		in(State.Play).executeOnExit(radioStateMachinePrototype.logPlayExit());

		in(State.AutoTune)
				.on(Event.StationFound)
				.goTo(State.Play)
				.execute(
						radioStateMachinePrototype
								.logTransitionFromAutoTuneToPlay());
		in(State.AutoTune).executeOnEntry(
				radioStateMachinePrototype.logAutoTuneEntry());
		in(State.AutoTune).executeOnExit(
				radioStateMachinePrototype.logAutoTuneExit());
	}
}
