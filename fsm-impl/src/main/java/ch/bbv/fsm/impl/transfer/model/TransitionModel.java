package ch.bbv.fsm.impl.transfer.model;

public class TransitionModel {

	private String guid;

	private String source;

	private String target;

	private String effect;

	private String triggerGuid;

	private String guard;

	public TransitionModel(String guid, String source, String target) {
		this.guid = guid;
		this.source = source;
		this.target = target;
	}

	public void appendString(StateMachineModel stateMachineModel, String indent, StringBuilder str) {
		str.append(indent).append("Transition: ").append(source).append(" -> ").append(target).append('\n');
		if (effect != null) {
			str.append(indent).append('\t').append("Effect: ").append(effect).append('\n');
		}
		if (triggerGuid != null) {
			str.append(indent).append('\t').append("Trigger: ")
					.append(stateMachineModel.triggerFor(triggerGuid).getName()).append('\n');
		}
		if (guard != null) {
			str.append(indent).append('\t').append("Guard: ").append(guard).append('\n');
		}
	}

	public void setEffect(String value) {
		this.effect = value;
	}

	public void setTriggerGuid(String guid) {
		this.triggerGuid = guid;
	}

	public String getGuid() {
		return guid;
	}

	public String getSource() {
		return source;
	}

	public String getTarget() {
		return target;
	}

	public String getTriggerGuid() {
		return triggerGuid;
	}

	public String getEffect() {
		return effect;
	}

	public void setGuard(String value) {
		guard = value;
	}

	public String getGuard() {
		return guard;
	}
}
