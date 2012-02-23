/*******************************************************************************
 *  Copyright 2010, 2011 bbv Software Services AG, Mario Martinez
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
 *     bbv Software Services AG (http://www.bbv.ch), Mario Martinez
 *******************************************************************************/
package ch.bbv.fsm.model;

import ch.bbv.fsm.StateMachine;

/**
 * Represents a FSM Model Object. This model is a hierarchical(tree) representation of the scopes contained in the FSM.
 * 
 * 
 * @author Mario Martinez (bbv Software Services AG)
 * @param <TState>
 *            the enumeration type of the states.
 * @param <TEvent>
 *            the enumeration type of the events.
 * @param <TStateMachine>
 *            the type of state machine
 * 
 */
public interface State<TStateMachine extends StateMachine<TState, TEvent>, TState extends Enum<?>, TEvent extends Enum<?>>
		extends ModelObject<TStateMachine, TState, TEvent> {

	/**
	 * Gets the id of this state.
	 * 
	 * @return the id of this state.
	 */
	TState getId();

	/**
	 * Returns the level in the hierarchy.
	 * 
	 * @return the level in the hierarchy.
	 */
	int getLevel();

	/**
	 * Sets the level in the hierarchy.
	 * 
	 * @param level
	 *            the level
	 */
	void setLevel(int level);

}