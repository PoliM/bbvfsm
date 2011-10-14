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

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ch.bbv.fsm.HistoryType;
import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.action.Action;
import ch.bbv.fsm.impl.StatesAndEvents.Events;
import ch.bbv.fsm.impl.StatesAndEvents.States;

import com.google.common.collect.Lists;

/**
 * Tests state machine initialization and state switching.
 */
public class StateMachineTest {
	/**
	 * Record of a state entry.
	 */
	private class EntryRecord extends Record {
		/**
		 * Gets the message.
		 */
		@Override
		public String getMessage() {
			return "State " + this.getState() + " not entered.";
		}
	}

	/**
	 * Record of a state exit.
	 */
	private class ExitRecord extends Record {
		/**
		 * Gets the message.
		 */
		@Override
		public String getMessage() {
			return "State " + this.getState() + " not exited.";
		}
	}

	/**
	 * A record of something that happened.
	 */
	private abstract class Record {
		private States state;

		/**
		 * Gets the message.
		 */
		public abstract String getMessage();

		/**
		 * Gets or sets the state.
		 */
		public States getState() {
			return this.state;
		}

		public void setState(final States state) {
			this.state = state;
		}
	}

	private final class RecordEntryAction implements Action<SimpleStateMachine<States, Events>, States, Events> {

		private final States state;

		private RecordEntryAction(final States state) {
			this.state = state;
		}

		@Override
		public void execute(final SimpleStateMachine<States, Events> stateMachine, final Object... arguments) {
			final EntryRecord record = new EntryRecord();
			record.setState(this.state);
			StateMachineTest.this.records.add(record);

		}

	}

	private final class RecordExitAction implements Action<SimpleStateMachine<States, Events>, States, Events> {

		private final States state;

		private RecordExitAction(final States state) {
			this.state = state;
		}

		@Override
		public void execute(final SimpleStateMachine<States, Events> stateMachine, final Object... arguments) {
			final ExitRecord record = new ExitRecord();
			record.setState(this.state);
			StateMachineTest.this.records.add(record);

		}

	}

	private static final Object NEWLINE = System.getProperty("line.separator");

	/**
	 * The list of recorded actions.
	 */
	private List<Record> records;

	private SimpleStateMachineDefinition<States, Events> stateMachineDefinition;

	/**
	 * Checks that no remaining records are present.
	 */
	private void checkNoRemainingRecords() {
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

	/**
	 * Checks that the first record in the list of records is of type T and involves the specified state.
	 */
	private <T> void checkRecords(final States state, final Class<?> c) {
		final Record record = this.records.get(0);

		Assert.assertNotNull(String.format("expected record missing: {0} on {1}.", c.getName(), state), record);
		Assert.assertTrue(record.getMessage(), record.getClass() == c);

		this.records.remove(0);
	}

	/**
	 * Clears the records.
	 */
	private void clearRecords() {
		this.records.clear();
	}

	/**
	 * When a transition between two states at the top level then the exit action of the source state is executed, then the action is
	 * performed and the entry action of the target state is executed. Finally, the current state is the target state.
	 */
	@Test
	public void executeTransition() {
		final StateMachine<States, Events> testee = stateMachineDefinition.createPassiveStateMachine(
				"executeTransitionBetweenStatesOnDifferentLevelsDownwards", States.E);
		testee.start();

		this.clearRecords();

		testee.fire(Events.A);

		Assert.assertEquals(States.A, testee.getCurrentState());

		this.checkRecords(States.E, ExitRecord.class);
		this.checkRecords(States.A, EntryRecord.class);
		this.checkNoRemainingRecords();
	}

	/**
	 * When a transition between two states in different super states on different levels is executed then all states from the source up to
	 * the common super-state are exited and all states down to the target state are entered. In this case the target state is lower than
	 * the source state.
	 */
	@Test
	public void executeTransitionBetweenStatesOnDifferentLevelsDownwards() {
		final StateMachine<States, Events> testee = stateMachineDefinition.createPassiveStateMachine(
				"executeTransitionBetweenStatesOnDifferentLevelsDownwards", States.B2);
		testee.start();

		this.clearRecords();

		testee.fire(Events.C1b);

		Assert.assertEquals(States.C1b, testee.getCurrentState());

		this.checkRecords(States.B2, ExitRecord.class);
		this.checkRecords(States.B, ExitRecord.class);
		this.checkRecords(States.C, EntryRecord.class);
		this.checkRecords(States.C1, EntryRecord.class);
		this.checkRecords(States.C1b, EntryRecord.class);
		this.checkNoRemainingRecords();
	}

	/**
	 * When a transition between two states in different super states on different levels is executed then all states from the source up to
	 * the common super-state are exited and all states down to the target state are entered. In this case the target state is higher than
	 * the source state.
	 */
	@Test
	public void executeTransitionBetweenStatesOnDifferentLevelsUpwards() {
		final StateMachine<States, Events> testee = stateMachineDefinition.createPassiveStateMachine(
				"executeTransitionBetweenStatesOnDifferentLevelsDownwards", States.D1b);
		testee.start();

		this.clearRecords();

		testee.fire(Events.B1);

		Assert.assertEquals(States.B1, testee.getCurrentState());

		this.checkRecords(States.D1b, ExitRecord.class);
		this.checkRecords(States.D1, ExitRecord.class);
		this.checkRecords(States.D, ExitRecord.class);
		this.checkRecords(States.B, EntryRecord.class);
		this.checkRecords(States.B1, EntryRecord.class);
		this.checkNoRemainingRecords();
	}

	/**
	 * When a transition between two states with the same super state is executed then the exit action of source state, the transition
	 * action and the entry action of the target state are executed.
	 */
	@Test
	public void executeTransitionBetweenStatesWithSameSuperState() {
		final StateMachine<States, Events> testee = stateMachineDefinition.createPassiveStateMachine(
				"executeTransitionBetweenStatesOnDifferentLevelsDownwards", States.B1);
		testee.start();

		this.clearRecords();

		testee.fire(Events.B2);

		Assert.assertEquals(States.B2, testee.getCurrentState());

		this.checkRecords(States.B1, ExitRecord.class);
		this.checkRecords(States.B2, EntryRecord.class);
		this.checkNoRemainingRecords();
	}

	/**
	 * The state hierarchy is recursively walked up until a state can handle the event.
	 */
	@Test
	public void executeTransitionHandledBySuperState() {
		final StateMachine<States, Events> testee = stateMachineDefinition.createPassiveStateMachine(
				"executeTransitionBetweenStatesOnDifferentLevelsDownwards", States.C1b);
		testee.start();

		this.clearRecords();

		testee.fire(Events.A);

		Assert.assertEquals(States.A, testee.getCurrentState());

		this.checkRecords(States.C1b, ExitRecord.class);
		this.checkRecords(States.C1, ExitRecord.class);
		this.checkRecords(States.C, ExitRecord.class);
		this.checkRecords(States.A, EntryRecord.class);
		this.checkNoRemainingRecords();
	}

	/**
	 * When a transition targets a super-state with {@link HistoryType#DEEP} then the last active sub-state is entered recursively down to
	 * the most nested state.
	 */
	@Test
	public void executeTransitionWithHistoryTypeDeep() {
		final StateMachine<States, Events> testee = stateMachineDefinition.createPassiveStateMachine(
				"executeTransitionBetweenStatesOnDifferentLevelsDownwards", States.D1b);
		testee.start();
		testee.fire(Events.A);

		this.clearRecords();

		testee.fire(Events.D);

		Assert.assertEquals(States.D1b, testee.getCurrentState());

		this.checkRecords(States.A, ExitRecord.class);
		this.checkRecords(States.D, EntryRecord.class);
		this.checkRecords(States.D1, EntryRecord.class);
		this.checkRecords(States.D1b, EntryRecord.class);
		this.checkNoRemainingRecords();
	}

	/**
	 * When a transition targets a super-state with {@link HistoryType#NONE} then the initial sub-state is entered whatever sub.state was
	 * last active.
	 */
	@Test
	public void executeTransitionWithHistoryTypeNone() {
		final StateMachine<States, Events> testee = stateMachineDefinition.createPassiveStateMachine(
				"executeTransitionBetweenStatesOnDifferentLevelsDownwards", States.B2);
		testee.start();
		testee.fire(Events.A);

		this.clearRecords();

		testee.fire(Events.B);

		this.checkRecords(States.A, ExitRecord.class);
		this.checkRecords(States.B, EntryRecord.class);
		this.checkRecords(States.B1, EntryRecord.class);
		this.checkNoRemainingRecords();
	}

	/**
	 * When a transition targets a super-state with {@link HistoryType#SHALLOW} then the last active sub-state is entered and the
	 * initial-state of the entered sub-state is entered (no recursive history).
	 */
	@Test
	public void executeTransitionWithHistoryTypeShallow() {
		final StateMachine<States, Events> testee = stateMachineDefinition.createPassiveStateMachine(
				"executeTransitionBetweenStatesOnDifferentLevelsDownwards", States.C1b);
		testee.start();
		testee.fire(Events.A);

		this.clearRecords();

		testee.fire(Events.C);

		Assert.assertEquals(States.C1a, testee.getCurrentState());

		this.checkRecords(States.A, ExitRecord.class);
		this.checkRecords(States.C, EntryRecord.class);
		this.checkRecords(States.C1, EntryRecord.class);
		this.checkRecords(States.C1a, EntryRecord.class);
		this.checkNoRemainingRecords();
	}

	/**
	 * When a transition targets a super-state then the initial-state of this super-state is entered recursively down to the most nested
	 * state. No history here!
	 */
	@Test
	public void executeTransitionWithInitialSubState() {
		final StateMachine<States, Events> testee = stateMachineDefinition.createPassiveStateMachine(
				"executeTransitionBetweenStatesOnDifferentLevelsDownwards", States.A);
		testee.start();

		this.clearRecords();

		testee.fire(Events.B);

		Assert.assertEquals(States.B1, testee.getCurrentState());

		this.checkRecords(States.A, ExitRecord.class);
		this.checkRecords(States.B, EntryRecord.class);
		this.checkRecords(States.B1, EntryRecord.class);
		this.checkNoRemainingRecords();
	}

	/**
	 * When the state machine is initializes to a state with sub-states then the hierarchy is recursively traversed to the most nested state
	 * along the chain of initial states.
	 */
	@Test
	public void initializeStateWithSubStates() {
		final StateMachine<States, Events> testee = stateMachineDefinition.createPassiveStateMachine(
				"executeTransitionBetweenStatesOnDifferentLevelsDownwards", States.D);
		testee.start();

		Assert.assertEquals(States.D1a, testee.getCurrentState());

		this.checkRecords(States.D, EntryRecord.class);
		this.checkRecords(States.D1, EntryRecord.class);
		this.checkRecords(States.D1a, EntryRecord.class);
		this.checkNoRemainingRecords();
	}

	/**
	 * After initialization the state machine is in the initial state and the initial state is entered. All states up in the hierarchy of
	 * the initial state are entered, too.
	 */
	@Test
	public void initializeToNestedState() {
		final StateMachine<States, Events> testee = stateMachineDefinition.createPassiveStateMachine(
				"executeTransitionBetweenStatesOnDifferentLevelsDownwards", States.D1b);
		testee.start();

		Assert.assertEquals(States.D1b, testee.getCurrentState());

		this.checkRecords(States.D, EntryRecord.class);
		this.checkRecords(States.D1, EntryRecord.class);
		this.checkRecords(States.D1b, EntryRecord.class);
		this.checkNoRemainingRecords();
	}

	/**
	 * After initialization the state machine is in the initial state and the initial state is entered.
	 */
	@Test
	public void initializeToTopLevelState() {
		final StateMachine<States, Events> testee = stateMachineDefinition.createPassiveStateMachine(
				"executeTransitionBetweenStatesOnDifferentLevelsDownwards", States.A);
		testee.start();

		Assert.assertEquals(States.A, testee.getCurrentState());

		this.checkRecords(States.A, EntryRecord.class);
		this.checkNoRemainingRecords();
	}

	/**
	 * Internal transitions do not trigger any exit or entry actions.
	 */
	@Test
	public void internalTransition() {
		final StateMachine<States, Events> testee = stateMachineDefinition.createPassiveStateMachine(
				"executeTransitionBetweenStatesOnDifferentLevelsDownwards", States.A);
		testee.start();

		this.clearRecords();

		testee.fire(Events.A);

		Assert.assertEquals(States.A, testee.getCurrentState());
	}

	/**
	 * Initializes a test.
	 */
	@Before
	public void setUp() {
		this.records = Lists.newArrayList();

		stateMachineDefinition = new SimpleStateMachineDefinition<States, Events>("StateMachineTest", States.A);

		stateMachineDefinition.defineHierarchyOn(States.B, States.B1, HistoryType.NONE, States.B1, States.B2);
		stateMachineDefinition.defineHierarchyOn(States.C, States.C2, HistoryType.SHALLOW, States.C1, States.C2);
		stateMachineDefinition.defineHierarchyOn(States.C1, States.C1a, HistoryType.SHALLOW, States.C1a, States.C1b);
		stateMachineDefinition.defineHierarchyOn(States.D, States.D1, HistoryType.DEEP, States.D1, States.D2);
		stateMachineDefinition.defineHierarchyOn(States.D1, States.D1a, HistoryType.DEEP, States.D1a, States.D1b);

		stateMachineDefinition.in(States.A).executeOnEntry(new RecordEntryAction(States.A)).executeOnExit(new RecordExitAction(States.A))
				.on(Events.B).goTo(States.B).on(Events.C).goTo(States.C).on(Events.D).goTo(States.D).on(Events.A);

		stateMachineDefinition.in(States.B).executeOnEntry(new RecordEntryAction(States.B)).executeOnExit(new RecordExitAction(States.B));

		stateMachineDefinition.in(States.B1).executeOnEntry(new RecordEntryAction(States.B1))
				.executeOnExit(new RecordExitAction(States.B1)).on(Events.B2).goTo(States.B2);

		stateMachineDefinition.in(States.B2).executeOnEntry(new RecordEntryAction(States.B2))
				.executeOnExit(new RecordExitAction(States.B2)).on(Events.A).goTo(States.A).on(Events.C1b).goTo(States.C1b);

		stateMachineDefinition.in(States.C).executeOnEntry(new RecordEntryAction(States.C)).executeOnExit(new RecordExitAction(States.C))
				.on(Events.A).goTo(States.A);

		stateMachineDefinition.in(States.C1).executeOnEntry(new RecordEntryAction(States.C1))
				.executeOnExit(new RecordExitAction(States.C1));

		stateMachineDefinition.in(States.C2).executeOnEntry(new RecordEntryAction(States.C2))
				.executeOnExit(new RecordExitAction(States.C2));

		stateMachineDefinition.in(States.C1a).executeOnEntry(new RecordEntryAction(States.C1a))
				.executeOnExit(new RecordExitAction(States.C1a));

		stateMachineDefinition.in(States.C1b).executeOnEntry(new RecordEntryAction(States.C1b))
				.executeOnExit(new RecordExitAction(States.C1b)).on(Events.A).goTo(States.A);

		stateMachineDefinition.in(States.D).executeOnEntry(new RecordEntryAction(States.D)).executeOnExit(new RecordExitAction(States.D));

		stateMachineDefinition.in(States.D1).executeOnEntry(new RecordEntryAction(States.D1))
				.executeOnExit(new RecordExitAction(States.D1));

		stateMachineDefinition.in(States.D1a).executeOnEntry(new RecordEntryAction(States.D1a))
				.executeOnExit(new RecordExitAction(States.D1a));

		stateMachineDefinition.in(States.D1b).executeOnEntry(new RecordEntryAction(States.D1b))
				.executeOnExit(new RecordExitAction(States.D1b)).on(Events.A).goTo(States.A).on(Events.B1).goTo(States.B1);

		stateMachineDefinition.in(States.E).executeOnEntry(new RecordEntryAction(States.E)).executeOnExit(new RecordExitAction(States.E))
				.on(Events.A).goTo(States.A);

	}
}
