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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.bbv.fsm.Action;
import ch.bbv.fsm.Function;
import ch.bbv.fsm.impl.internal.Notifier;
import ch.bbv.fsm.impl.internal.state.State;
import ch.bbv.fsm.impl.internal.state.StateContext;

import com.google.common.collect.Lists;

public class TransitionImpl<TState, TEvent> implements Transition<TState, TEvent> {

    private static Logger LOG = LoggerFactory.getLogger(TransitionImpl.class);

    /**
     * The actions that are executed when this transition is fired.
     */
    private final List<Action> actions;

    private final Notifier<TState, TEvent> notifier;

    private State<TState, TEvent> source;

    private State<TState, TEvent> traget;

    private Function<Object[], Boolean> guard;

    /**
     * Creates a new instance.
     * 
     * @param notifier
     */
    public TransitionImpl(final Notifier<TState, TEvent> notifier) {
        this.notifier = notifier;

        this.actions = Lists.newArrayList();
    }

    /**
     * Recursively traverses the state hierarchy, exiting states along the way,
     * performing the action, and entering states to the target.
     * <hr>
     * There exist the following transition scenarios:
     * <ul>
     * <li>0. there is no target state (internal transition) --> handled outside
     * this method.</li>
     * <li>1. The source and target state are the same (self transition) -->
     * perform the transition directly: Exit source state, perform transition
     * actions and enter target state</li>
     * <li>2. The target state is a direct or indirect sub-state of the source
     * state --> perform the transition actions, then traverse the hierarchy
     * from the source state down to the target state, entering each state along
     * the way. No state is exited.
     * <li>3. The source state is a sub-state of the target state --> traverse
     * the hierarchy from the source up to the target, exiting each state along
     * the way. Then perform transition actions. Finally enter the target state.
     * </li>
     * <li>4. The source and target state share the same super-state</li>
     * <li>5. All other scenarios:
     * <ul>
     * <li>a. The source and target states reside at the same level in the
     * hierarchy but do not share the same direct super-state</li>
     * <li>b. The source state is lower in the hierarchy than the target state</li>
     * <li>c. The target state is lower in the hierarchy than the source state</li>
     * </ul>
     * </ul>
     * 
     * @param source
     *            the source state
     * @param target
     *            the target state
     * @param eventArguments
     *            the event arguments
     * @param context
     *            the state context
     */
    private void fire(final State<TState, TEvent> source, final State<TState, TEvent> target,
            final Object[] eventArguments, final TransitionContext<TState, TEvent> context) {
        if (source == this.getTarget()) {
            // Handles 1.
            // Handles 3. after traversing from the source to the target.
            source.exit(context);
            this.performActions(eventArguments, context);
            this.getTarget().entry(context);
        } else if (source == target) {
            // Handles 2. after traversing from the target to the source.
            this.performActions(eventArguments, context);
        } else if (source.getSuperState() == target.getSuperState()) {
            // // Handles 4.
            // // Handles 5a. after traversing the hierarchy until a common
            // ancestor if found.
            source.exit(context);
            this.performActions(eventArguments, context);
            target.entry(context);
        } else {
            // traverses the hierarchy until one of the above scenarios is met.

            // Handles 3.
            // Handles 5b.
            if (source.getLevel() > target.getLevel()) {
                source.exit(context);
                this.fire(source.getSuperState(), target, eventArguments, context);
            } else if (source.getLevel() < target.getLevel()) {
                // Handles 2.
                // Handles 5c.
                this.fire(source, target.getSuperState(), eventArguments, context);
                target.entry(context);
            } else {
                // Handles 5a.
                source.exit(context);
                this.fire(source.getSuperState(), target.getSuperState(), eventArguments, context);
                target.entry(context);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.bbv.asm.impl.internal.transition.Transition#fire(ch.bbv.asm.impl.internal
     * .transition.TransitionContext)
     */
    @Override
    @SuppressWarnings("unchecked")
    public TransitionResult<TState, TEvent> fire(final TransitionContext<TState, TEvent> context) {
        if (!this.shouldFire(context.getEventArguments(), context)) {
            return (TransitionResult<TState, TEvent>) TransitionResultImpl.NOT_FIRED;
        }

        this.notifier.onTransitionBegin(context);

        State<TState, TEvent> newState = context.getState();

        if (!this.isInternalTransition()) {
            this.unwindSubStates(context.getState(), context);

            this.fire(this.getSource(), this.getTarget(), context.getEventArguments(), context);

            newState = this.getTarget().enterByHistory(context);
        } else {
            this.performActions(context.getEventArguments(), context);
        }

        return new TransitionResultImpl<TState, TEvent>(true, newState, context.getExceptions());
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.bbv.asm.impl.internal.transition.Transition#getActions()
     */
    @Override
    public List<Action> getActions() {
        return this.actions;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.bbv.asm.impl.internal.transition.Transition#getGuard()
     */
    @Override
    public Function<Object[], Boolean> getGuard() {
        return this.guard;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.bbv.asm.impl.internal.transition.Transition#getSource()
     */
    @Override
    public State<TState, TEvent> getSource() {
        return this.source;
    }

    /*
     * (non-Javadoc)
     * 
     * @see ch.bbv.asm.impl.internal.transition.Transition#getTarget()
     */
    @Override
    public State<TState, TEvent> getTarget() {
        return this.traget;
    }

    /**
     * Handles an exception thrown during performing the transition or guard
     * evaluation.
     * 
     * @param exception
     *            the exception
     * @param context
     *            the transition context
     */
    private void handleException(final Exception exception, final TransitionContext<TState, TEvent> context) {
        context.getExceptions().add(exception);
        this.notifier.onExceptionThrown(context, exception);
    }

    /**
     * Gets a value indicating whether this is an internal transition. true =
     * internal.
     */
    private boolean isInternalTransition() {
        return this.traget == null;
    }

    /**
     * Performs the actions of this transition.
     * 
     * @param eventArguments
     *            the event arguments
     * @param context
     *            the transition context
     */
    private void performActions(final Object[] eventArguments, final TransitionContext<TState, TEvent> context) {
        for (final Action action : this.getActions()) {
            try {
                action.execute(eventArguments);
            } catch (final Exception exception) {
                LOG.error("Exception in action of transition {}: {}", this, exception);
                this.handleException(exception, context);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.bbv.asm.impl.internal.transition.Transition#setGuard(ch.bbv.asm.Function
     * )
     */
    @Override
    public void setGuard(final Function<Object[], Boolean> guard) {
        this.guard = guard;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.bbv.asm.impl.internal.transition.Transition#setSource(ch.bbv.asm.impl
     * .internal.state.State)
     */
    @Override
    public void setSource(final State<TState, TEvent> source) {
        this.source = source;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.bbv.asm.impl.internal.transition.Transition#setTarget(ch.bbv.asm.impl
     * .internal.state.State)
     */
    @Override
    public void setTarget(final State<TState, TEvent> target) {
        this.traget = target;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ch.bbv.asm.impl.internal.transition.Transition#setTargetState(ch.bbv.
     * asm.impl.internal.state.State)
     */
    @Override
    public void setTargetState(final State<TState, TEvent> targetState) {
        this.traget = targetState;
    }

    /**
     * Returns true if the transition should fire.
     * 
     * @param eventArguments
     *            the event arguments
     * @param context
     *            the context
     * @return true if the transition should fire
     */
    private boolean shouldFire(final Object[] eventArguments, final TransitionContext<TState, TEvent> context) {
        try {
            return this.getGuard() == null || this.getGuard().execute(eventArguments);
        } catch (final Exception exception) {
            LOG.error("Exception in guard of transition {}: {}", this, exception);
            this.handleException(exception, context);
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format("Transition from state %s to state %s.", this.getSource(), this.getTarget());
    }

    /**
     * Exits all sub-states up the hierarchy up to the origin state.
     * 
     * @param origin
     *            the origin state
     * @param stateContext
     *            the state context
     */
    private void unwindSubStates(final State<TState, TEvent> origin, final StateContext<TState, TEvent> stateContext) {
        for (State<TState, TEvent> o = origin; o != this.getSource(); o = o.getSuperState()) {
            o.exit(stateContext);
        }
    }
}
