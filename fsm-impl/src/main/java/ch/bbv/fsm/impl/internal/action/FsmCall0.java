package ch.bbv.fsm.impl.internal.action;

import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.action.FsmAction0;


/**
 * {@link FsmCall} implementation with zero parameters. 
 *
 * @param <TStateMachine> the type of the state machine.
 * @param <TState> the type of the states.
 * @param <TEvent> the type of the events.
 */
public class FsmCall0<TStateMachine extends StateMachine<TState, TEvent>, TState extends Enum<?>, TEvent extends Enum<?>>
    implements FsmCall<TStateMachine, TState, TEvent> {
  private final FsmAction0<TStateMachine, TState, TEvent> action;

  /**
   * Constructor.
   * @param action the action.
   */
  public FsmCall0(final FsmAction0<TStateMachine, TState, TEvent> action) {
    this.action = action;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void execOn(final TStateMachine fsm) {
    action.exec(fsm);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void execOn(final TStateMachine fsm, final Object... args) {
    action.exec(fsm);
  }
}
