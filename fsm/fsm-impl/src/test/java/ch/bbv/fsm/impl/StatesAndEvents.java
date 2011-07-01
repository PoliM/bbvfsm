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

public class StatesAndEvents {

    // / <summary>
    // / The events used in the test state machines.
    // / </summary>
    public enum Events {
        // / <summary>Test event A.</summary>
        A,

        // / <summary>Test event B.</summary>
        B,

        // / <summary>Test event B1.</summary>
        B1,

        // / <summary>Test event B2.</summary>
        B2,

        // / <summary>Test event C.</summary>
        C,

        // / <summary>Test event C1b.</summary>
        C1b,

        // / <summary>Test event D.</summary>
        D
    }

    // / <summary>
    // / The states used in the test state machines
    // / </summary>
    public enum States {
        // / <summary>Test state A.</summary>
        A,

        // / <summary>Test state B.</summary>
        B,

        // / <summary>Test state B1.</summary>
        B1,

        // / <summary>Test state B2.</summary>
        B2,

        // / <summary>Test state C.</summary>
        C,

        // / <summary>Test state C1.</summary>
        C1,

        // / <summary>Test state C1a.</summary>
        C1a,

        // / <summary>Test state C1b.</summary>
        C1b,

        // / <summary>Test state C2.</summary>
        C2,

        // / <summary>Test state D.</summary>
        D,

        // / <summary>Test state D1.</summary>
        D1,

        // / <summary>Test state D1a.</summary>
        D1a,

        // / <summary>Test state D1b.</summary>
        D1b,

        // / <summary>Test state D2.</summary>
        D2,

        // / <summary>Test state E.</summary>
        E,
    }
}
