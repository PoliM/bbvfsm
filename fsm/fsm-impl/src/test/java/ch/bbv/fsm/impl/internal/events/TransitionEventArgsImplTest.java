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

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import ch.bbv.fsm.impl.StatesAndEvents.Events;
import ch.bbv.fsm.impl.StatesAndEvents.States;
import ch.bbv.fsm.impl.internal.state.State;
import ch.bbv.fsm.impl.internal.transition.TransitionContext;
public class TransitionEventArgsImplTest {
    @Test
    public void toStringExpectInformationAboutTheTransitionEvent() {
        @SuppressWarnings("unchecked")
		final State<States, Events> stateMock = Mockito.mock(State.class);
        Mockito.when(stateMock.getId()).thenReturn(States.A);
        @SuppressWarnings("unchecked")
		final TransitionContext<States, Events> contextMock = Mockito.mock(TransitionContext.class);
        Mockito.when(contextMock.getEventId()).thenReturn(Events.A);
        Mockito.when(contextMock.getState()).thenReturn(stateMock);
        final TransitionEventArgsImpl<States, Events> testee = new TransitionEventArgsImpl<States, Events>(contextMock);
        Assert.assertEquals("Transition from state A on event A.", testee.toString());
    }
}
