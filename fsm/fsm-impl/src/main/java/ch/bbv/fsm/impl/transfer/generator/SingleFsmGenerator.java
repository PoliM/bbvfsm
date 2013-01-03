package ch.bbv.fsm.impl.transfer.generator;

import java.util.List;

import ch.bbv.fsm.impl.transfer.GeneratorException;
import ch.bbv.fsm.impl.transfer.model.RegionModel;
import ch.bbv.fsm.impl.transfer.model.StateMachineModel;
import ch.bbv.fsm.impl.transfer.model.StateModel;
import ch.bbv.fsm.impl.transfer.model.TransitionModel;
import ch.bbv.fsm.impl.transfer.model.TriggerModel;

public class SingleFsmGenerator {

	public static String model2Java(StateMachineModel model, String packageName, String fsmName)
			throws GeneratorException {

		StringBuilder str = new StringBuilder();

		RegionModel region = model.getSingleRegion();

		writeHead(packageName, str);
		writeImports(str);
		writeStatesEnum(str, region);
		writeEventsEnum(model, str);
		writeClassHeader(fsmName, str);
		writeConstructor(model, fsmName, str);
		writeCreateFsm(fsmName, str);
		str.append("}\n");
		return str.toString();
	}

	private static void writeHead(String packageName, StringBuilder str) {
		str.append("package ").append(packageName).append(";\n");
		str.append('\n');
		str.append("// =============================\n");
		str.append("// Generated file. DO NOT TOUCH!\n");
		str.append("// =============================\n");
		str.append('\n');
	}

	private static void writeCreateFsm(String fsmName, StringBuilder str) {
		str.append("\t@Override\n");
		str.append("\tprotected ").append(fsmName)
				.append(" createStateMachine(StateMachine<States, Events> driver) {\n");
		str.append("\t\treturn new ").append(fsmName).append("(driver);\n");
		str.append("\t}\n");
	}

	private static void writeConstructor(StateMachineModel model, String fsmName, StringBuilder str)
			throws GeneratorException {
		str.append("\tpublic ").append(fsmName).append("Definition() {\n");
		str.append("\t\tsuper(\"").append(fsmName).append("Definition\", States.")
				.append(model.computeInitialState().getName()).append(");\n");

		str.append("\t\t").append(fsmName).append(" proto = this.getPrototype();\n");

		RegionModel region = model.getSingleRegion();
		for (StateModel sourceState : region.getOrderedStates()) {
			if (sourceState.isInitialState()) {
				continue;
			}
			List<TransitionModel> transitions = region.getTransitionsForSource(sourceState.getGuid());

			for (TransitionModel trans : transitions) {
				StateModel targetState = region.getState(trans.getTarget());
				if (targetState.isFinalState()) {
					continue;
				}

				// source->target transition
				str.append("\t\tin(States.").append(sourceState.getName());
				TriggerModel trigger = model.triggerFor(trans.getTriggerGuid());
				if (trigger == null) {
					throw new GeneratorException("trigger must not be null. transGuid: " + trans.getGuid());
				}
				str.append(").on(Events.").append(trigger.getName());
				str.append(").goTo(States.").append(targetState.getName()).append(") //\n");

				// execute
				if (trans.getEffect() != null) {
					str.append("\t\t\t.execute(proto.").append(trans.getEffect()).append(") //\n");
				}
				// guard
				if (trans.getGuard() != null) {
					str.append("\t\t\t.onlyIf(proto.").append(trans.getGuard()).append(") //\n");
				}

				str.append("\t\t\t;\n");
			}
		}

		str.append("\t}\n");
		str.append('\n');
	}

	private static void writeClassHeader(String fsmName, StringBuilder str) {
		str.append("public class ").append(fsmName).append("Definition extends AbstractStateMachineDefinition<");
		str.append(fsmName).append(", States, Events> {\n");
		str.append('\n');
	}

	private static void writeImports(StringBuilder str) {
		str.append("import ch.bbv.fsm.StateMachine;\n");
		str.append("import ch.bbv.fsm.impl.AbstractStateMachineDefinition;\n");
		str.append('\n');
	}

	private static void writeEventsEnum(StateMachineModel model, StringBuilder str) {
		str.append("enum Events {\n");
		boolean isFirst = true;
		for (String triggerName : model.getSortedTriggerNames()) {
			if (isFirst) {
				isFirst = false;
			} else {
				str.append(", //\n");
			}
			str.append('\t').append(triggerName);
		}
		str.append("\n}\n");
		str.append('\n');
	}

	private static void writeStatesEnum(StringBuilder str, RegionModel region) {
		str.append("enum States {\n");
		boolean isFirst = true;
		for (StateModel sm : region.getOrderedStates()) {
			if (isFirst) {
				isFirst = false;
			} else {
				str.append(", //\n");
			}
			str.append('\t').append(sm.getName());
		}
		str.append("\n}\n");
		str.append('\n');
	}
}
