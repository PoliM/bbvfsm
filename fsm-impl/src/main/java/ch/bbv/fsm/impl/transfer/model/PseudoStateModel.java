package ch.bbv.fsm.impl.transfer.model;

public class PseudoStateModel extends StateModel {

  private final PseudostateKinds kind;

  /**
   * Initialize the object.
   *
   * @param guid The GUID of the model element.
   * @param name The name of the model element.
   * @param kind The type of pseudo state.
   */
  public PseudoStateModel(final String guid, final String name, final PseudostateKinds kind) {
    super(guid, name);
    this.kind = kind;
  }

  public PseudostateKinds getKind() {
    return kind;
  }

  @Override
  public void appendString(final String indent, final StringBuilder str) {
    str.append(indent).append("Pseudostate: ").append(getGuid()).append(" / ").append(name)
        .append('\n');
  }

  @Override
  public boolean isInitialState() {
    return kind == PseudostateKinds.Initial;
  }
}
