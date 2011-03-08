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
package ch.bbv.fsm.impl.internal.transition;

import ch.bbv.fsm.impl.internal.state.State;

public interface TransitionResult<TState, TEvent> {

    /**
     * Returns the new state.
     * 
     * @return the new state.
     */
    State<TState, TEvent> getNewState();

    /**
     * Gets a value indicating whether this transition is fired.
     * 
     * @return true if fired; otherwise, false.
     */
    boolean isFired();

}
