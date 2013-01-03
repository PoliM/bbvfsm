package ch.bbv.fsm.impl.transfer.model;

public class FinalStateModel extends StateModel {

	public FinalStateModel(String guid, String name) {
		super(guid, name);
	}

	public void appendString(String indent, StringBuilder str) {
		str.append(indent).append("FinalState: ").append(getGuid()).append(" / ").append(name).append('\n');
	}

	@Override
	public boolean isFinalState() {
		return true;
	}
}
