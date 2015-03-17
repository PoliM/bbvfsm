package ch.bbv.fsm.impl.internal.action;

abstract class FsmCall<FSM> {
	public abstract void execOn(FSM fsm);
}