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

// / <summary>
// / Tests state machine initialization and state switching.
// / </summary>

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ch.bbv.fsm.Action;
import ch.bbv.fsm.HistoryType;
import ch.bbv.fsm.impl.StatesAndEvents.Events;
import ch.bbv.fsm.impl.StatesAndEvents.States;
import ch.bbv.fsm.impl.internal.StateMachineImpl;

import com.google.common.collect.Lists;

public class StateMachineTest {
    // / <summary>
    // / Record of a state entry.
    // / </summary>
    private class EntryRecord extends Record {
        // / <summary>
        // / Gets the message.
        // / </summary>
        // / <value>The message.</value>
        @Override
        public String getMessage() {
            return "State " + this.state + " not entered.";
        }
    }

    // / <summary>
    // / Record of a state exit.
    // / </summary>
    private class ExitRecord extends Record {
        // / <summary>
        // / Gets the message.
        // / </summary>
        // / <value>The message.</value>
        @Override
        public String getMessage() {
            return "State " + this.state + " not exited.";
        }
    }

    // / <summary>
    // / A record of something that happened.
    // / </summary>
    private abstract class Record {
        States state;

        // / <summary>
        // / Gets the message.
        // / </summary>
        // / <value>The message.</value>
        public abstract String getMessage();

        // / <summary>
        // / Gets or sets the state.
        // / </summary>
        // / <value>The state.</value>
        public States getState() {
            return this.state;
        }

        public void setState(final States state) {
            this.state = state;
        }
    }

    private class RecordEntryAction implements Action {

        private final States state;

        private RecordEntryAction(final States state) {
            this.state = state;
        }

        @Override
        public void execute(final Object... arguments) {
            final EntryRecord record = new EntryRecord();
            record.setState(this.state);
            StateMachineTest.this.records.add(record);

        }

    }

    private class RecordExitAction implements Action {

        private final States state;

        private RecordExitAction(final States state) {
            this.state = state;
        }

        @Override
        public void execute(final Object... arguments) {
            final ExitRecord record = new ExitRecord();
            record.setState(this.state);
            StateMachineTest.this.records.add(record);

        }

    }

    private static final Object NEWLINE = System.getProperty("line.separator");

    // / <summary>
    // / Object under test.
    // / </summary>
    private StateMachineImpl<States, Events> testee;

    // / <summary>
    // / The list of recorded actions.
    // / </summary>
    private List<Record> records;

    // / <summary>
    // / Checks that no remaining records are present.
    // / </summary>
    private void CheckNoRemainingRecords() {
        if (this.records.size() == 0) {
            return;
        }

        final StringBuilder sb = new StringBuilder("there are additional records:");
        for (final Record record : this.records) {
            sb.append(NEWLINE);
            sb.append(record.getClass().getName() + "-" + record.getState());
        }

        Assert.fail(sb.toString());
    }

    // / <summary>
    // / Checks that the first record in the list of records is of type
    // <typeparamref name="T"/> and involves the specified state.
    // / </summary>
    // / <typeparam name="T">Type of the record.</typeparam>
    // / <param name="state">The state.</param>
    private <T> void CheckRecords(final States state, final Class<?> c) {
        final Record record = this.records.get(0);

        Assert.assertNotNull(String.format("expected record missing: {0} on {1}.", c.getName(), state), record);
        Assert.assertTrue(record.getMessage(), record.getClass() == c);

        this.records.remove(0);
    }

    // / <summary>
    // / Clears the records.
    // / </summary>
    private void ClearRecords() {
        this.records.clear();
    }

    // / <summary>
    // / When a transition between two states at the top level then the
    // / exit action of the source state is executed, then the actionis
    // performed
    // / and the entry action of the target state is executed.
    // / Finally, the current state is the target state.
    // / </summary>
    @Test
    public void ExecuteTransition() {
        this.testee.initialize(States.E);

        this.ClearRecords();

        this.testee.fire(Events.A);

        Assert.assertEquals(States.A, this.testee.getCurrentStateId());

        this.CheckRecords(States.E, ExitRecord.class);
        this.CheckRecords(States.A, EntryRecord.class);
        this.CheckNoRemainingRecords();
    }

    // / <summary>
    // / When a transition between two states in different super states on
    // different levels is executed
    // / then all states from the source up to the common super-state are exited
    // and all states down to
    // / the target state are entered. In this case the target state is lower
    // than the source state.
    // / </summary>
    @Test
    public void ExecuteTransitionBetweenStatesOnDifferentLevelsDownwards() {
        this.testee.initialize(States.B2);

        this.ClearRecords();

        this.testee.fire(Events.C1b);

        Assert.assertEquals(States.C1b, this.testee.getCurrentStateId());

        this.CheckRecords(States.B2, ExitRecord.class);
        this.CheckRecords(States.B, ExitRecord.class);
        this.CheckRecords(States.C, EntryRecord.class);
        this.CheckRecords(States.C1, EntryRecord.class);
        this.CheckRecords(States.C1b, EntryRecord.class);
        this.CheckNoRemainingRecords();
    }

    // / <summary>
    // / When a transition between two states in different super states on
    // different levels is executed
    // / then all states from the source up to the common super-state are exited
    // and all states down to
    // / the target state are entered. In this case the target state is higher
    // than the source state.
    // / </summary>
    @Test
    public void ExecuteTransitionBetweenStatesOnDifferentLevelsUpwards() {
        this.testee.initialize(States.D1b);

        this.ClearRecords();

        this.testee.fire(Events.B1);

        Assert.assertEquals(States.B1, this.testee.getCurrentStateId());

        this.CheckRecords(States.D1b, ExitRecord.class);
        this.CheckRecords(States.D1, ExitRecord.class);
        this.CheckRecords(States.D, ExitRecord.class);
        this.CheckRecords(States.B, EntryRecord.class);
        this.CheckRecords(States.B1, EntryRecord.class);
        this.CheckNoRemainingRecords();
    }

    // / <summary>
    // / When a transition between two states with the same super state is
    // executed then
    // / the exit action of source state, the transition action and the entry
    // action of
    // / the target state are executed.
    // / </summary>
    @Test
    public void ExecuteTransitionBetweenStatesWithSameSuperState() {
        this.testee.initialize(States.B1);

        this.ClearRecords();

        this.testee.fire(Events.B2);

        Assert.assertEquals(States.B2, this.testee.getCurrentStateId());

        this.CheckRecords(States.B1, ExitRecord.class);
        this.CheckRecords(States.B2, EntryRecord.class);
        this.CheckNoRemainingRecords();
    }

    // / <summary>
    // / The state hierarchy is recursively walked up until a state can handle
    // the event.
    // / </summary>
    @Test
    public void ExecuteTransitionHandledBySuperState() {
        this.testee.initialize(States.C1b);

        this.ClearRecords();

        this.testee.fire(Events.A);

        Assert.assertEquals(States.A, this.testee.getCurrentStateId());

        this.CheckRecords(States.C1b, ExitRecord.class);
        this.CheckRecords(States.C1, ExitRecord.class);
        this.CheckRecords(States.C, ExitRecord.class);
        this.CheckRecords(States.A, EntryRecord.class);
        this.CheckNoRemainingRecords();
    }

    // / <summary>
    // / When a transition targets a super-state with <see
    // cref="HistoryType.Deep"/> then the last
    // / active sub-state is entered recursively down to the most nested state.
    // / </summary>
    @Test
    public void ExecuteTransitionWithHistoryTypeDeep() {
        this.testee.initialize(States.D1b);
        this.testee.fire(Events.A);

        this.ClearRecords();

        this.testee.fire(Events.D);

        Assert.assertEquals(States.D1b, this.testee.getCurrentStateId());

        this.CheckRecords(States.A, ExitRecord.class);
        this.CheckRecords(States.D, EntryRecord.class);
        this.CheckRecords(States.D1, EntryRecord.class);
        this.CheckRecords(States.D1b, EntryRecord.class);
        this.CheckNoRemainingRecords();
    }

    // / <summary>
    // / When a transition targets a super-state with <see
    // cref="HistoryType.None"/> then the initial
    // / sub-state is entered whatever sub.state was last active.
    // / </summary>
    @Test
    public void ExecuteTransitionWithHistoryTypeNone() {
        this.testee.initialize(States.B2);
        this.testee.fire(Events.A);

        this.ClearRecords();

        this.testee.fire(Events.B);

        this.CheckRecords(States.A, ExitRecord.class);
        this.CheckRecords(States.B, EntryRecord.class);
        this.CheckRecords(States.B1, EntryRecord.class);
        this.CheckNoRemainingRecords();
    }

    // / <summary>
    // / When a transition targets a super-state with <see
    // cref="HistoryType.Shallow"/> then the last
    // / active sub-state is entered and the initial-state of the entered
    // sub-state is entered (no recursive history).
    // / </summary>
    @Test
    public void ExecuteTransitionWithHistoryTypeShallow() {
        this.testee.initialize(States.C1b);
        this.testee.fire(Events.A);

        this.ClearRecords();

        this.testee.fire(Events.C);

        Assert.assertEquals(States.C1a, this.testee.getCurrentStateId());

        this.CheckRecords(States.A, ExitRecord.class);
        this.CheckRecords(States.C, EntryRecord.class);
        this.CheckRecords(States.C1, EntryRecord.class);
        this.CheckRecords(States.C1a, EntryRecord.class);
        this.CheckNoRemainingRecords();
    }

    // / <summary>
    // / When a transition targets a super-state then the initial-state of this
    // super-state is entered recursively
    // / down to the most nested state. No history here!
    // / </summary>
    @Test
    public void ExecuteTransitionWithInitialSubState() {
        this.testee.initialize(States.A);

        this.ClearRecords();

        this.testee.fire(Events.B);

        Assert.assertEquals(States.B1, this.testee.getCurrentStateId());

        this.CheckRecords(States.A, ExitRecord.class);
        this.CheckRecords(States.B, EntryRecord.class);
        this.CheckRecords(States.B1, EntryRecord.class);
        this.CheckNoRemainingRecords();
    }

    // / <summary>
    // / When the state machine is initializes to a state with sub-states then
    // the hierarchy is recursively
    // / traversed to the most nested state along the chain of initial states.
    // / </summary>
    @Test
    public void InitializeStateWithSubStates() {
        this.testee.initialize(States.D);

        Assert.assertEquals(States.D1a, this.testee.getCurrentStateId());

        this.CheckRecords(States.D, EntryRecord.class);
        this.CheckRecords(States.D1, EntryRecord.class);
        this.CheckRecords(States.D1a, EntryRecord.class);
        this.CheckNoRemainingRecords();
    }

    // / <summary>
    // / After initialization the state machine is in the initial state and the
    // initial state is entered.
    // / All states up in the hierarchy of the initial state are entered, too.
    // / </summary>
    @Test
    public void InitializeToNestedState() {
        this.testee.initialize(States.D1b);

        Assert.assertEquals(States.D1b, this.testee.getCurrentStateId());

        this.CheckRecords(States.D, EntryRecord.class);
        this.CheckRecords(States.D1, EntryRecord.class);
        this.CheckRecords(States.D1b, EntryRecord.class);
        this.CheckNoRemainingRecords();
    }

    // / <summary>
    // / After initialization the state machine is in the initial state and the
    // initial state is entered.
    // / </summary>
    @Test
    public void InitializeToTopLevelState() {
        this.testee.initialize(States.A);

        Assert.assertEquals(States.A, this.testee.getCurrentStateId());

        this.CheckRecords(States.A, EntryRecord.class);
        this.CheckNoRemainingRecords();
    }

    // / <summary>
    // / Internal transitions do not trigger any exit or entry actions.
    // / </summary>
    @Test
    public void InternalTransition() {
        this.testee.initialize(States.A);

        this.ClearRecords();

        this.testee.fire(Events.A);

        Assert.assertEquals(States.A, this.testee.getCurrentStateId());
    }

    // / <summary>
    // / Initializes a test.
    // / </summary>
    @Before
    public void SetUp() {
        this.records = Lists.newArrayList();

        this.testee = new StateMachineImpl<States, Events>();

        this.testee.defineHierarchyOn(States.B, States.B1, HistoryType.NONE, States.B1, States.B2);
        this.testee.defineHierarchyOn(States.C, States.C2, HistoryType.SHALLOW, States.C1, States.C2);
        this.testee.defineHierarchyOn(States.C1, States.C1a, HistoryType.SHALLOW, States.C1a, States.C1b);
        this.testee.defineHierarchyOn(States.D, States.D1, HistoryType.DEEP, States.D1, States.D2);
        this.testee.defineHierarchyOn(States.D1, States.D1a, HistoryType.DEEP, States.D1a, States.D1b);

        this.testee.in(States.A).executeOnEntry(new RecordEntryAction(States.A))
                .executeOnExit(new RecordExitAction(States.A)).on(Events.B).goTo(States.B).on(Events.C).goTo(States.C)
                .on(Events.D).goTo(States.D).on(Events.A);

        this.testee.in(States.B).executeOnEntry(new RecordEntryAction(States.B))
                .executeOnExit(new RecordExitAction(States.B));

        this.testee.in(States.B1).executeOnEntry(new RecordEntryAction(States.B1))
                .executeOnExit(new RecordExitAction(States.B1)).on(Events.B2).goTo(States.B2);

        this.testee.in(States.B2).executeOnEntry(new RecordEntryAction(States.B2))
                .executeOnExit(new RecordExitAction(States.B2)).on(Events.A).goTo(States.A).on(Events.C1b)
                .goTo(States.C1b);

        this.testee.in(States.C).executeOnEntry(new RecordEntryAction(States.C))
                .executeOnExit(new RecordExitAction(States.C)).on(Events.A).goTo(States.A);

        this.testee.in(States.C1).executeOnEntry(new RecordEntryAction(States.C1))
                .executeOnExit(new RecordExitAction(States.C1));

        this.testee.in(States.C2).executeOnEntry(new RecordEntryAction(States.C2))
                .executeOnExit(new RecordExitAction(States.C2));

        this.testee.in(States.C1a).executeOnEntry(new RecordEntryAction(States.C1a))
                .executeOnExit(new RecordExitAction(States.C1a));

        this.testee.in(States.C1b).executeOnEntry(new RecordEntryAction(States.C1b))
                .executeOnExit(new RecordExitAction(States.C1b)).on(Events.A).goTo(States.A);

        this.testee.in(States.D).executeOnEntry(new RecordEntryAction(States.D))
                .executeOnExit(new RecordExitAction(States.D));

        this.testee.in(States.D1).executeOnEntry(new RecordEntryAction(States.D1))
                .executeOnExit(new RecordExitAction(States.D1));

        this.testee.in(States.D1a).executeOnEntry(new RecordEntryAction(States.D1a))
                .executeOnExit(new RecordExitAction(States.D1a));

        this.testee.in(States.D1b).executeOnEntry(new RecordEntryAction(States.D1b))
                .executeOnExit(new RecordExitAction(States.D1b)).on(Events.A).goTo(States.A).on(Events.B1)
                .goTo(States.B1);

        this.testee.in(States.E).executeOnEntry(new RecordEntryAction(States.E))
                .executeOnExit(new RecordExitAction(States.E)).on(Events.A).goTo(States.A);
    }

    // / <summary>
    // / Tears down a test.
    // / </summary>
    @After
    public void TearDown() {
    }
}
