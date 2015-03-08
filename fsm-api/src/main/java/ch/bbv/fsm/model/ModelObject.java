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

import java.util.List;

import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.model.visitor.Visitable;

/**
 * @author Mario Martinez (bbv Software Services AG)
 * 
 * @param <TStateMachine>
 *            the type of state machine
 * @param <TState>
 *            the type of the states
 * @param <TEvent>
 *            the type of the events
 */
public interface ModelObject<TStateMachine extends StateMachine<TState, TEvent>, TState extends Enum<?>, TEvent extends Enum<?>>
		extends Visitable<TStateMachine, TState, TEvent> {

	/**
	 * @return True if there is a super-state associated.
	 */
	boolean hasParent();

	/**
	 * Returns the parent ModelObject. This means, the super-state.
	 * 
	 * @return the super-state.
	 */
	State<TStateMachine, TState, TEvent> getParent();

	/**
	 * @return True if there are sub-states associated.
	 */
	boolean hasChildren();

	/**
	 * Returns a list containing the children ModelObjects. This means, a list of the substates for this state.
	 * 
	 * @return the list of sub-states.
	 */
	List<State<TStateMachine, TState, TEvent>> getChildren();

}
