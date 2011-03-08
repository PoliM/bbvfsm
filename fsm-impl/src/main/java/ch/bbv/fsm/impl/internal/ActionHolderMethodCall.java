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
package ch.bbv.fsm.impl.internal;

import ch.bbv.fsm.dsl.MethodCall;

/**
 * Wraps a MethodCall instance as an ActionHolder.
 * 
 * @author Ueli Kurmann (bbv Software Services AG) (bbv Software Services AG)
 */
public class ActionHolderMethodCall implements ActionHolder {

    private final MethodCall methodCall;

    /**
     * Creates a new instance.
     * 
     * @param methodCall
     *            the method call instance.
     */
    public ActionHolderMethodCall(final MethodCall methodCall) {
        this.methodCall = methodCall;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.bbv.asm.impl.internal.ActionHolder#execute()
     */
    @Override
    public void execute() {
        this.methodCall.execute();

    }

}
