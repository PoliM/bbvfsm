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

import org.junit.Before;
import org.junit.Test;

import ch.bbv.fsm.HistoryType;
import ch.bbv.fsm.impl.StatesAndEvents.Events;
import ch.bbv.fsm.impl.StatesAndEvents.States;
import ch.bbv.fsm.impl.internal.StateMachineImpl;

// / <summary>
// / Tests hierarchy building in the <see cref="StateMachine{TState,TEvent}"/>.
// / </summary>

public class StateMachineBuildHierarchyTest {
    // / <summary>
    // / Object under test.
    // / </summary>
    private StateMachineImpl<States, Events> testee;

    // / <summary>
    // / If a state is specified as the inital sub state that is not in the list
    // of sub states then an <see cref="ArgumentException"/> is thrown.
    // / </summary>
    @Test(expected = IllegalArgumentException.class)
    public void AddHierarchicalStatesInitialStateIsNotASubState() {
        this.testee.defineHierarchyOn(States.B, States.A, HistoryType.NONE, States.B1, States.B2);

    }

    // / <summary>
    // / If the super-state is specified as the initial state of its sub-states
    // then an <see cref="ArgumentException"/> is thrown.
    // / </summary>
    @Test(expected = IllegalArgumentException.class)
    public void AddHierarchicalStatesInitialStateIsSuperStateItself() {
        this.testee.defineHierarchyOn(States.B, States.B, HistoryType.NONE, States.B1, States.B2);
    }

    // / <summary>
    // / Initializes a test.
    // / </summary>
    @Before
    public void SetUp() {
        this.testee = new StateMachineImpl<States, Events>();
    }
}
