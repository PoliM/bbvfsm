package ch.bbv.fsm.impl.internal.action;

import ch.bbv.fsm.action.FsmAction1;


public class FsmCall1<FSM, P1> extends FsmCall<FSM> {
	private FsmAction1<FSM, P1> action;
	private P1 p1;

	public FsmCall1(FsmAction1<FSM, P1> action, P1 p1) {
		this.action = action;
		this.p1 = p1;
	}

	@Override
	public void execOn(FSM fsm) {
		action.exec(fsm, p1);
	}
}