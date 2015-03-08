package ch.bbv.fsm.impl.transfer.model;

public class TriggerModel implements Comparable<TriggerModel> {

	protected String guid;

	protected String name;

	public TriggerModel(String guid, String name) {
		this.guid = guid;
		this.name = name;
	}

	public void appendString(String indent, StringBuilder str) {
		str.append(indent).append("Trigger: ").append(guid).append(" / ").append(name).append('\n');
	}

	@Override
	public int compareTo(TriggerModel o) {
		return name.compareTo(o.name);
	}

	public String getName() {
		return name;
	}
}
