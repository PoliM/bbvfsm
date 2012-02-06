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

import ch.bbv.fsm.acceptance.action.definition.SimpleStateMachineDefinition.Event;
import ch.bbv.fsm.acceptance.action.definition.SimpleStateMachineDefinition.State;
import ch.bbv.fsm.action.Action;

public class ActionDefinition implements Action<SimpleStateMachine, State, Event> {

	@Override
	public void execute(final SimpleStateMachine stateMachine, final Object... arguments) {

		stateMachine.addCallingAction(this);
		stateMachine.log("execute: ActionDefinition.execute()");
	}

	/*
	 * CLASS
	 */
	public static class ActionDefinitionInnerStatic implements Action<SimpleStateMachine, State, Event> {

		@Override
		public void execute(final SimpleStateMachine stateMachine, final Object... arguments) {

			stateMachine.log("execute: ActionDefinition$ActionDefinitionInnerStatic.execute()");
		}

		/*
		 * CLASS
		 */
		public static class ActionDefinitionInnerStaticSecondLevel implements
				Action<SimpleStateMachine, State, Event> {

			@Override
			public void execute(final SimpleStateMachine stateMachine, final Object... arguments) {

				stateMachine
						.log("execute: ActionDefinition$ActionDefinitionInner$ActionDefinitionInnerSecondLevel.execute()");
			}
		}

	}

	/*
	 * CLASS
	 */
	public class ActionDefinitionInner implements Action<SimpleStateMachine, State, Event> {

		@Override
		public void execute(final SimpleStateMachine stateMachine, final Object... arguments) {

			stateMachine.log("execute: ActionDefinition$ActionDefinitionInner.execute()");
		}

	}

	public final class ActionDefinitionInnerWithoutDefaultConstructor implements
			Action<SimpleStateMachine, State, Event> {

		@Override
		public void execute(final SimpleStateMachine stateMachine, final Object... arguments) {

		}

		private ActionDefinitionInnerWithoutDefaultConstructor() {
		}
	}

	/*
	 * CLASS
	 */
	public static class ActionDefinitionEntry implements Action<SimpleStateMachine, State, Event> {

		@Override
		public void execute(final SimpleStateMachine stateMachine, final Object... arguments) {

			stateMachine.addCallingAction(this);

			stateMachine.log("execute: ActionDefinition$ActionDefinitionEntry.execute()");
		}
	}

	public static class ActionDefinitionEntryWithParameter implements Action<SimpleStateMachine, State, Event> {

		@Override
		public void execute(final SimpleStateMachine stateMachine, final Object... arguments) {

			stateMachine.log("execute: ActionDefinition$ActionDefinitionEntryWithParameter.execute(" + arguments[0]
					+ ")");
		}
	}

}
