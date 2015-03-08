package ch.bbv.fsm.impl.transfer.model;

/**
 * The model for the final state.
 */
public class FinalStateModel extends StateModel {

  /**
   * Initialize the object.
   *
   * @param guid The GUID of the model element.
   * @param name The name of the model element.
   */
  public FinalStateModel(final String guid, final String name) {
    super(guid, name);
  }

  @Override
  public void appendString(final String indent, final StringBuilder str) {
    str.append(indent).append("FinalState: ").append(getGuid()).append(" / ").append(name)
        .append('\n');
  }

  @Override
  public boolean isFinalState() {
    return true;
  }
}
