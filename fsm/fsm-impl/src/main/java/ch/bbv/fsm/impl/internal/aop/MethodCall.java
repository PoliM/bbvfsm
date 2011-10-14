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

/**
 * MethodCall Interface.
 * 
 * @param <TObject>
 *            the type of the object to call
 * 
 * @author Ueli Kurmann (bbv Software Services AG) (bbv Software Services AG)
 */
public interface MethodCall<TObject> {

	/**
	 * Executes the method in the owner with the arguments. A RuntimeException is thrown if an error occurs.
	 * 
	 * @param objectToCall
	 *            the state machine that triggers this call
	 * @param arguments
	 *            the arguments used for the call
	 */
	Object execute(TObject objectToCall, Object[] arguments);

	/**
	 * Returns an array of arguments of the method.
	 * 
	 * @return an array of arguments.
	 */
	Object[] getArguments();
}
