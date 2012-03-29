package ch.bbv.fsm.memento;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds the state machine's internal state.
 * 
 * @author lukasweibel
 * 
 * @param <TState>
 * @param <TEvent>
 */
public class StateMachineMemento<TState extends Enum<?>, TEvent extends Enum<?>> {

	private TState currentState;

	private Map<TState, TState> savedHistoryStates = new HashMap<TState, TState>();

	/**
	 * Returns the state machine's current state.
	 */
	public TState getCurrentState() {
		return currentState;
	}

	/**
	 * Sets state machine's current state.
	 * 
	 * @param currentState
	 *            the current state to set
	 */
	public void setCurrentState(final TState currentState) {
		this.currentState = currentState;
	}

	/**
	 * Returns the stored history states.
	 */
	public Map<TState, TState> getSavedHistoryStates() {
		return savedHistoryStates;
	}

	/**
	 * Sets the stored history states.
	 * 
	 * @param savedHistoryStates
	 *            the history states
	 */
	public void setSavedHistoryStates(
			final Map<TState, TState> savedHistoryStates) {
		this.savedHistoryStates = savedHistoryStates;
	}

	/**
	 * Adds a history entry to the memento.
	 * 
	 * @param parent
	 *            the parent
	 * @param child
	 *            the last used child
	 */
	public void putHistoryState(final TState parent, final TState child) {
		savedHistoryStates.put(parent, child);
	}
}
