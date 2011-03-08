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
package ch.bbv.fsm.impl.internal;

import ch.bbv.fsm.Action;
import ch.bbv.fsm.Function;
import ch.bbv.fsm.dsl.EntryActionSyntax;
import ch.bbv.fsm.dsl.EventActionSyntax;
import ch.bbv.fsm.dsl.EventSyntax;
import ch.bbv.fsm.dsl.ExecuteSyntax;
import ch.bbv.fsm.dsl.ExitActionSyntax;
import ch.bbv.fsm.dsl.GotoSyntax;
import ch.bbv.fsm.dsl.GuardSyntax;
import ch.bbv.fsm.impl.internal.aop.MethodCallImpl;
import ch.bbv.fsm.impl.internal.state.State;
import ch.bbv.fsm.impl.internal.state.StateDictionary;
import ch.bbv.fsm.impl.internal.transition.Transition;
import ch.bbv.fsm.impl.internal.transition.TransitionImpl;

/**
 * State Builder.
 * 
 * @author Ueli Kurmann (bbv Software Services AG) (bbv Software Services AG).
 * @param <TState>
 *            the type of the states.
 * @param <TEvent>
 *            the type of the events.
 */
public class StateBuilder<TState, TEvent> implements EntryActionSyntax<TState, TEvent>,
        EventActionSyntax<TState, TEvent>, ExecuteSyntax<TState, TEvent>, GotoSyntax<TState, TEvent> {

    private final State<TState, TEvent> state;
    private final StateDictionary<TState, TEvent> stateDictionary;
    private final Notifier<TState, TEvent> notifier;
    private Transition<TState, TEvent> currentTransition;

    /**
     * Creates a new instance.
     * 
     * @param state
     *            the state
     * @param stateDictionary
     *            the state dictionary
     * @param notifier
     *            the notifier
     */
    public StateBuilder(final State<TState, TEvent> state, final StateDictionary<TState, TEvent> stateDictionary,
            final Notifier<TState, TEvent> notifier) {
        this.state = state;
        this.stateDictionary = stateDictionary;
        this.notifier = notifier;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.bbv.asm.dsl.EventActionSyntax#execute(ch.bbv.asm.Action[])
     */
    @Override
    public GuardSyntax<TState, TEvent> execute(final Action... actions) {
        if (actions == null) {
            return this;
        }

        for (final Action action : actions) {
            this.currentTransition.getActions().add(action);
        }

        return this;
    }

    @Override
    public GuardSyntax<TState, TEvent> execute(final Object... methodCalls) {

        if (methodCalls == null) {
            return this;
        }

        for (@SuppressWarnings("unused")
        final Object action : methodCalls) {
            this.currentTransition.getActions().add(new MethodCallAction(MethodCallImpl.pop()));
        }

        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.bbv.asm.dsl.EntryActionSyntax#executeOnEntry(ch.bbv.asm.Action)
     */
    @Override
    public ExitActionSyntax<TState, TEvent> executeOnEntry(final Action action) {
        this.state.setEntryAction(new ActionHolderNoParameter(action));

        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.bbv.asm.dsl.EntryActionSyntax#executeOnEntry(ch.bbv.asm.Action,
     * java.lang.Object)
     */
    @Override
    public <T> ExitActionSyntax<TState, TEvent> executeOnEntry(final Action action, final T parameter) {
        this.state.setEntryAction(new ActionHolderParameter<T>(action, parameter));

        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.bbv.asm.tdl.EntryActionSyntax#executeOnEntry(ch.bbv.asm.tdl.MethodCall
     * )
     */
    @Override
    public ExitActionSyntax<TState, TEvent> executeOnEntry(final Object action) {
        this.state.setEntryAction(new ActionHolderMethodCall(MethodCallImpl.pop()));
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.bbv.asm.dsl.ExitActionSyntax#executeOnExit(ch.bbv.asm.Action)
     */
    @Override
    public ExitActionSyntax<TState, TEvent> executeOnExit(final Action action) {
        this.state.setExitAction(new ActionHolderNoParameter(action));

        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.bbv.asm.dsl.ExitActionSyntax#executeOnExit(ch.bbv.asm.Action,
     * java.lang.Object)
     */
    @Override
    public <T> EventSyntax<TState, TEvent> executeOnExit(final Action action, final T parameter) {
        this.state.setExitAction(new ActionHolderParameter<T>(action, parameter));
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.bbv.asm.dsl.ExitActionSyntax#executeOnExit(java.lang.Object)
     */
    @Override
    public ExitActionSyntax<TState, TEvent> executeOnExit(final Object action) {
        this.state.setExitAction(new ActionHolderMethodCall(MethodCallImpl.pop()));
        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.bbv.asm.dsl.EventActionSyntax#goTo(java.lang.Object)
     */
    @Override
    public ExecuteSyntax<TState, TEvent> goTo(final TState target) {
        this.currentTransition.setTarget(this.stateDictionary.getState(target));

        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.bbv.asm.dsl.EventSyntax#on(java.lang.Object)
     */
    @Override
    public EventActionSyntax<TState, TEvent> on(final TEvent eventId) {
        this.currentTransition = new TransitionImpl<TState, TEvent>(this.notifier);

        this.state.getTransitions().add(eventId, this.currentTransition);

        return this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.bbv.asm.dsl.GuardSyntax#onlyIf(boolean)
     */
    @Override
    public EventSyntax<TState, TEvent> onlyIf(final boolean guard) {
        this.currentTransition.setGuard(new MethodCallFunction(MethodCallImpl.pop()));
        return this;

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.bbv.asm.dsl.GuardSyntax#onlyIf(ch.bbv.asm.Function)
     */
    @Override
    public EventSyntax<TState, TEvent> onlyIf(final Function<Object[], Boolean> guard) {
        this.currentTransition.setGuard(guard);

        return this;
    }

}
