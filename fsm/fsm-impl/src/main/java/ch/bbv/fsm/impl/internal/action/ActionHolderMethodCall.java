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
package ch.bbv.fsm.impl.internal.action;

import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.impl.internal.aop.MethodCall;
import ch.bbv.fsm.impl.internal.statemachine.state.StateContext;

/**
 * Wraps a MethodCall instance as an ActionHolder.
 * 
 * @param <TStateMachine>
 *            the type of state machine
 * @param <TState>
 *            the type of the states
 * @param <TEvent>
 *            the type of the events
 * @author Ueli Kurmann (bbv Software Services AG) (bbv Software Services AG)
 */
public class ActionHolderMethodCall<TStateMachine extends StateMachine<TState, TEvent>, TState extends Enum<?>, TEvent extends Enum<?>>
		implements ActionHolder<TStateMachine, TState, TEvent> {

	private final MethodCall<TStateMachine> methodCall;

	/**
	 * Creates a new instance.
	 * 
	 * @param methodCall
	 *            the method call instance.
	 */
	public ActionHolderMethodCall(final MethodCall<TStateMachine> methodCall) {
		this.methodCall = methodCall;
	}

	@Override
	public void execute(final StateContext<TStateMachine, TState, TEvent> stateContext) {
		this.methodCall.execute(stateContext.getStateMachine(), null);
	}
}
