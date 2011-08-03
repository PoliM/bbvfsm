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
package ch.bbv.fsm.impl.internal.aop;

import java.lang.reflect.Method;
import java.util.Stack;

import ch.bbv.fsm.dsl.MethodCall;

/**
 * Implementation of a Method Call.
 * 
 * @author Ueli Kurmann (bbv Software Services AG) (bbv Software Services AG)
 */
public class MethodCallImpl implements MethodCall {

	private final Object[] args;

	private final Method method;

	private final Object owner;

	private static ThreadLocal<Stack<MethodCall>> methodCalls = new ThreadLocal<Stack<MethodCall>>() {
		@Override
		protected Stack<MethodCall> initialValue() {
			return new Stack<MethodCall>();
		}
	};

	/**
	 * Pops a method call from the stack.
	 * 
	 * @return the method call on the top of the stack.
	 */
	public static MethodCall pop() {
		return methodCalls.get().pop();
	}

	/**
	 * Pushes a method call instance to the stack.
	 * 
	 * @param methodCall
	 *            the method call.
	 */
	public static void push(final MethodCall methodCall) {
		methodCalls.get().push(methodCall);
	}
	
	public static void reset(){
		methodCalls.get().clear();
	}

	/**
	 * Creates a new instance.
	 * 
	 * @param owner
	 *            the object owner.
	 * @param method
	 *            the method.
	 * @param args
	 *            the arguments of the method.
	 */
	public MethodCallImpl(final Object owner, final Method method,
			final Object[] args) {
		this.owner = owner;
		this.method = method;
		this.args = args;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.bbv.asm.dsl.MethodCall#execute()
	 */
	@Override
	public void execute() {
		try {
			this.method.invoke(this.owner, this.args);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.bbv.asm.dsl.MethodCall#getArguments()
	 */
	@Override
	public Object[] getArguments() {
		return this.args;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.bbv.asm.dsl.MethodCall#getMethod()
	 */
	@Override
	public Method getMethod() {
		return this.method;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ch.bbv.asm.dsl.MethodCall#getOwner()
	 */
	@Override
	public Object getOwner() {
		return this.owner;
	}
}
