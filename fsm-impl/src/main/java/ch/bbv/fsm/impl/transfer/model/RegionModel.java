package ch.bbv.fsm.impl.transfer.model;

import java.util.Collections;
import java.util.List;

import ch.bbv.fsm.impl.transfer.GeneratorException;

import com.google.common.collect.Lists;

public class RegionModel {

  private final String guid;

  private final String name;

  private final List<StateModel> states = Lists.newLinkedList();

  private final List<TransitionModel> transitions = Lists.newLinkedList();

  public RegionModel(final String guid, final String name) {
    this.guid = guid;
    this.name = name;
  }

  public void appendString(final StateMachineModel stateMachineModel, final String indent,
      final StringBuilder str) {
    str.append(indent).append("Region: ").append(guid).append(" / ").append(name).append('\n');
    for (final StateModel sm : states) {
      sm.appendString(indent + '\t', str);
    }
    for (final TransitionModel tm : transitions) {
      tm.appendString(stateMachineModel, indent + '\t', str);
    }
  }

  public void addNewState(final String guid, final String name) {
    states.add(new StateModel(guid, name));
  }

  public TransitionModel addNewTransition(final String guid, final String source,
      final String target) {
    final TransitionModel tm = new TransitionModel(guid, source, target);
    transitions.add(tm);
    return tm;
  }

  public void addNewPseudoState(final String guid, final String name, final PseudostateKinds kind) {
    states.add(new PseudoStateModel(guid, name, kind));
  }

  public void addNewFinalState(final String guid, final String name) {
    states.add(new FinalStateModel(guid, name));
  }

  public List<StateModel> getOrderedStates() {
    final List<StateModel> orderedList = Lists.newArrayList(states);
    Collections.sort(orderedList);
    return orderedList;
  }

  public PseudoStateModel findInitialState() throws GeneratorException {
    PseudoStateModel result = null;
    for (final StateModel sm : states) {
      if (sm.isInitialState()) {
        final PseudoStateModel candidate = (PseudoStateModel) sm;
        if (result != null) {
          throw new GeneratorException("only one initial state allowed");
        }
        result = candidate;
      }
    }
    return result;
  }

  public List<TransitionModel> getTransitionsForSource(final String guid) throws GeneratorException {
    final List<TransitionModel> result = Lists.newLinkedList();
    for (final TransitionModel tm : transitions) {
      if (guid.equals(tm.getSource())) {
        result.add(tm);
      }
    }
    return result;
  }

  public StateModel getState(final String target) throws GeneratorException {
    for (final StateModel sm : states) {
      if (target.equals(sm.getGuid())) {
        return sm;
      }
    }
    throw new GeneratorException("state with guid not found: " + target);
  }
}
