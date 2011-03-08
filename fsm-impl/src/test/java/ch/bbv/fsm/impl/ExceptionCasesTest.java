/*******************************************************************************
 *  Copyright 2010, 2011 bbv Software Services AG, Ueli Kurmann
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 * Contributors:
 *     bbv Software Services AG (http://www.bbv.ch), Ueli Kurmann
 *******************************************************************************/
package ch.bbv.fsm.impl;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ch.bbv.fsm.Action;
import ch.bbv.fsm.Function;
import ch.bbv.fsm.events.ExceptionEventArgs;
import ch.bbv.fsm.events.StateMachineEventAdapter;
import ch.bbv.fsm.events.TransitionEventArgs;
import ch.bbv.fsm.events.TransitionExceptionEventArgs;
import ch.bbv.fsm.impl.StatesAndEvents.Events;
import ch.bbv.fsm.impl.StatesAndEvents.States;
import ch.bbv.fsm.impl.internal.StateMachineImpl;

public class ExceptionCasesTest {
    private class Handler extends StateMachineEventAdapter<States, Events> {

        @Override
        public void onExceptionThrown(final ExceptionEventArgs<States, Events> eventArgs) {
            if (eventArgs != null) {
                ExceptionCasesTest.this.recordedException = eventArgs.getException();
            }

        }

        @Override
        public void onTransitionDeclined(final TransitionEventArgs<States, Events> arg) {
            ExceptionCasesTest.this.transitionDeclined = true;

        }

        @Override
        public void onTransitionThrowsException(final TransitionExceptionEventArgs<States, Events> eventArgs) {

            ExceptionCasesTest.this.recordedStateId = eventArgs.getStateId();
            ExceptionCasesTest.this.recordedEventId = eventArgs.getEventId();
            ExceptionCasesTest.this.recordedEventArguments = eventArgs.getEventArguments();
            ExceptionCasesTest.this.recordedException = eventArgs.getException();

        }

    }

    // / <summary>
    // / Object under test.
    // / </summary>
    private StateMachineImpl<States, Events> testee;

    // / <summary>
    // / the state that was provided in the <see
    // cref="StateMachine{TState,TEvent}.ExceptionThrown"/> event.
    // / </summary>
    private States recordedStateId;

    // / <summary>
    // / the event that was provided in the <see
    // cref="StateMachine{TState,TEvent}.ExceptionThrown"/> event.
    // / </summary>
    private Events recordedEventId;

    // / <summary>
    // / the event arguments that was provided in the <see
    // cref="StateMachine{TState,TEvent}.ExceptionThrown"/> event.
    // / </summary>
    private Object[] recordedEventArguments;

    // / <summary>
    // / the exception that was provided in the <see
    // cref="StateMachine{TState,TEvent}.ExceptionThrown"/> event.
    // / </summary>
    private Exception recordedException;

    public boolean transitionDeclined;

    // / <summary>
    // / Asserts that the correct exception was notified.
    // / </summary>
    // / <param name="expectedStateId">The expected state id.</param>
    // / <param name="expectedEventId">The expected event id.</param>
    // / <param name="expectedEventArguments">The expected event
    // arguments.</param>
    // / <param name="expectedException">The expected exception.</param>
    private void AssertException(final States expectedStateId, final Events expectedEventId,
            final Object[] expectedEventArguments, final Exception expectedException) {
        Assert.assertEquals(expectedStateId, this.recordedStateId);
        Assert.assertEquals(expectedEventId, this.recordedEventId);
        Assert.assertArrayEquals(expectedEventArguments, this.recordedEventArguments);
        Assert.assertEquals(expectedException, this.recordedException);
    }

    // / <summary>
    // / When a transition throws an exception then the exception is catched and
    // the <see cref="StateMachine{TState,TEvent}.ExceptionThrown"/> event is
    // fired.
    // / The transition is executed and the state machine is in the target
    // state.
    // / </summary>
    @Test
    public void ExceptionThrowingAction() {
        final Object[] eventArguments = new Object[] { 1, 2, "test" };
        final RuntimeException e = new RuntimeException();

        final Action throwException = new Action() {

            @Override
            public void execute(final Object... arguments) {
                throw e;
            }
        };

        this.testee.in(States.A).on(Events.B).goTo(States.B).execute(throwException);

        this.testee.initialize(States.A);
        this.testee.fire(Events.B, eventArguments);

        this.AssertException(States.A, Events.B, eventArguments, e);
        Assert.assertEquals(States.B, this.testee.getCurrentStateId());

    }

    // / <summary>
    // / When an exception is thrown in an entry action then it is notified and
    // the state is entered anyway.
    // / </summary>
    @Test
    public void ExceptionThrowingEntryAction() {
        final Object[] eventArguments = new Object[] { 1, 2, "test" };

        final RuntimeException e = new RuntimeException();

        final Action throwException = new Action() {

            @Override
            public void execute(final Object... arguments) {
                throw e;
            }
        };

        this.testee.in(States.A).on(Events.B).goTo(States.B);

        this.testee.in(States.B).executeOnEntry(throwException);

        this.testee.initialize(States.A);
        this.testee.fire(Events.B, eventArguments);

        Assert.assertEquals(e, this.recordedException);
        Assert.assertEquals(States.B, this.testee.getCurrentStateId());

    }

    // / <summary>
    // / When an exception is thrown in an entry action then it is notified and
    // the state is entered anyway.
    // / </summary>
    @Test
    public void ExceptionThrowingExitAction() {
        final Object[] eventArguments = new Object[] { 1, 2, "test" };
        final RuntimeException exception = new RuntimeException();

        final Action throwException = new Action() {

            @Override
            public void execute(final Object... arguments) {
                throw exception;
            }
        };

        this.testee.in(States.A).executeOnExit(throwException).on(Events.B).goTo(States.B);

        this.testee.initialize(States.A);
        this.testee.fire(Events.B, eventArguments);

        Assert.assertEquals(exception, this.recordedException);
        Assert.assertEquals(States.B, this.testee.getCurrentStateId());

    }

    // / <summary>
    // / When a guard throws an exception then it is catched and the <see
    // cref="StateMachine{TState,TEvent}.ExceptionThrown"/> event is fired.
    // / The transition is not executed and if there is no other transition then
    // the state machine remains in the same state.
    // / </summary>
    @Test
    public void ExceptionThrowingGuard() {
        final Object[] eventArguments = new Object[] { 1, 2, "test" };
        final RuntimeException e = new RuntimeException();

        final Function<Object[], Boolean> f1 = new Function<Object[], Boolean>() {

            @Override
            public Boolean execute(final Object[] parameter) {
                throw e;
            }
        };

        this.testee.in(States.A).on(Events.B).goTo(States.B).onlyIf(f1);

        this.testee.addEventHandler(new Handler());

        this.testee.initialize(States.A);
        this.testee.fire(Events.B, eventArguments);

        this.AssertException(States.A, Events.B, eventArguments, e);
        Assert.assertEquals(States.A, this.testee.getCurrentStateId());
        Assert.assertTrue(this.transitionDeclined);

    }

    // / <summary>
    // / The state machine has to be initialized before events can be fired.
    // / </summary>
    @Test(expected = IllegalStateException.class)
    public void NotInitialized() {
        this.testee.fire(Events.B);
    }

    // / <summary>
    // / Initializes a test.
    // / </summary>
    @Before
    public void SetUp() {

        this.testee = new StateMachineImpl<States, Events>();

        this.testee.addEventHandler(new Handler());
    }
}
