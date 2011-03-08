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

/**
 * Entry Action Syntax.
 * 
 * @author Ueli Kurmann (bbv Software Services AG) (bbv Software Services AG)
 * @param <TState>
 *            the type of the states.
 * @param <TEvent>
 *            the type of the events.
 */
public interface EntryActionSyntax<TState, TEvent> extends ExitActionSyntax<TState, TEvent>, EventSyntax<TState, TEvent> {

    /**
     * Defines an entry action.
     * 
     * @param <T>
     * @param action
     *            the Action
     * @return the ExitActionSyntax.
     */
    <T> ExitActionSyntax<TState, TEvent> executeOnEntry(Action action);

    /**
     * Defines an entry action.
     * 
     * @param <T>
     *            The return type of the action.
     * @param action
     *            The action.
     * @param parameter
     *            (necessary?)
     * @return the ExitActionSyntax
     */
    <T> ExitActionSyntax<TState, TEvent> executeOnEntry(Action action, T parameter);

    /**
     * Defines an entry action.
     * 
     * @param <T>
     * @param methodCall
     * @return
     */
    <T> ExitActionSyntax<TState, TEvent> executeOnEntry(Object methodCall);

}
