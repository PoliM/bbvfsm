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

import ch.bbv.fsm.Function;
import ch.bbv.fsm.dsl.MethodCall;

/**
 * Implementation of a function that wraps a MethodCall.
 * 
 * @author Ueli Kurmann (bbv Software Services AG) (bbv Software Services AG)
 */
public class MethodCallFunction implements Function<Object[], Boolean> {

    private final MethodCall methodCall;

    /**
     * Creates a new instance.
     * 
     * @param methodCall
     *            the methodCall instance
     */
    public MethodCallFunction(final MethodCall methodCall) {
        this.methodCall = methodCall;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.bbv.asm.Function#execute(java.lang.Object)
     */
    @Override
    public Boolean execute(final Object[] args) {
        try {
            Object[] arguments;
            if (args.length == this.methodCall.getArguments().length) {
                arguments = args;
            } else {
                arguments = this.methodCall.getArguments();
            }

            return (Boolean) this.methodCall.getMethod().invoke(this.methodCall.getOwner(), arguments);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

}
