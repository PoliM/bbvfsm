package ch.bbv.fsm.impl.transfer.generator;

import java.util.List;

import ch.bbv.fsm.impl.transfer.GeneratorException;
import ch.bbv.fsm.impl.transfer.model.RegionModel;
import ch.bbv.fsm.impl.transfer.model.StateMachineModel;
import ch.bbv.fsm.impl.transfer.model.StateModel;
import ch.bbv.fsm.impl.transfer.model.TransitionModel;
import ch.bbv.fsm.impl.transfer.model.TriggerModel;

/**
 * This generator writes the code for a state machine.
 */
public final class SingleFsmGenerator {

  private SingleFsmGenerator() {}

  /**
   * Writes the model to Java code.
   *
   * @param model The model to generate.
   * @param packageName The package name of the Java code.
   * @param fsmName The name of the FSM.
   * @return The string with the Java code.
   * @throws GeneratorException in case something bad happens.
   */
  public static String model2Java(final StateMachineModel model, final String packageName,
      final String fsmName) throws GeneratorException {

    final StringBuilder str = new StringBuilder();

    final RegionModel region = model.getSingleRegion();

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

  private static void writeHead(final String packageName, final StringBuilder str) {
    str.append("package ").append(packageName).append(";\n");
    str.append('\n');
    str.append("// =============================\n");
    str.append("// Generated file. DO NOT TOUCH!\n");
    str.append("// =============================\n");
    str.append('\n');
  }

  private static void writeCreateFsm(final String fsmName, final StringBuilder str) {
    str.append("\t@Override\n");
    str.append("\tprotected ").append(fsmName)
        .append(" createStateMachine(StateMachine<States, Events> driver) {\n");
    str.append("\t\treturn new ").append(fsmName).append("(driver);\n");
    str.append("\t}\n");
  }

  private static void writeConstructor(final StateMachineModel model, final String fsmName,
      final StringBuilder str) throws GeneratorException {
    str.append("\tpublic ").append(fsmName).append("Definition() {\n");
    str.append("\t\tsuper(\"").append(fsmName).append("Definition\", States.")
        .append(model.computeInitialState().getName()).append(");\n");

    str.append("\t\t").append(fsmName).append(" proto = this.getPrototype();\n");

    final RegionModel region = model.getSingleRegion();
    for (final StateModel sourceState : region.getOrderedStates()) {
      if (sourceState.isInitialState()) {
        continue;
      }
      final List<TransitionModel> transitions =
          region.getTransitionsForSource(sourceState.getGuid());

      for (final TransitionModel trans : transitions) {
        final StateModel targetState = region.getState(trans.getTarget());

        // source->target transition
        str.append("\t\tin(States.").append(sourceState.getName());
        final TriggerModel trigger = model.triggerFor(trans.getTriggerGuid());
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

  private static void writeClassHeader(final String fsmName, final StringBuilder str) {
    str.append("public class ").append(fsmName)
        .append("Definition extends AbstractStateMachineDefinition<");
    str.append(fsmName).append(", States, Events> {\n");
    str.append('\n');
  }

  private static void writeImports(final StringBuilder str) {
    str.append("import ch.bbv.fsm.StateMachine;\n");
    str.append("import ch.bbv.fsm.impl.AbstractStateMachineDefinition;\n");
    str.append('\n');
  }

  private static void writeEventsEnum(final StateMachineModel model, final StringBuilder str) {
    str.append("enum Events {\n");
    boolean isFirst = true;
    for (final String triggerName : model.getSortedTriggerNames()) {
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

  private static void writeStatesEnum(final StringBuilder str, final RegionModel region) {
    str.append("enum States {\n");
    boolean isFirst = true;
    for (final StateModel sm : region.getOrderedStates()) {
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
