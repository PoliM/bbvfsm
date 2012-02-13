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
import ch.bbv.fsm.guard.Function;

/**
 * executes a Function. The Function is defined as a
 * {@literal Class<? extends Function<TStateMachine, TState, TEvent, ParameterType, ReturnType>>}
 * 
 * @param <TStateMachine>
 *            the type of state machine
 * @param <TState>
 *            the type of the states
 * @param <TEvent>
 *            the type of the events
 * @param <TParameterType>
 *            parameter type
 * @param <TReturnType>
 *            return type
 */
public class FunctionClassCallFunction<TStateMachine extends StateMachine<TState, TEvent>, TState extends Enum<?>, TEvent extends Enum<?>, TParameterType, TReturnType>
		implements
		Function<TStateMachine, TState, TEvent, TParameterType, TReturnType> {

	private final Class<? extends Function<TStateMachine, TState, TEvent, TParameterType, TReturnType>> functionClass;

	/**
	 * Constructor.
	 * 
	 * @param functionClass
	 *            the class implementing the Function to be called.
	 */
	public FunctionClassCallFunction(
			final Class<? extends Function<TStateMachine, TState, TEvent, TParameterType, TReturnType>> functionClass) {
		this.functionClass = functionClass;
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
			throw new RuntimeException(e);
		}
	}

	@Override
	public TReturnType execute(final TStateMachine stateMachine,
			final TParameterType parameter) {
		Function<TStateMachine, TState, TEvent, TParameterType, TReturnType> instance = createClassInstance(this.functionClass);
		return instance.execute(stateMachine, parameter);
	}
}
