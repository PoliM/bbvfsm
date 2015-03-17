package ch.bbv.fsm.impl.internal.action;

import ch.bbv.fsm.action.FsmAction2;


public class FsmCall2<FSM, P1, P2> extends FsmCall<FSM> {
	private FsmAction2<FSM, P1, P2> action;
	private P1 p1;
	private P2 p2;

	public FsmCall2(FsmAction2<FSM, P1, P2> action, P1 p1, P2 p2) {
		this.action = action;
		this.p1 = p1;
		this.p2 = p2;
	}

	@Override
	public void execOn(FSM fsm) {
		action.exec(fsm, p1, p2);
	}
}