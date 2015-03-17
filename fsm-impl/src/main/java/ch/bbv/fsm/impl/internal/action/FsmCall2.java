package ch.bbv.fsm.impl.internal.action;

import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.action.FsmAction2;


/**
 * {@link FsmCall} implementation with two parameters.
 *
 * @param <TStateMachine> the state machine.
 * @param <TState> the type of the states.
 * @param <TEvent> the type of the events.
 * @param <P1> the type of the first parameter.
 * @param <P2> the type of the second parameter.
 */
public class FsmCall2<TStateMachine extends StateMachine<TState, TEvent>, TState extends Enum<?>, TEvent extends Enum<?>, P1, P2>
    implements FsmCall<TStateMachine, TState, TEvent> {
  private final FsmAction2<TStateMachine, TState, TEvent, P1, P2> action;
  private final P1 p1;
  private final P2 p2;

  /**
   * Constructor.
   * @param action the action.
   * @param p1 the first parameter.
   * @param p2 the second parameter.
   */
  public FsmCall2(final FsmAction2<TStateMachine, TState, TEvent, P1, P2> action, final P1 p1,
      final P2 p2) {
    this.action = action;
    this.p1 = p1;
    this.p2 = p2;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void execOn(final TStateMachine fsm) {
    action.exec(fsm, p1, p2);
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("unchecked")
  @Override
  public void execOn(final TStateMachine fsm, final Object... args) {
    action.exec(fsm, (P1) args[0], (P2) args[1]);
  }
}
