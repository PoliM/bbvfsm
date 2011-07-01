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

import ch.bbv.fsm.Function;
import ch.bbv.fsm.events.StateMachineEventAdapter;
import ch.bbv.fsm.events.TransitionEventArgs;
import ch.bbv.fsm.impl.StatesAndEvents.Events;
import ch.bbv.fsm.impl.StatesAndEvents.States;
import ch.bbv.fsm.impl.internal.StateMachineImpl;

public class GuardTest {
    private class Handler extends StateMachineEventAdapter<States, Events> {

        @Override
        public void onTransitionDeclined(final TransitionEventArgs<States, Events> arg) {
            GuardTest.this.transitionDeclined = true;
        }
    }

    // / <summary>
    // / Object under test.
    // / </summary>
    private StateMachineImpl<States, Events> testee;

    boolean transitionDeclined = false;

    // / <summary>
    // / When all guards deny the execution of its transition then ???
    // / </summary>
    @Test
    public void AllGuardsReturnFalse() {

        final Function<Object[], Boolean> f1 = new Function<Object[], Boolean>() {

            @Override
            public Boolean execute(final Object[] parameter) {
                return false;
            }

        };

        this.testee.addEventHandler(new Handler());

        this.testee.in(States.A).on(Events.A).goTo(States.B).onlyIf(f1).on(Events.A).goTo(States.C).onlyIf(f1);

        this.testee.initialize(States.A);

        this.testee.fire(Events.A);

        Assert.assertEquals(States.A, this.testee.getCurrentStateId());
        Assert.assertTrue(this.transitionDeclined);
    }

    // / <summary>
    // / The event arguments are passed to the guard.
    // / </summary>
    @Test
    public void EventArgumentsArePassedToTheGuard() {
        final Object[] originalEventArguments = new Object[] { 1, 2, "test" };

        final Object[][] eventArguments = new Object[1][];

        final Function<Object[], Boolean> f2 = new Function<Object[], Boolean>() {

            @Override
            public Boolean execute(final Object[] parameter) {
                eventArguments[0] = parameter;
                return true;
            }

        };

        this.testee.in(States.A).on(Events.A).goTo(States.B).onlyIf(f2);

        this.testee.initialize(States.A);

        this.testee.fire(Events.A, originalEventArguments);

        Assert.assertSame(originalEventArguments, eventArguments[0]);
    }

    // / <summary>
    // / Initializes a test.
    // / </summary>
    @Before
    public void SetUp() {
        this.testee = new StateMachineImpl<States, Events>();
    }

    // / <summary>
    // / Only the transition with a guard returning true is executed and the
    // event arguments are passed to the guard.
    // / </summary>
    @Test
    public void TransitionWithGuardReturningTrueIsExecuted() {
        final Function<Object[], Boolean> f1 = new Function<Object[], Boolean>() {

            @Override
            public Boolean execute(final Object[] parameter) {
                return false;
            }

        };

        final Function<Object[], Boolean> f2 = new Function<Object[], Boolean>() {

            @Override
            public Boolean execute(final Object[] parameter) {
                return true;
            }

        };

        this.testee.in(States.A).on(Events.A).goTo(States.B).onlyIf(f1).on(Events.A).goTo(States.C).onlyIf(f2);

        this.testee.initialize(States.A);

        this.testee.fire(Events.A);

        Assert.assertEquals(States.C, this.testee.getCurrentStateId());
    }
}
