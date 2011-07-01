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
package ch.bbv.fsm.impl.internal.state;

import ch.bbv.fsm.HistoryType;
import ch.bbv.fsm.impl.internal.ActionHolder;
import ch.bbv.fsm.impl.internal.transition.TransitionContext;
import ch.bbv.fsm.impl.internal.transition.TransitionDictionary;
import ch.bbv.fsm.impl.internal.transition.TransitionResult;

/**
 * Represents a state of the state machine.
 * 
 * @author Ueli Kurmann (bbv Software Services AG) (bbv Software Services AG)
 * @param <TState>
 * @param <TEvent>
 */
public interface State<TState, TEvent> {

    /**
     * Adds a sub state.
     * 
     * @param state
     *            a sub state.
     */
    void addSubState(State<TState, TEvent> state);

    /**
     * Enters this state by its history depending on its
     * <code>HistoryType</code>. The <code>Entry</code> method has to be called
     * already.
     * 
     * @param stateContext
     *            the state context.
     * @return the active state. (depends on this states
     *         <code>HistoryType</code>)
     */
    State<TState, TEvent> enterByHistory(StateContext<TState, TEvent> stateContext);

    /**
     * Enters this state is deep mode: mode if there is one.
     * 
     * @param stateContext
     *            the event context.
     * @return the active state.
     */
    State<TState, TEvent> enterDeep(StateContext<TState, TEvent> stateContext);

    /**
     * Enters this state is shallow mode: The entry action is executed and the
     * initial state is entered in shallow mode if there is one.
     * 
     * @param stateContext
     *            the event context.
     * @return the active state.
     */
    State<TState, TEvent> enterShallow(StateContext<TState, TEvent> stateContext);

    /**
     * Enters this state.
     * 
     * @param stateContext
     *            the state context.
     */
    void entry(StateContext<TState, TEvent> stateContext);

    /**
     * Exits this state.
     * 
     * @param stateContext
     *            the state context.
     */
    void exit(StateContext<TState, TEvent> stateContext);

    /**
     * Fires the specified event id on this state.
     * 
     * @param context
     *            the event context.
     * @return the result of the transition.
     */
    TransitionResult<TState, TEvent> fire(TransitionContext<TState, TEvent> context);

    /**
     * Returns the entry action.
     * 
     * @return the entry action.
     */
    ActionHolder getEntryAction();

    /**
     * Gets the exit action.
     * 
     * @return the exit action.
     */
    ActionHolder getExitAction();

    /**
     * Returns the history type of this state.
     * 
     * @return the history type of this state.
     */
    HistoryType getHistoryType();

    /**
     * Gets the id of this state.
     * 
     * @return the id of this state.
     */
    TState getId();

    /**
     * Returns the initial sub-state.
     * 
     * @return the initial sub-state or Null if this state has no sub-states.
     */
    State<TState, TEvent> getInitialState();

    /**
     * Returns the last active state.
     * 
     * @return the last active state.
     */
    State<TState, TEvent> getLastActiveState();

    /**
     * Returns the level in the hierarchy.
     * 
     * @return the level in the hierarchy.
     */
    int getLevel();

    /**
     * Returns the sub-states.
     * 
     * @return the sub-states.
     */
    Iterable<State<TState, TEvent>> getSubStates();

    /**
     * Returns the super-state. Null if this is a root state.
     * 
     * @return the super-state.
     */
    State<TState, TEvent> getSuperState();

    /**
     * Returns the transitions.
     * 
     * @return the transitions.
     */
    TransitionDictionary<TState, TEvent> getTransitions();

    /**
     * Sets the entry action.
     * 
     * @param <T>
     * @param action
     *            the entry action.
     */
    void setEntryAction(ActionHolder action);

    /**
     * Sets the exit action.
     * 
     * @param <T>
     * @param action
     *            the exit action.
     */
    void setExitAction(ActionHolder action);

    /**
     * Sets the history type of this state.
     * 
     * @param historyType
     *            the history type of this state.
     */
    void setHistoryType(HistoryType historyType);

    /**
     * Sets the initial sub-state.
     * 
     * @param initialState
     *            the initial sub-state.
     */
    void setInitialState(State<TState, TEvent> initialState);

    /**
     * Sets the last active state of this state.
     * 
     * @param state
     *            the last active state of this state.
     */
    void setLastActiveState(State<TState, TEvent> state);

    /**
     * Sets the level in the hierarchy.
     * 
     * @param level
     */
    void setLevel(int level);

    /**
     * Sets the super-state.
     * 
     * @param superState
     *            the super-state.
     */
    void setSuperState(State<TState, TEvent> superState);

}
