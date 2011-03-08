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

import static ch.bbv.fsm.impl.Tool.*;

import org.junit.Before;
import org.junit.Test;

import ch.bbv.fsm.HistoryType;
import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.impl.PassiveStateMachine;
import ch.bbv.fsm.impl.StatesAndEvents.Events;
import ch.bbv.fsm.impl.StatesAndEvents.States;

/**
 * Tests the report functionality of the state machine.
 * 
 * @author Ueli Kurmann (bbv Software Services AG) (bbv Software Services AG)
 * 
 */
public class ReportTest {
    private StateMachine<States, Events> testee;

    public Void foo() {
        return null;
    }

    public boolean onlyIfCheck(final boolean check) {
        return check;
    }

    @Test
    public void Report() {
        this.testee.defineHierarchyOn(States.B, States.B1, HistoryType.NONE, States.B1, States.B2);
        this.testee.defineHierarchyOn(States.C, States.C1, HistoryType.SHALLOW, States.C1, States.C2);
        this.testee.defineHierarchyOn(States.C1, States.C1a, HistoryType.SHALLOW, States.C1a, States.C1b);
        this.testee.defineHierarchyOn(States.D, States.D1, HistoryType.DEEP, States.D1, States.D2);
        this.testee.defineHierarchyOn(States.D1, States.D1a, HistoryType.DEEP, States.D1a, States.D1b);

        this.testee.in(States.A).executeOnEntry(from(this).foo()).executeOnExit(from(this).foo()).on(Events.A)
                .on(Events.B).goTo(States.B).on(Events.C).goTo(States.C1).onlyIf(from(this).onlyIfCheck(true))
                .on(Events.C).goTo(States.C2).onlyIf(from(this).onlyIfCheck(false));

        this.testee.in(States.B).on(Events.A).goTo(States.A);

        this.testee.in(States.B1).on(Events.B2).goTo(States.B1);

        this.testee.in(States.B2).on(Events.B1).goTo(States.B2);

        this.testee.initialize(States.A);

        final String report = this.testee.report();

        System.out.println(report);
    }

    /**
     * Initialization.
     */
    @Before
    public void SetUp() {
        this.testee = new PassiveStateMachine<States, Events>("Test Machine");
    }
}