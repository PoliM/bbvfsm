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
package ch.bbv.fsm.impl.internal.events;

import ch.bbv.fsm.events.TransitionCompletedEventArgs;
import ch.bbv.fsm.impl.internal.transition.TransitionContext;

public class TransitionCompletedEventArgsImpl<TState, TEvent> extends TransitionEventArgsImpl<TState, TEvent> implements
        TransitionCompletedEventArgs<TState, TEvent> {

    // / <summary>
    // / The new state the state machine is in after the transition.
    // / </summary>
    private final TState newStateId;

    // / <summary>
    // / Initializes a new instance of the <see
    // cref="TransitionCompletedEventArgs&lt;TState, TEvent&gt;"/> class.
    // / </summary>
    // / <param name="newStateId">The new state id.</param>
    // / <param name="context">The context.</param>
    public TransitionCompletedEventArgsImpl(final TState newStateId, final TransitionContext<TState, TEvent> context) {
        super(context);
        this.newStateId = newStateId;
    }

    // / <summary>
    // / Gets the new state id the state machine is in after the transition.
    // / </summary>
    // / <value>The new state id the state machine is in after the
    // transition.</value>
    /*
     * (non-Javadoc)
     * 
     * @see ch.bbv.asm.impl.ITransitionCompletedEventArgs#getNewStateId()
     */
    @Override
    public TState getNewStateId() {
        return this.newStateId;
    }

}
