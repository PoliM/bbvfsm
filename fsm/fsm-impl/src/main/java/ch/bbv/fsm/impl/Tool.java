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
package ch.bbv.fsm.impl;

import net.sf.cglib.core.GeneratorStrategy;
import net.sf.cglib.proxy.Enhancer;
import ch.bbv.fsm.impl.internal.aop.MethodCallInterceptor;

/**
 * Static helper methods used to define state machines.
 * 
 * @author Ueli Kurmann (bbv Software Services AG) (bbv Software Services AG)
 */
public class Tool {

    public static <T> T any(final Class<T> type) {
        return null;
    }

    /**
     * Intercepts a type to simulate delegates.
     * 
     * @param <T>
     * @param obj
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T from(final T obj) {
        final MethodCallInterceptor interceptor = new MethodCallInterceptor(obj);
        final Class<?> clazz = obj.getClass();
        final Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(interceptor);
        final T instance = (T) enhancer.create();
        return instance;
    }

    /**
     * Private constructor to prevent the creation of an instance.
     */
    private Tool() {
        // empty body
    }

}