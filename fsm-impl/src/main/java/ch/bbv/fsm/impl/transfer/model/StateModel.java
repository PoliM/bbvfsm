package ch.bbv.fsm.impl.transfer.model;

/**
 * Base class for all model elements.
 */
public class StateModel implements Comparable<StateModel> {

  private final String guid;

  protected String name;

  /**
   * Initialize the object.
   * 
   * @param guid The GUID of the model element.
   * @param name The name of the model element.
   */
  public StateModel(final String guid, final String name) {
    this.guid = guid;
    this.name = name;
  }

  public void appendString(final String indent, final StringBuilder str) {
    str.append(indent).append("State: ").append(getGuid()).append(" / ").append(name).append('\n');
  }

  @Override
  public int compareTo(final StateModel o) {
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

  public boolean isFinalState() {
    return false;
  }
}
