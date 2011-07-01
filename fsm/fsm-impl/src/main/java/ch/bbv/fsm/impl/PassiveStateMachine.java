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

import java.util.LinkedList;

import ch.bbv.fsm.HistoryType;
import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.dsl.EntryActionSyntax;
import ch.bbv.fsm.events.StateMachineEventHandler;
import ch.bbv.fsm.impl.internal.EventInformation;
import ch.bbv.fsm.impl.internal.StateMachineImpl;

/**
 * A passive state machine. This state machine reacts to events on the current
 * thread.
 * 
 * @author Ueli Kurmann (bbv Software Services AG) (bbv Software Services AG)
 * @param <TState>
 *            the type of the states. (Enum)
 * @param <TEvent>
 *            the type of the events. (Enum)
 */
public class PassiveStateMachine<TState extends Enum<?>, TEvent extends Enum<?>> implements
        StateMachine<TState, TEvent> {

    /**
     * The internal state machine.
     */
    private StateMachineImpl<TState, TEvent> stateMachine;

    /**
     * List of all queued events.
     */
    private LinkedList<EventInformation<TEvent>> events;

    /**
     * Whether this state machine is executing an event. Allows that events can
     * be added while executing.
     */
    private boolean executing;

    /**
     * Indicates if the state machine is running.
     */
    private boolean isRunning;

    /**
     * Initializes the passive state machine.
     */
    public PassiveStateMachine() {
        this(PassiveStateMachine.class.getSimpleName());
    }

    /**
     * Initializes the state machine.
     * 
     * @param name
     *            the name of the state machine used in the logs.
     */
    public PassiveStateMachine(final String name) {
        this.stateMachine = new StateMachineImpl<TState, TEvent>(name);
        this.events = new LinkedList<EventInformation<TEvent>>();
    }

    /**
     * (non-Javadoc)
     * 
     * @see ch.bbv.fsm.StateMachine#addEventHandler(ch.bbv.fsm.events.StateMachineEventHandler)
     */
    @Override
    public void addEventHandler(final StateMachineEventHandler<TState, TEvent> handler) {
        this.stateMachine.addEventHandler(handler);
    }

    /**
     * (non-Javadoc)
     * 
     * @see ch.bbv.fsm.StateMachine#defineHierarchyOn(java.lang.Object,
     *      java.lang.Object, ch.bbv.fsm.HistoryType, TState[])
     */
    @Override
    public void defineHierarchyOn(final TState superStateId, final TState initialSubStateId,
            final HistoryType historyType, final TState... subStateIds) {
        this.stateMachine.defineHierarchyOn(superStateId, initialSubStateId, historyType, subStateIds);
    }

    /**
     * Executes all queued events.
     */
    private void execute() {
        if (this.executing || !this.isRunning) {
            return;
        }

        this.executing = true;
        try {
            this.processQueuedEvents();
        } finally {
            this.executing = false;
        }
    }

    /**
     * (non-Javadoc)
     * 
     * @see ch.bbv.fsm.StateMachine#fire(java.lang.Object, java.lang.Object[])
     */
    @Override
    public void fire(final TEvent eventId, final Object... eventArguments) {
        this.events.addLast(new EventInformation<TEvent>(eventId, eventArguments));
        this.execute();
    }

    /**
     * Fires the event on the state machine.
     * 
     * @param e
     *            the event to be fired on the state machine.
     */
    private void fireEventOnStateMachine(final EventInformation<TEvent> e) {
        this.stateMachine.fire(e.getEventId(), e.getEventArguments());
    }

    /**
     * (non-Javadoc)
     * 
     * @see ch.bbv.fsm.StateMachine#firePriority(java.lang.Object,
     *      java.lang.Object[])
     */
    @Override
    public void firePriority(final TEvent eventId, final Object... eventArguments) {
        this.events.addFirst(new EventInformation<TEvent>(eventId, eventArguments));
        this.execute();
    }

    /**
     * Returns the current internal state of the state machine.
     * 
     * @return the current internal state of the state machine.
     */
    public TState getCurrentState() {
        return this.stateMachine.getCurrentStateId();
    }

    /**
     * Gets the next event to process for the queue.
     * 
     * @return The next queued event.
     */
    private EventInformation<TEvent> getNextEventToProcess() {
        final EventInformation<TEvent> e = this.events.getFirst();
        this.events.removeFirst();
        return e;
    }

    /**
     * (non-Javadoc)
     * 
     * @see ch.bbv.fsm.StateMachine#in(java.lang.Object)
     */
    @Override
    public EntryActionSyntax<TState, TEvent> in(final TState state) {
        return this.stateMachine.in(state);
    }

    /**
     * (non-Javadoc)
     * 
     * @see ch.bbv.fsm.StateMachine#initialize(java.lang.Object)
     */
    @Override
    public void initialize(final TState initialState) {
        this.stateMachine.initialize(initialState);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.bbv.asm.StateMachine#isExecuting()
     */
    @Override
    public boolean isExecuting() {
        return this.executing;
    }

    /**
     * (non-Javadoc)
     * 
     * @see ch.bbv.fsm.StateMachine#isRunning()
     */
    @Override
    public boolean isRunning() {
        return this.isRunning;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.bbv.asm.StateMachine#numberOfQueuedEvents()
     */
    @Override
    public int numberOfQueuedEvents() {
        return this.events.size();
    }

    /**
     * Processes the queued events.
     */
    private void processQueuedEvents() {
        while (this.events.size() > 0) {
            final EventInformation<TEvent> eventToProcess = this.getNextEventToProcess();
            this.fireEventOnStateMachine(eventToProcess);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @seech.bbv.asm.StateMachine#removeEventHandler(ch.bbv.asm.events.
     * StateMachineEventHandler)
     */
    @Override
    public void removeEventHandler(final StateMachineEventHandler<TState, TEvent> handler) {
        this.stateMachine.removeEventHandler(handler);

    }

    /**
     * (non-Javadoc)
     * 
     * @see ch.bbv.fsm.StateMachine#report()
     */
    @Override
    public String report() {
        return this.stateMachine.report();
    }

    /**
     * (non-Javadoc)
     * 
     * @see ch.bbv.fsm.StateMachine#start()
     */
    @Override
    public void start() {
        this.isRunning = true;

        this.execute();
    }

    /**
     * (non-Javadoc)
     * 
     * @see ch.bbv.fsm.StateMachine#stop()
     */
    @Override
    public void stop() {
        this.isRunning = false;
    }

}
