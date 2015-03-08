package ch.bbv.fsm.impl.transfer.model;

public class TriggerModel implements Comparable<TriggerModel> {

  protected String guid;

  protected String name;

  /**
   * Initialize the object.
   *
   * @param guid The GUID of the model element.
   * @param name The name of the model element.
   */
  public TriggerModel(final String guid, final String name) {
    this.guid = guid;
    this.name = name;
  }

  public void appendString(final String indent, final StringBuilder str) {
    str.append(indent).append("Trigger: ").append(guid).append(" / ").append(name).append('\n');
  }

  @Override
  public int compareTo(final TriggerModel o) {
    return name.compareTo(o.name);
  }

  public String getName() {
    return name;
  }
}
