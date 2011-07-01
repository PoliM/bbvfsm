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
package ch.bbv.fsm.dsl;

import ch.bbv.fsm.Action;

public interface ExitActionSyntax<TState, TEvent> extends EventSyntax<TState, TEvent> {

    /** 
     * Defines an exit action.
     * 
     * @param action
     *            the action.
     * @return Event syntax.
     */
    EventSyntax<TState, TEvent> executeOnExit(Action action);

    /**
     * Defines an exit action.
     * 
     * @param <T>
     *            type of the parameter.
     * @param action
     *            the action.
     * @param parameter
     *            the parameter of the action.
     * @return Exit action syntax.
     */
    <T> EventSyntax<TState, TEvent> executeOnExit(Action action, T parameter);

    /**
     * Defines an exit action.
     * 
     * @param method
     * @return Event syntax.
     */
    EventSyntax<TState, TEvent> executeOnExit(Object method);

}
