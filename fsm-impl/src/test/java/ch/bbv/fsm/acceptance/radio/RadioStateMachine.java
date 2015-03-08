package ch.bbv.fsm.acceptance.radio;

import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.acceptance.radio.RadioStateMachineDefinion.Event;
import ch.bbv.fsm.acceptance.radio.RadioStateMachineDefinion.State;
import ch.bbv.fsm.impl.AbstractStateMachine;

public class RadioStateMachine extends
		AbstractStateMachine<RadioStateMachine, State, Event> {

	private StringBuilder log = new StringBuilder();
	private boolean maintenance = false;

	public RadioStateMachine(final StateMachine<State, Event> driver) {
		super(driver);
	}

	public RadioStateMachine() {
		super(null);
	}

	public boolean isUserMode() {
		return !maintenance;
	}

	public boolean isMaintenanceMode() {
		return maintenance;
	}

	public Void logOffEntry() {
		addOptionalDot();
		log.append("entryOff");
		return null;
	}

	public Void logOnEntry() {
		addOptionalDot();
		log.append("entryOn");
		return null;
	}

	public Void logFMEntry() {
		addOptionalDot();
		log.append("entryFM");
		return null;
	}

	public Void logTransitionFromOffToOn() {
		addOptionalDot();
		log.append("OffToOn");
		return null;
	}

	public Void logOffExit() {
		addOptionalDot();
		log.append("exitOff");
		return null;
	}

	public Void logTransitionFromOnToOff() {
		addOptionalDot();
		log.append("OnToOff");
		return null;
	}

	public Void logOnExit() {
		addOptionalDot();
		log.append("exitOn");
		return null;
	}

	public Void logFMExit() {
		addOptionalDot();
		log.append("exitFM");
		return null;
	}

	public Void logTransitionFromFMToAM() {
		addOptionalDot();
		log.append("FMtoAM");
		return null;
	}

	public Void logTransitionFromAMToFM() {
		addOptionalDot();
		log.append("AMtoFM");
		return null;
	}

	public Void logAMEntry() {
		addOptionalDot();
		log.append("entryAM");
		return null;
	}

	public Void logAMExit() {
		addOptionalDot();
		log.append("exitAM");
		return null;
	}

	public Void logTransitionOffToMaintenance() {
		addOptionalDot();
		log.append("OffToMaintenance");
		return null;
	}

	public Void logTransitionFromMaintenanceToOff() {
		addOptionalDot();
		log.append("MaintenanceToOff");
		return null;
	}

	public Void logMaintenanceEntry() {
		addOptionalDot();
		log.append("entryMaintenance");
		return null;
	}

	public Void logMaintenanceExit() {
		addOptionalDot();
		log.append("exitMaintenance");
		return null;
	}

	public Void logTransitionFromPlayToAutoTune() {
		addOptionalDot();
		log.append("PlayToAutoTune");
		return null;
	}

	public Void logPlayEntry() {
		addOptionalDot();
		log.append("entryPlay");
		return null;
	}

	public Void logPlayExit() {
		addOptionalDot();
		log.append("exitPlay");
		return null;
	}

	public Void logTransitionFromAutoTuneToPlay() {
		addOptionalDot();
		log.append("AutoTuneToPlay");
		return null;
	}

	public Void logAutoTuneEntry() {
		addOptionalDot();
		log.append("entryAutoTune");
		return null;
	}

	public Void logAutoTuneExit() {
		addOptionalDot();
		log.append("exitAutoTune");
		return null;
	}

	public String consumeLog() {
		final String result = log.toString();
		log = new StringBuilder();
		return result;
	}

	private void addOptionalDot() {
		if (log.length() > 0) {
			log.append('.');
		}
	}

	public void setMaintenance(final boolean maintenance) {
		this.maintenance = maintenance;
	}
}
