package ch.bbv.fsm.impl.transfer.model;

public class StateModel implements Comparable<StateModel> {

	private String guid;

	protected String name;

	public StateModel(String guid, String name) {
		this.guid = guid;
		this.name = name;
	}

	public void appendString(String indent, StringBuilder str) {
		str.append(indent).append("State: ").append(getGuid()).append(" / ").append(name).append('\n');
	}

	@Override
	public int compareTo(StateModel o) {
		return name.compareTo(o.name);
	}

	public String getName() {
		return name;
	}

	public String getGuid() {
		return guid;
	}

	public boolean isInitialState() {
		return false;
	}
}
