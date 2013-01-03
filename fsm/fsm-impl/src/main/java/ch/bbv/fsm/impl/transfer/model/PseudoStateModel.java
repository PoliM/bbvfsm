package ch.bbv.fsm.impl.transfer.model;

public class PseudoStateModel extends StateModel {

	private PseudostateKinds kind;

	public PseudoStateModel(String guid, String name, PseudostateKinds kind) {
		super(guid, name);
		this.kind = kind;
	}

	public PseudostateKinds getKind() {
		return kind;
	}

	public void appendString(String indent, StringBuilder str) {
		str.append(indent).append("Pseudostate: ").append(getGuid()).append(" / ").append(name).append('\n');
	}

	@Override
	public boolean isInitialState() {
		return kind == PseudostateKinds.Initial;
	}
}
