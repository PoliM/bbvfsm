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
import ch.bbv.fsm.action.Action;
import ch.bbv.fsm.action.EmbeddedAction;

/**
 * Calls a method.
 * 
 * @param <TStateMachine>
 *            the type of state machine
 * @param <TState>
 *            the type of the states
 * @param <TEvent>
 *            the type of the events
 */
public class EmbeddedCallAction<TStateMachine extends StateMachine<TState, TEvent>, TState extends Enum<?>, TEvent extends Enum<?>>
		implements Action<TStateMachine, TState, TEvent> {

	private final Class<? extends EmbeddedAction<TStateMachine, TState, TEvent>> methodCall;

	/**
	 * Constructor.
	 * 
	 * @param methodCall
	 *            the method to call
	 */
	public EmbeddedCallAction(final Class<? extends EmbeddedAction<TStateMachine, TState, TEvent>> methodCall) {
		this.methodCall = methodCall;
	}

	@Override
	public void execute(final TStateMachine stateMachine, final Object... arguments) {
		EmbeddedAction<TStateMachine, TState, TEvent> instance = createClassInstance(this.methodCall);
		instance.execute(stateMachine, arguments);
	}

	/**
	 * Instances a class.
	 * 
	 * @param clazz
	 * @return
	 * @throws RuntimeException
	 */
	private <T> T createClassInstance(final Class<T> clazz) {
		try {
			return clazz.getDeclaredConstructor(new Class[0]).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
