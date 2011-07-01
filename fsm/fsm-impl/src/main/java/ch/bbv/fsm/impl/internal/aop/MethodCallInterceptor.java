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

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import ch.bbv.fsm.dsl.MethodCall;
/**
 * The method call interceptor.
 * 
 * @author Ueli Kurmann (bbv Software Services AG) (bbv Software Services AG)
 */
public class MethodCallInterceptor implements MethodInterceptor {
    private final Object owner;

    /**
     * Creates a new instance.
     * 
     * @param owner
     *            the owner of the method.
     */
    public MethodCallInterceptor(final Object owner) {
        this.owner = owner;
    }

    /*
     * (non-Javadoc)
     * 
     * @see net.sf.cglib.proxy.MethodInterceptor#intercept(java.lang.Object,
     * java.lang.reflect.Method, java.lang.Object[], net.sf.cglib.proxy.MethodProxy)
     */
    @Override
    public Object intercept(final Object object, final Method method, final Object[] args, final MethodProxy methodProxy) throws Throwable {
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }
        final MethodCall methodCall = new MethodCallImpl(this.owner, method, args);
        MethodCallImpl.push(methodCall);
        return null;
    }
}