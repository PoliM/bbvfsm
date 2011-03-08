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

import org.junit.Assert;
import org.junit.Test;

import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.impl.PassiveStateMachine;
import ch.bbv.fsm.impl.StatesAndEvents.Events;
import ch.bbv.fsm.impl.StatesAndEvents.States;

public class NewSyntaxTest {

    String testString;
    int testNumber;

    public Void actionMethod(final String a, final Integer b) {
        this.testString = a;
        this.testNumber = b;
        return null;
    }

    @Test
    public void transitionTest() {
        final StateMachine<States, Events> sm = new PassiveStateMachine<States, Events>();

        sm.in(States.A).on(Events.B).goTo(States.B).execute(from(this).actionMethod(any(String.class), any(int.class)));

        sm.initialize(States.A);
        sm.start();

        sm.fire(Events.B, "testString", 77);

        Assert.assertEquals("testString", this.testString);
        Assert.assertEquals(77, this.testNumber);
    }

}
