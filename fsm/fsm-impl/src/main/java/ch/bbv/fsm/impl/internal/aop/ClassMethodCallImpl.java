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

/**
 * Implementation of a Method Call.
 * 
 * @param <TObject>
 *            the type of the object to call
 * 
 * @author Ueli Kurmann (bbv Software Services AG) (bbv Software Services AG)
 */
public class ClassMethodCallImpl<TObject> implements MethodCall<TObject> {

	private final Object[] args;

	private final Method method;

	/**
	 * Creates a new instance.
	 * 
	 * @param method
	 *            the method.
	 * @param args
	 *            the arguments of the method.
	 */
	public ClassMethodCallImpl(final Method method, final Object[] args) {
		this.method = method;
		this.args = args;
	}

	@Override
	public Object execute(final TObject objectToCall, final Object[] args) {
		try {
			return this.method.invoke(objectToCall, args != null ? args : this.args);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Object[] getArguments() {
		return this.args;
	}

	@Override
	public String toString() {
		return method.toGenericString();
	}
}
