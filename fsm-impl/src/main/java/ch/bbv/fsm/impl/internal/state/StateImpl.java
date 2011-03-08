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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.bbv.fsm.HistoryType;
import ch.bbv.fsm.impl.internal.ActionHolder;
import ch.bbv.fsm.impl.internal.Notifier;
import ch.bbv.fsm.impl.internal.state.StateContext.RecordType;
import ch.bbv.fsm.impl.internal.transition.Transition;
import ch.bbv.fsm.impl.internal.transition.TransitionContext;
import ch.bbv.fsm.impl.internal.transition.TransitionDictionary;
import ch.bbv.fsm.impl.internal.transition.TransitionDictionaryImpl;
import ch.bbv.fsm.impl.internal.transition.TransitionResult;
import ch.bbv.fsm.impl.internal.transition.TransitionResultImpl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

/**
 * Implementation of the state.
 * 
 * @author Ueli Kurmann (bbv Software Services AG) (bbv Software Services AG)
 * @param <TState>
 * @param <TEvent>
 */
public class StateImpl<TState, TEvent> implements State<TState, TEvent> {

    private static Logger LOG = LoggerFactory.getLogger(StateImpl.class);

    /**
     * The level of this state within the state hierarchy [1..maxLevel]
     */
    private int level;
    private final List<State<TState, TEvent>> subStates;

    /**
     * The super-state of this state. Null for states with <code>level</code>
     * equal to 1.
     */
    private State<TState, TEvent> superState;

    /**
     * Collection of transitions that start in this .
     * (Transition<TState,TEvent>.getSource() is equal to this state)
     */
    private final TransitionDictionary<TState, TEvent> transitions;

    /**
     * The notifier used to notify events.
     */
    private final Notifier<TState, TEvent> notifier;

    /**
     * The initial sub-state of this state.
     */
    private State<TState, TEvent> initialState;

    /**
     * The HistoryType of this state.
     */
    private HistoryType historyType = HistoryType.NONE;

    /**
     * The unique state id.
     */
    private final TState id;

    /**
     * The entry action.
     */
    private ActionHolder entryAction;

    /**
     * The exit action.
     */
    private ActionHolder exitAction;

    /**
     * the last active state.
     */
    private State<TState, TEvent> lastActiveState;

    /**
     * Initializes a new instance of the state.
     * 
     * @param id
     *            the unique id of the state.
     * @param notifier
     *            the notifier used to notify events.
     */

    public StateImpl(final TState id, final Notifier<TState, TEvent> notifier) {
        this.id = id;
        this.level = 1;
        this.notifier = notifier;

        this.subStates = Lists.newArrayList();
        this.transitions = new TransitionDictionaryImpl<TState, TEvent>(this);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.bbv.asm.impl.internal.state.State#addSubState(ch.bbv.asm.impl.internal
     * .state.State)
     */
    @Override
    public void addSubState(final State<TState, TEvent> state) {
        this.subStates.add(state);

    }

    /**
     * Throws an exception if the new initial state is not a sub-state of this
     * instance.
     * 
     * @param value
     */
    private void checkInitialStateIsASubState(final State<TState, TEvent> value) {
        if (value.getSuperState() != this) {
            throw new IllegalArgumentException(String.format(
                    "State {0} cannot be the initial state of super state {1} because it is not a direct sub-state.",
                    value, this));
        }
    }

    /**
     * Throws an exception if the new initial state is this instance.
     * 
     * @param newInitialState
     *            the new initial state.
     */
    private void checkInitialStateIsNotThisInstance(final State<TState, TEvent> newInitialState) {
        if (this == newInitialState) {
            throw new IllegalArgumentException(String.format("State {0} cannot be the initial sub-state to itself.",
                    this));
        }
    }

    /**
     * Throws an exception if the new super state is this instance.
     * 
     * @param newSuperState
     *            the super state.
     */
    private void checkSuperStateIsNotThisInstance(final State<TState, TEvent> newSuperState) {
        if (this == newSuperState) {
            throw new IllegalArgumentException(String.format(

            "State {0} cannot be its own super-state.", this));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.bbv.asm.impl.internal.state.State#enterByHistory(ch.bbv.asm.impl.internal
     * .state.StateContext)
     */
    @Override
    public State<TState, TEvent> enterByHistory(final StateContext<TState, TEvent> stateContext) {

        State<TState, TEvent> result = this;

        switch (this.historyType) {
        case NONE:
            result = enterHistoryNone(stateContext);
            break;
        case SHALLOW:
            result = enterHistoryShallow(stateContext);
            break;
        case DEEP:
            result = enterHistoryDeep(stateContext);
            break;
        }

        return result;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.bbv.asm.impl.internal.state.State#enterDeep(ch.bbv.asm.impl.internal
     * .state.StateContext)
     */
    @Override
    public State<TState, TEvent> enterDeep(final StateContext<TState, TEvent> stateContext) {
        this.entry(stateContext);

        return this.lastActiveState == null ? this : this.lastActiveState.enterDeep(stateContext);

    }

    /**
     * Enters this instance with history type = deep.
     * 
     * @param stateContext
     *            the state context.
     * @return the state
     */
    private State<TState, TEvent> enterHistoryDeep(final StateContext<TState, TEvent> stateContext) {
        return this.getLastActiveState() != null ? this.getLastActiveState().enterDeep(stateContext) : this;
    }

    /**
     * Enters with history type = none.
     * 
     * @param stateContext
     *            state context
     * @return the entered state.
     */
    private State<TState, TEvent> enterHistoryNone(final StateContext<TState, TEvent> stateContext) {
        return this.initialState != null ? this.getInitialState().enterShallow(stateContext) : this;
    }

    /**
     * Enters this instance with history type = shallow.
     * 
     * @param stateContext
     *            state context
     * @return the entered state
     */
    private State<TState, TEvent> enterHistoryShallow(final StateContext<TState, TEvent> stateContext) {
        return this.getLastActiveState() != null ? this.getLastActiveState().enterShallow(stateContext) : this;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.bbv.asm.impl.internal.state.State#enterShallow(ch.bbv.asm.impl.internal
     * .state.StateContext)
     */
    @Override
    public State<TState, TEvent> enterShallow(final StateContext<TState, TEvent> stateContext) {
        this.entry(stateContext);

        return this.initialState == null ? this : this.initialState.enterShallow(stateContext);

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.bbv.asm.impl.internal.state.State#entry(ch.bbv.asm.impl.internal.state
     * .StateContext)
     */
    @Override
    public void entry(final StateContext<TState, TEvent> stateContext) {

        stateContext.addRecord(this.getId(), RecordType.Enter);

        if (this.entryAction != null) {
            try {
                this.entryAction.execute();
            } catch (final Exception e) {
                handleException(e, stateContext);
            }
        }

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.bbv.asm.impl.internal.state.State#exit(ch.bbv.asm.impl.internal.state
     * .StateContext)
     */
    @Override
    public void exit(final StateContext<TState, TEvent> stateContext) {
        stateContext.addRecord(this.getId(), StateContext.RecordType.Exit);

        if (this.exitAction != null) {
            try {
                this.exitAction.execute();
            } catch (final Exception e) {
                handleException(e, stateContext);
            }
        }
        this.setThisStateAsLastStateOfSuperState();

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.bbv.asm.impl.internal.state.State#fire(ch.bbv.asm.impl.internal.transition
     * .TransitionContext)
     */
    @SuppressWarnings("unchecked")
    @Override
    public TransitionResult<TState, TEvent> fire(final TransitionContext<TState, TEvent> context) {
        TransitionResult<TState, TEvent> result = (TransitionResult<TState, TEvent>) TransitionResultImpl.NOT_FIRED;

        final List<Transition<TState, TEvent>> transitionsForEvent = this.transitions.getTransitions(context
                .getEventId());
        if (transitionsForEvent != null) {
            for (final Transition<TState, TEvent> transition : transitionsForEvent) {
                result = transition.fire(context);
                if (result.isFired()) {
                    return result;
                }
            }
        }
        LOG.info("No transition available in this state ({}).", this.getId());

        if (this.getSuperState() != null) {
            LOG.info("Fire the same event on the super state.");
            result = this.getSuperState().fire(context);
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.bbv.asm.impl.internal.state.State#getEntryAction()
     */
    @Override
    public ActionHolder getEntryAction() {
        return this.entryAction;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.bbv.asm.impl.internal.state.State#getExitAction()
     */
    @Override
    public ActionHolder getExitAction() {
        return this.exitAction;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.bbv.asm.impl.internal.state.State#getHistoryType()
     */
    @Override
    public HistoryType getHistoryType() {
        return this.historyType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.bbv.asm.impl.internal.state.State#getId()
     */
    @Override
    public TState getId() {
        return this.id;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.bbv.asm.impl.internal.state.State#getInitialState()
     */
    @Override
    public State<TState, TEvent> getInitialState() {
        return this.initialState;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.bbv.asm.impl.internal.state.State#getLastActiveState()
     */
    @Override
    public State<TState, TEvent> getLastActiveState() {
        return this.lastActiveState;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.bbv.asm.impl.internal.state.State#getLevel()
     */
    @Override
    public int getLevel() {
        return this.level;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.bbv.asm.impl.internal.state.State#getSubStates()
     */
    @Override
    public List<State<TState, TEvent>> getSubStates() {
        return ImmutableList.copyOf(this.subStates);
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.bbv.asm.impl.internal.state.State#getSuperState()
     */
    @Override
    public State<TState, TEvent> getSuperState() {
        return this.superState;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.bbv.asm.impl.internal.state.State#getTransitions()
     */
    @Override
    public TransitionDictionary<TState, TEvent> getTransitions() {
        return this.transitions;
    }

    /**
     * Handles the specific exception.
     * 
     * @param exception
     *            the exception
     * @param stateContext
     *            the state context.
     */
    private void handleException(final Exception exception, final StateContext<TState, TEvent> stateContext) {
        stateContext.getExceptions().add(exception);
        this.notifier.onExceptionThrown(stateContext, exception);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.bbv.asm.impl.internal.state.State#setEntryAction(ch.bbv.asm.impl.internal
     * .ActionHolder)
     */
    @Override
    public void setEntryAction(final ActionHolder action) {
        this.entryAction = action;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.bbv.asm.impl.internal.state.State#setExitAction(ch.bbv.asm.impl.internal
     * .ActionHolder)
     */
    @Override
    public void setExitAction(final ActionHolder action) {
        this.exitAction = action;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.bbv.asm.impl.internal.state.State#setHistoryType(ch.bbv.asm.HistoryType
     * )
     */
    @Override
    public void setHistoryType(final HistoryType historyType) {
        this.historyType = historyType;

    }

    /**
     * Sets the initial level depending on the level of the super state of this
     * instance.
     */
    private void setInitialLevel() {
        this.setLevel(this.superState != null ? this.superState.getLevel() + 1 : 1);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.bbv.asm.impl.internal.state.State#setInitialState(ch.bbv.asm.impl.
     * internal.state.State)
     */
    @Override
    public void setInitialState(final State<TState, TEvent> initialState) {
        this.checkInitialStateIsNotThisInstance(initialState);
        this.checkInitialStateIsASubState(initialState);

        this.initialState = initialState;
        this.lastActiveState = initialState;

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.bbv.asm.impl.internal.state.State#setLastActiveState(ch.bbv.asm.impl
     * .internal.state.State)
     */
    @Override
    public void setLastActiveState(final State<TState, TEvent> state) {
        this.lastActiveState = state;

    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.bbv.asm.impl.internal.state.State#setLevel(int)
     */
    @Override
    public void setLevel(final int level) {
        this.level = level;
        this.setLevelOfSubStates();
    }

    /**
     * Sets the level of all sub states.
     */
    private void setLevelOfSubStates() {
        for (final State<TState, TEvent> state : this.getSubStates()) {
            state.setLevel(this.level + 1);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.bbv.asm.impl.internal.state.State#setSuperState(ch.bbv.asm.impl.internal
     * .state.State)
     */
    @Override
    public void setSuperState(final State<TState, TEvent> superState) {
        this.checkSuperStateIsNotThisInstance(superState);
        this.superState = superState;
        this.setInitialLevel();

    }

    /**
     * Sets this instance as the last state of this instance's super state.
     */
    private void setThisStateAsLastStateOfSuperState() {
        if (this.superState != null) {
            this.superState.setLastActiveState(this);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.id.toString();
    }

}
