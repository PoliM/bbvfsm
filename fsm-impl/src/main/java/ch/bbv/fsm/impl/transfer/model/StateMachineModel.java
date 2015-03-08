package ch.bbv.fsm.impl.transfer.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.bbv.fsm.impl.transfer.GeneratorException;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class StateMachineModel {

  private final String guid;

  private final String name;

  private final List<RegionModel> regions = Lists.newLinkedList();

  private final Map<String, TriggerModel> triggers = Maps.newHashMap();

  /**
   * Initialize the object.
   *
   * @param guid The GUID of the model element.
   * @param name The name of the model element.
   */
  public StateMachineModel(final String guid, final String name) {
    this.guid = guid;
    this.name = name;
  }

  public RegionModel addNewRegion(final String guid, final String name) {
    final RegionModel rm = new RegionModel(guid, name);
    regions.add(rm);
    return rm;
  }

  @Override
  public String toString() {
    final StringBuilder str = new StringBuilder();

    str.append("StateMachine: ").append(guid).append(" / ").append(name).append('\n');

    for (final RegionModel rm : regions) {
      rm.appendString(this, "", str);
    }

    return str.toString();
  }

  public RegionModel getSingleRegion() throws GeneratorException {
    if (regions.isEmpty()) {
      throw new GeneratorException("no region found");
    }
    if (regions.size() > 1) {
      throw new GeneratorException("only one region expected");
    }
    return regions.get(0);
  }

  public void addNewTrigger(final String guid, final String name) {
    triggers.put(guid, new TriggerModel(guid, name));
  }

  public Set<String> getSortedTriggerNames() {
    final Set<String> result = Sets.newTreeSet();
    for (final TriggerModel tm : triggers.values()) {
      result.add(tm.getName());
    }
    return result;
  }

  public TriggerModel triggerFor(final String triggerGuid) {
    return triggers.get(triggerGuid);
  }

  public StateModel computeInitialState() throws GeneratorException {
    final PseudoStateModel initialState = getSingleRegion().findInitialState();
    final List<TransitionModel> transitions =
        getSingleRegion().getTransitionsForSource(initialState.getGuid());
    if (transitions.isEmpty()) {
      throw new GeneratorException("transition with source not found: " + guid);
    }
    if (transitions.size() > 1) {
      throw new GeneratorException(
          "more than one transition with source found (but only one is allowed for initial state): "
              + guid);
    }
    final StateModel state = getSingleRegion().getState(transitions.get(0).getTarget());
    return state;
  }
}
