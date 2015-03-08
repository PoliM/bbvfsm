package ch.bbv.fsm.impl.transfer.testfsms;

import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.impl.AbstractStateMachine;

public class SimpleFsm extends AbstractStateMachine<SimpleFsm, States, Events> {

	protected SimpleFsm(StateMachine<States, Events> driver) {
		super(driver);
	}

	public Void turnOn() {
		return null;
	}

	public Void turnOff() {
		return null;
	}

	public boolean notRecentlyUsed() {
		return false;
	}
}
