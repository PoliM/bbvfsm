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
package ch.bbv.fsm.impl.internal;

import static ch.bbv.fsm.impl.Tool.*;
import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.impl.PassiveStateMachine;

/**
 * Sample showing usage of state machine.
 */
public class SyntaxTest {

    public enum Events {
        toA, toB, toC, toD
    }

    public enum States {
        A, B, C, D
    }

    private static final String EXIT_A = "ExitA";

    private static final String ENTRY_A = "EntryA";;

    private static final String ENTRY_B = "EntryB";

    private StateMachine<States, Events> testee;
    private String fooEntryValue;
    private String fooExitValue;
    private boolean barValue;

    public boolean bar(final Boolean value) {
        this.barValue = value;
        return true;
    }

    public Void fooEntry(final String text) {
        this.fooEntryValue = text;
        return null;
    }

    public Void fooExit(final String text) {
        this.fooExitValue = text;
        return null;
    }

    @Before
    public void setup() {
        this.testee = new PassiveStateMachine<States, Events>("SimpleExample");

        this.testee.in(States.A).executeOnEntry(from(this).fooEntry(ENTRY_A)).executeOnExit(from(this).fooExit(EXIT_A))
                .on(Events.toB).goTo(States.B).onlyIf(from(this).bar(any(Boolean.class)));

        this.testee.in(States.B).executeOnEntry(from(this).fooEntry(ENTRY_B)).on(Events.toB).goTo(States.B)
                .onlyIf(from(this).bar(any(Boolean.class)));

        this.testee.in(States.B).on(Events.toD).goTo(States.D);
        this.testee.in(States.D).on(Events.toA).goTo(States.A);
    }

    @After
    public void shutdown() {
        this.testee.stop();
    }

    /**
     * Test Enter & Exit of state A.
     */
    @Test
    public void t1() {

        this.testee.initialize(States.A);
        this.testee.start();
        final String enterA = this.fooEntryValue;
        this.testee.fire(Events.toB, true);
        final boolean onlyIf = this.barValue;
        final String exitA = this.fooExitValue;
        final String enterB = this.fooEntryValue;

        Assert.assertTrue(onlyIf);
        Assert.assertEquals(ENTRY_A, enterA);
        Assert.assertEquals(EXIT_A, exitA);
        Assert.assertEquals(ENTRY_B, enterB);
    }

}
