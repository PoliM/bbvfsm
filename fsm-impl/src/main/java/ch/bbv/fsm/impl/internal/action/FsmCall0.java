package ch.bbv.fsm.impl.internal.action;

import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.action.FsmAction0;


public class FsmCall0<TStateMachine extends StateMachine<TState, TEvent>, TState extends Enum<?>, TEvent extends Enum<?>>
    extends FsmCall<TStateMachine, TState, TEvent> {
  private final FsmAction0<TStateMachine, TState, TEvent> action;

  public FsmCall0(final FsmAction0<TStateMachine, TState, TEvent> action) {
    this.action = action;
  }

  @Override
  public void execOn(final TStateMachine fsm) {
    action.exec(fsm);
  }

  @Override
  public void execOn(final TStateMachine fsm, final Object... args) {
    action.exec(fsm);
  }
}
