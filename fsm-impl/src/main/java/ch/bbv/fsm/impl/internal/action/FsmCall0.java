package ch.bbv.fsm.impl.internal.action;

import ch.bbv.fsm.action.FsmAction0;


public class FsmCall0<FSM> extends FsmCall<FSM> {
	private FsmAction0<FSM> action;

	public FsmCall0(FsmAction0<FSM> action) {
		this.action = action;
	}

	@Override
	public void execOn(FSM fsm) {
		action.exec(fsm);
	}
}