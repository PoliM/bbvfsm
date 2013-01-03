package ch.bbv.fsm.impl.transfer.testfsms;

// =============================
// Generated file. DO NOT TOUCH!
// =============================

import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.impl.AbstractStateMachineDefinition;

enum States {
	Initial, //
	Off, //
	On
}

enum Events {
	toggle
}

public class SimpleFsmDefinition extends AbstractStateMachineDefinition<SimpleFsm, States, Events> {

	public SimpleFsmDefinition() {
		super("SimpleFsmDefinition", States.Off);
		SimpleFsm proto = this.getPrototype();
		in(States.Off).on(Events.toggle).goTo(States.On) //
				.execute(proto.turnOn()) //
		;
		in(States.On).on(Events.toggle).goTo(States.Off) //
				.execute(proto.turnOff()) //
		;
	}

	@Override
	protected SimpleFsm createStateMachine(StateMachine<States, Events> driver) {
		return new SimpleFsm(driver);
	}
}
