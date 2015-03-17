package ch.bbv.fsm.impl.internal.action;

import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.action.FsmAction1;


public class FsmCall1<TStateMachine extends StateMachine<TState, TEvent>, TState extends Enum<?>, TEvent extends Enum<?>, P1> extends
    FsmCall<TStateMachine, TState, TEvent> {
  private final FsmAction1<TStateMachine, TState, TEvent, P1> action;
  private final P1 p1;

  public FsmCall1(final FsmAction1<TStateMachine, TState, TEvent, P1> action, final P1 p1) {
    this.action = action;
    this.p1 = p1;
  }

  @Override
  public void execOn(final TStateMachine fsm) {
    action.exec(fsm, p1);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void execOn(final TStateMachine fsm, final Object... args) {
    action.exec(fsm, (P1) args[0]);
  }
}
