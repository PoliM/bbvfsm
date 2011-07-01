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
import org.junit.Test;

import ch.bbv.fsm.Action;
import ch.bbv.fsm.impl.StatesAndEvents.Events;
import ch.bbv.fsm.impl.StatesAndEvents.States;
import ch.bbv.fsm.impl.internal.StateMachineImpl;

public class StateActionTest {
    // / <summary>
    // / Entry actions are executed when a state is entered.
    // / </summary>
    @Test
    public void EntryAction() {
        final StateMachineImpl<States, Events> testee = new StateMachineImpl<States, Events>();

        final boolean[] entered = new boolean[1];

        final Action action = new Action() {

            @Override
            public void execute(final Object... arguments) {
                entered[0] = true;
            }

        };

        testee.in(States.A).executeOnEntry(action);

        testee.initialize(States.A);

        Assert.assertTrue(entered[0]);
    }

    // / <summary>
    // / Exit actions are executed when a state is left.
    // / </summary>
    @Test
    public void ExitAction() {
        final StateMachineImpl<States, Events> testee = new StateMachineImpl<States, Events>();

        final boolean[] entered = new boolean[1];

        final Action action = new Action() {

            @Override
            public void execute(final Object... arguments) {
                entered[0] = true;
            }

        };

        testee.in(States.A).executeOnExit(action).on(Events.B).goTo(States.B);

        testee.initialize(States.A);
        testee.fire(Events.B);

        Assert.assertTrue(entered[0]);
    }

    // / <summary>
    // / Entry actions can be parametrized.
    // / </summary>
    @Test
    public void ParameterizedEntryAction() {
        final StateMachineImpl<States, Events> testee = new StateMachineImpl<States, Events>();

        final int[] argument = new int[1];
        argument[0] = 0;

        final Action action = new Action() {

            @Override
            public void execute(final Object... arguments) {
                argument[0] = (Integer) arguments[0];
            }

        };

        testee.in(States.A).executeOnEntry(action, 3);

        testee.initialize(States.A);

        Assert.assertEquals(3, argument[0]);
    }

    // / <summary>
    // / Exit actions can be parametrized.
    // / </summary>
    @Test
    public void ParametrizedExitAction() {
        final StateMachineImpl<States, Events> testee = new StateMachineImpl<States, Events>();

        final int[] argument = new int[1];
        argument[0] = 0;

        final Action action = new Action() {

            @Override
            public void execute(final Object... arguments) {
                argument[0] = (Integer) arguments[0];
            }

        };

        testee.in(States.A).executeOnExit(action, 3).on(Events.B).goTo(States.B);

        testee.initialize(States.A);
        testee.fire(Events.B);

        Assert.assertEquals(3, argument[0]);
    }
}
