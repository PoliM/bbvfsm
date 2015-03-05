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
	

		in(State.Off).on(Event.TogglePower).goTo(State.On)
				.execute((sm, p) -> sm.logTransitionFromOffToOn())
				.onlyIf((sm, p) -> sm.isUserMode());
		in(State.Off).executeOnEntry((sm, p) -> sm.logOffEntry());
		in(State.Off).executeOnExit((sm, p) -> sm.logOffExit());

		in(State.On).on(Event.TogglePower).goTo(State.Off)
				.execute((sm, p) -> sm.logTransitionFromOnToOff());
		in(State.On).executeOnEntry((sm, p) -> sm.logOnEntry());
		in(State.On).executeOnExit((sm, p) -> sm.logOnExit());

		in(State.Off)
				.on(Event.TogglePower)
				.goTo(State.Maintenance)
				.execute(
						(sm, p) -> sm
								.logTransitionOffToMaintenance())
				.onlyIf((sm, p) -> sm.isMaintenanceMode());

		in(State.Maintenance)
				.on(Event.TogglePower)
				.goTo(State.Off)
				.execute(
						(sm, p) -> sm
								.logTransitionFromMaintenanceToOff());
		in(State.Maintenance).executeOnEntry(
				(sm, p) -> sm.logMaintenanceEntry());
		in(State.Maintenance).executeOnExit(
				(sm, p) -> sm.logMaintenanceExit());

		defineOnBla();
	}

	private void defineOnBla() {
		defineHierarchyOn(State.On, State.FM, historyTypeForOn, State.FM,
				State.AM);

		in(State.FM).on(Event.ToggleMode).goTo(State.AM)
				.execute((sm, p) -> sm.logTransitionFromFMToAM());
		in(State.FM).executeOnEntry((sm, p) -> sm.logFMEntry());
		in(State.FM).executeOnExit((sm, p) -> sm.logFMExit());

		in(State.AM).on(Event.ToggleMode).goTo(State.FM)
				.execute((sm, p) -> sm.logTransitionFromAMToFM());
		in(State.AM).executeOnEntry((sm, p) -> sm.logAMEntry());
		in(State.AM).executeOnExit((sm, p) -> sm.logAMExit());

		defineAM();
	}

	private void defineAM() {
		defineHierarchyOn(State.AM, State.Play, historyTypeForAM, State.Play,
				State.AutoTune);

		in(State.Play)
				.on(Event.StationLost)
				.goTo(State.AutoTune)
				.execute(
						(sm, p) -> sm
								.logTransitionFromPlayToAutoTune());
		in(State.Play)
				.executeOnEntry((sm, p) -> sm.logPlayEntry());
		in(State.Play).executeOnExit((sm, p) -> sm.logPlayExit());

		in(State.AutoTune)
				.on(Event.StationFound)
				.goTo(State.Play)
				.execute(
						(sm, p) -> sm
								.logTransitionFromAutoTuneToPlay());
		in(State.AutoTune).executeOnEntry(
				(sm, p) -> sm.logAutoTuneEntry());
		in(State.AutoTune).executeOnExit(
				(sm, p) -> sm.logAutoTuneExit());
	}
}
