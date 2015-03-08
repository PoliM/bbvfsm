package ch.bbv.fsm.acceptance.radio;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Map;

import org.junit.Test;

import ch.bbv.fsm.acceptance.radio.RadioStateMachineDefinion.Event;
import ch.bbv.fsm.acceptance.radio.RadioStateMachineDefinion.State;
import ch.bbv.fsm.memento.StateMachineMemento;

import com.google.common.collect.Maps;

public class RadioPassivateAndActivateAcceptanceTest {

	public class RadioStateMachineMemento
			extends
			StateMachineMemento<RadioStateMachineDefinion.State, RadioStateMachineDefinion.Event> {

	}

	@Test
	public void radioWhenUseHistoryThenHistoryMustBeWrittenToMemento() {
		final RadioStateMachineDefinion radioStateMachineDefinion = new RadioStateMachineDefinion();

		final RadioStateMachine radioStateMachine = radioStateMachineDefinion
				.createPassiveStateMachine("radioWhenSimplingTurnOnAndOffThenPlayFM");

		radioStateMachine.start();

		radioStateMachine.fire(Event.TogglePower);
		radioStateMachine.fire(Event.ToggleMode);
		radioStateMachine.fire(Event.TogglePower);

		final StateMachineMemento<State, Event> memento = new RadioStateMachineMemento();
		radioStateMachine.passivate(memento);

		final Map<State, State> expectedHistory = Maps.newHashMap();
		expectedHistory.put(State.On, State.AM);
		assertThat(memento.getCurrentState(), is(equalTo(State.Off)));
		assertThat(memento.getSavedHistoryStates(),
				is(equalTo(expectedHistory)));
	}

	@Test
	public void radioWhenActiveUsingMemementoThenMustBeRestored() {
		final RadioStateMachineDefinion radioStateMachineDefinion = new RadioStateMachineDefinion();

		final RadioStateMachine radioStateMachine = radioStateMachineDefinion
				.createPassiveStateMachine("radioWhenSimplingTurnOnAndOffThenPlayFM");

		final StateMachineMemento<State, Event> memento = new RadioStateMachineMemento();
		memento.setCurrentState(State.Off);
		memento.putHistoryState(State.On, State.FM);

		radioStateMachine.activate(memento);

		radioStateMachine.fire(Event.TogglePower);
		radioStateMachine.fire(Event.TogglePower);

		radioStateMachine.terminate();

		assertThat(
				radioStateMachine.consumeLog(),
				is(equalTo("exitOff.OffToOn.entryOn.entryFM.exitFM.exitOn.OnToOff.entryOff.exitOff")));
	}

}
