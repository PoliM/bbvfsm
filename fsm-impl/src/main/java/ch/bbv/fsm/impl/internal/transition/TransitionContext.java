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
import ch.bbv.fsm.impl.internal.state.StateContext;

public class TransitionContext<TState, TEvent> extends StateContext<TState, TEvent> {

    /**
     * The event that causes the transition.
     */
    private final TEvent eventId;

    /**
     * The event arguments.
     */
    private final Object[] eventArguments;

    /**
     * Creates a new instance.
     * 
     * @param state
     *            the source state
     * @param eventId
     *            the event id
     * @param eventArguments
     *            the event arguments
     */
    public TransitionContext(final State<TState, TEvent> state, final TEvent eventId, final Object[] eventArguments) {
        super(state);
        this.eventId = eventId;
        this.eventArguments = eventArguments;
    }

    /**
     * Returns the event arguments.
     * 
     * @return the event arguments.
     */
    public Object[] getEventArguments() {
        return this.eventArguments;
    }

    /**
     * Returns the event id.
     * 
     * @return the event id.
     */
    public TEvent getEventId() {
        return this.eventId;
    }

}
