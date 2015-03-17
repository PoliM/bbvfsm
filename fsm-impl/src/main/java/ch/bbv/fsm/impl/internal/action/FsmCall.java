package ch.bbv.fsm.impl.internal.action;

import ch.bbv.fsm.StateMachine;

public abstract class FsmCall<TStateMachine extends StateMachine<TState, TEvent>, TState extends Enum<?>, TEvent extends Enum<?>> {
  public abstract void execOn(TStateMachine fsm);

  public abstract void execOn(TStateMachine fsm, Object... args);
}
