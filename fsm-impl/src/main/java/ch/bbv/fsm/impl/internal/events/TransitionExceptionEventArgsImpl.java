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

import ch.bbv.fsm.events.TransitionExceptionEventArgs;
import ch.bbv.fsm.impl.internal.transition.TransitionContext;

public class TransitionExceptionEventArgsImpl<TState, TEvent> extends TransitionEventArgsImpl<TState, TEvent> implements
        TransitionExceptionEventArgs<TState, TEvent> {

    // / <summary>
    // / The exception.
    // / </summary>
    private final Exception exception;

    // / <summary>
    // / Initializes a new instance of the <see
    // cref="TransitionExceptionEventArgs&lt;TState, TEvent&gt;"/> class.
    // / </summary>
    // / <param name="context">The event context.</param>
    // / <param name="exception">The exception.</param>
    public TransitionExceptionEventArgsImpl(final TransitionContext<TState, TEvent> context, final Exception exception) {
        super(context);
        this.exception = exception;
    }

    // / <summary>
    // / Gets the exception.
    // / </summary>
    // / <value>The exception.</value>
    /*
     * (non-Javadoc)
     * 
     * @see ch.bbv.asm.impl.ITransitionExceptionEventArgs#getException()
     */
    @Override
    public Exception getException() {
        return this.exception;
    }

}
