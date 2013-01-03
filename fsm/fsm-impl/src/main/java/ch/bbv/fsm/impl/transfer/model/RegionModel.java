package ch.bbv.fsm.impl.transfer.model;

import java.util.Collections;
import java.util.List;

import ch.bbv.fsm.impl.transfer.GeneratorException;

import com.google.common.collect.Lists;

public class RegionModel {

	private String guid;

	private String name;

	private List<StateModel> states = Lists.newLinkedList();

	private List<TransitionModel> transitions = Lists.newLinkedList();

	public RegionModel(String guid, String name) {
		this.guid = guid;
		this.name = name;
	}

	public void appendString(StateMachineModel stateMachineModel, String indent, StringBuilder str) {
		str.append(indent).append("Region: ").append(guid).append(" / ").append(name).append('\n');
		for (StateModel sm : states) {
			sm.appendString(indent + '\t', str);
		}
		for (TransitionModel tm : transitions) {
			tm.appendString(stateMachineModel, indent + '\t', str);
		}
	}

	public void addNewState(String guid, String name) {
		states.add(new StateModel(guid, name));
	}

	public TransitionModel addNewTransition(String guid, String source, String target) {
		TransitionModel tm = new TransitionModel(guid, source, target);
		transitions.add(tm);
		return tm;
	}

	public void addNewPseudoState(String guid, String name, PseudostateKinds kind) {
		states.add(new PseudoStateModel(guid, name, kind));
	}

	public List<StateModel> getOrderedStates() {
		List<StateModel> orderedList = Lists.newArrayList(states);
		Collections.sort(orderedList);
		return orderedList;
	}

	public PseudoStateModel findInitialState() throws GeneratorException {
		PseudoStateModel result = null;
		for (StateModel sm : states) {
			if (sm.isInitialState()) {
				PseudoStateModel candidate = (PseudoStateModel) sm;
				if (result != null) {
					throw new GeneratorException("only one initial state allowed");
				}
				result = candidate;
			}
		}
		return result;
	}

	public List<TransitionModel> getTransitionsForSource(String guid) throws GeneratorException {
		List<TransitionModel> result = Lists.newLinkedList();
		for (TransitionModel tm : transitions) {
			if (guid.equals(tm.getSource())) {
				result.add(tm);
			}
		}
		return result;
	}

	public StateModel getState(String target) throws GeneratorException {
		for (StateModel sm : states) {
			if (target.equals(sm.getGuid())) {
				return sm;
			}
		}
		throw new GeneratorException("state with guid not found: " + target);
	}
}
