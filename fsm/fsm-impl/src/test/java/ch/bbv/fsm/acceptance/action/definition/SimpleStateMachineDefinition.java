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

package ch.bbv.fsm.acceptance.action.definition;

import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.impl.AbstractStateMachineDefinition;

public class SimpleStateMachineDefinition
		extends
		AbstractStateMachineDefinition<SimpleStateMachine, SimpleStateMachineDefinition.State, SimpleStateMachineDefinition.Event> {

	enum State {
		state_init, state_entry
	}

	public enum Event {
		move
	}

	public SimpleStateMachineDefinition() {
		super(State.state_init);
	}

	@Override
	protected SimpleStateMachine createStateMachine(final StateMachine<State, Event> driver) {
		return new SimpleStateMachine(driver);
	}

}
