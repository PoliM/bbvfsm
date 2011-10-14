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
package ch.bbv.fsm.impl.internal.statemachine.state;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

import ch.bbv.fsm.StateMachine;

import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;

/**
 * The mapping between state id's and the corresponding state instance.
 * 
 * @author Ueli Kurmann (bbv Software Services AG) (bbv Software Services AG)
 * 
 * @param <TStateMachine>
 *            the type of state machine
 * @param <TState>
 *            the type of the states
 * @param <TEvent>
 *            the type of the events
 */
public class StateDictionary<TStateMachine extends StateMachine<TState, TEvent>, TState extends Enum<?>, TEvent extends Enum<?>> {

	private final ConcurrentMap<TState, State<TStateMachine, TState, TEvent>> dictionary;

	/**
	 * Creates a new instance of the state dictionary.
	 */
	public StateDictionary() {
		this.dictionary = new MapMaker().makeMap();
	}

	/**
	 * Returns the state instance by it's id.
	 * 
	 * @param stateId
	 *            the state id.
	 * @return the state instance.
	 */
	public State<TStateMachine, TState, TEvent> getState(final TState stateId) {
		if (!this.dictionary.containsKey(stateId)) {
			this.dictionary.putIfAbsent(stateId, new StateImpl<TStateMachine, TState, TEvent>(stateId));
		}

		return this.dictionary.get(stateId);
	}

	/**
	 * Returns a list of all defined states.
	 * 
	 * @return a list of all defined states.
	 */
	public List<State<TStateMachine, TState, TEvent>> getStates() {
		return Lists.newArrayList(this.dictionary.values());
	}

}
