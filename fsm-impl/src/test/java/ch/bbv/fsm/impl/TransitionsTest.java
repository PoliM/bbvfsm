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
import ch.bbv.fsm.events.StateMachineEventAdapter;
import ch.bbv.fsm.events.TransitionEventArgs;
import ch.bbv.fsm.impl.StatesAndEvents.Events;
import ch.bbv.fsm.impl.StatesAndEvents.States;
import ch.bbv.fsm.impl.internal.StateMachineImpl;

public class TransitionsTest {
    private class Handler extends StateMachineEventAdapter<States, Events> {

        @Override
        public void onTransitionDeclined(final TransitionEventArgs<States, Events> arg) {
            TransitionsTest.this.declined = true;

        }
    }

    // / <summary>
    // / Object under test.
    // / </summary>
    private StateMachineImpl<States, Events> testee;
    Object[] action1Arguments = null;
    Object[] action2Arguments = null;
    Boolean executed = false;

    // / <summary>
    // / Initializes a test.
    // / </summary>

    boolean declined = false;

    // / <summary>
    // / Actions on transitions are performed and the event arguments are passed
    // to them.
    // / </summary>
    @Test
    public void ExecuteActions() {

        final Action action1 = new Action() {

            @Override
            public void execute(final Object... arguments) {
                TransitionsTest.this.action1Arguments = arguments;

            }
        };

        final Action action2 = new Action() {

            @Override
            public void execute(final Object... arguments) {
                TransitionsTest.this.action2Arguments = arguments;

            }
        };

        this.testee.in(States.A).on(Events.B).goTo(States.B).execute(action1, action2);

        this.testee.initialize(States.A);

        final Object[] eventArguments = new Object[] { 1, 2, 3, "test" };
        this.testee.fire(Events.B, eventArguments);

        Assert.assertArrayEquals(eventArguments, this.action1Arguments);
        Assert.assertArrayEquals(eventArguments, this.action2Arguments);
    }

    // / <summary>
    // / Internal transitions can be executed (internal transition = transition
    // that remains in the same state and does not execute exit and entry
    // actions.
    // / </summary>
    @Test
    public void InternalTransition() {

        final Action action2 = new Action() {

            @Override
            public void execute(final Object... arguments) {
                TransitionsTest.this.executed = true;

            }
        };

        this.testee.in(States.A).on(Events.A).execute(action2);
        this.testee.initialize(States.A);
        this.testee.fire(Events.A);

        Assert.assertTrue(this.executed);
        Assert.assertEquals(States.A, this.testee.getCurrentStateId());
    }

    // / <summary>
    // / When no transition for the fired event can be found in the entire
    // / hierarchy up from the current state then
    // / </summary>
    @Test
    public void MissingTransition() {
        this.testee.in(States.A).on(Events.B).goTo(States.B);

        this.testee.addEventHandler(new Handler());

        this.testee.initialize(States.A);

        this.testee.fire(Events.C);

        Assert.assertTrue(this.declined);
        Assert.assertEquals(States.A, this.testee.getCurrentStateId());
    }

    @Before
    public void setUp() {
        this.testee = new StateMachineImpl<States, Events>();
    }
}
