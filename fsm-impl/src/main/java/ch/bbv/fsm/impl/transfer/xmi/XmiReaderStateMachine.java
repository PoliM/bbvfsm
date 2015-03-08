package ch.bbv.fsm.impl.transfer.xmi;

import org.xml.sax.Attributes;

import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.impl.AbstractStateMachine;
import ch.bbv.fsm.impl.transfer.model.PseudostateKinds;
import ch.bbv.fsm.impl.transfer.model.RegionModel;
import ch.bbv.fsm.impl.transfer.model.StateMachineModel;
import ch.bbv.fsm.impl.transfer.model.TransitionModel;

/**
 * This is the state machine for the XMI parser.
 */
public class XmiReaderStateMachine extends
    AbstractStateMachine<XmiReaderStateMachine, States, Events> {

  private String fsmNameToLookFor;

  private StateMachineModel stateMachine;

  private RegionModel currentRegion;

  private TransitionModel currentTransition;

  /**
   * Initialize object.
   *
   * @param driver The state machine driver.
   */
  protected XmiReaderStateMachine(final StateMachine<States, Events> driver) {
    super(driver);
  }

  /**
   * Initialize object without driver.
   */
  public XmiReaderStateMachine() {
    super(null);
  }

  /**
   * Gets the created state machine.
   * 
   * @return The state machine.
   */
  public StateMachineModel getStateMachineModel() {
    return stateMachine;
  }

  /**
   * Sets the name of the state machine to look for.
   * 
   * @param fsmNameToLookFor The name of the state machine to look for
   */
  public void setFsmNameToLookFor(final String fsmNameToLookFor) {
    this.fsmNameToLookFor = fsmNameToLookFor;
  }

  /**
   * Checks if a certain element is a package start element.
   *
   * @param qName The XML Qname
   * @param attributes The attributes of the XML node.
   *
   * @return true if it is a package start element.
   */
  public boolean isPackageStartElementWithCorrectName(final String qName,
      final Attributes attributes) {
    return checkElement(qName, attributes, "packagedElement", "uml:Package", fsmNameToLookFor);
  }

  /**
   * Checks if a certain element is a package end element.
   *
   * @param qName The XML Qname
   *
   * @return true if it is a package end element.
   */
  public boolean isPackageEndElement(final String qName) {
    return checkElement(qName, null, "packagedElement", null, null);
  }

  /**
   * Adds a trigger to the current state machine.
   *
   * @param qName The XML Qname
   * @param attributes The attributes of the XML node.
   */
  public void addTrigger(final String qName, final Attributes attributes) {
    stateMachine.addNewTrigger(attributes.getValue("xmi:id"), attributes.getValue("name"));
  }

  /**
   * Checks if a certain element is a trigger start element.
   *
   * @param qName The XML Qname
   * @param attributes The attributes of the XML node.
   *
   * @return true if it is a trigger start element.
   */
  public boolean isTriggerStartElement(final String qName, final Attributes attributes) {
    return checkElement(qName, attributes, "packagedElement", "uml:Trigger", null);
  }

  /**
   * Checks if a certain element is a trigger end element.
   *
   * @param qName The XML Qname
   *
   * @return true if it is a trigger end element.
   */
  public boolean isTriggerEndElement(final String qName) {
    return checkElement(qName, null, "packagedElement", null, null);
  }

  /**
   * Checks if a certain element is a state machine start.
   *
   * @param qName The XML Qname
   * @param attributes The attributes of the XML node.
   *
   * @return true if it is a state machine start.
   */
  public boolean isStateMachineStartElement(final String qName, final Attributes attributes) {
    return checkElement(qName, attributes, "packagedElement", "uml:StateMachine", null);
  }

  /**
   * Checks if a certain element is a state machine end.
   *
   * @param qName The XML Qname
   *
   * @return true if it is a state machine end.
   */
  public boolean isStateMachineEndElement(final String qName) {
    return checkElement(qName, null, "packagedElement", null, null);
  }

  /**
   * Adds a new state machine.
   *
   * @param qName The XML Qname
   * @param attributes The attributes of the XML node.
   */
  public void addStateMachine(final String qName, final Attributes attributes) {
    stateMachine =
        new StateMachineModel(attributes.getValue("xmi:id"), attributes.getValue("name"));
  }

  /**
   * Adds a new region.
   *
   * @param qName The XML Qname
   * @param attributes The attributes of the XML node.
   */
  public void addRegion(final String qName, final Attributes attributes) {
    currentRegion =
        stateMachine.addNewRegion(attributes.getValue("xmi:id"), attributes.getValue("name"));
  }

  /**
   * Checks if a certain element is a region start.
   *
   * @param qName The XML Qname
   * @param attributes The attributes of the XML node.
   *
   * @return true if it is a region start.
   */
  public boolean isRegionStartElement(final String qName, final Attributes attributes) {
    return checkElement(qName, attributes, "region", "uml:Region", null);
  }

  /**
   * Ends the current region.
   *
   * @param qName The XML Qname
   */
  public void exitRegion(final String qName) {
    currentRegion = null;
  }

  /**
   * Checks if a certain element is a region end.
   *
   * @param qName The XML Qname
   *
   * @return true if it is a region end.
   */
  public boolean isRegionEndElement(final String qName) {
    return checkElement(qName, null, "region", null, null);
  }

  /**
   * Adds a state to the current region.
   *
   * @param qName The XML Qname
   * @param attributes The attributes of the XML node.
   */
  public void addState(final String qName, final Attributes attributes) {
    currentRegion.addNewState(attributes.getValue("xmi:id"), attributes.getValue("name"));
  }

  /**
   * Checks if a certain element is a state start.
   *
   * @param qName The XML Qname
   * @param attributes The attributes of the XML node.
   *
   * @return true if it is a state start.
   */
  public boolean isStateStartElement(final String qName, final Attributes attributes) {
    return checkElement(qName, attributes, "subvertex", "uml:State", null);
  }

  /**
   * Adds a pseudo state to the current region.
   *
   * @param qName The XML Qname
   * @param attributes The attributes of the XML node.
   */
  public void addPseudoStateModel(final String qName, final Attributes attributes) {
    currentRegion.addNewPseudoState(attributes.getValue("xmi:id"), attributes.getValue("name"),
        PseudostateKinds.Initial);
  }

  /**
   * Checks if a certain element is a pseudo state start.
   *
   * @param qName The XML Qname
   * @param attributes The attributes of the XML node.
   *
   * @return true if it is a pseudo state start.
   */
  public boolean isPseudoStateStartElement(final String qName, final Attributes attributes) {
    return checkElement(qName, attributes, "subvertex", "uml:Pseudostate", null);
  }

  /**
   * Adds a final state.
   *
   * @param qName The XML Qname
   * @param attributes The attributes of the XML node.
   */
  public void addFinalStateModel(final String qName, final Attributes attributes) {
    currentRegion.addNewFinalState(attributes.getValue("xmi:id"), attributes.getValue("name"));
  }

  /**
   * Checks if a certain element is a final state start.
   *
   * @param qName The XML Qname
   * @param attributes The attributes of the XML node.
   *
   * @return true if it is a final state start.
   */
  public boolean isFinalStateStartElement(final String qName, final Attributes attributes) {
    return checkElement(qName, attributes, "subvertex", "uml:FinalState", null);
  }

  /**
   * Starts a new transition.
   *
   * @param qName The XML Qname
   * @param attributes The attributes of the XML node.
   */
  public void addTransition(final String qName, final Attributes attributes) {
    currentTransition =
        currentRegion.addNewTransition(attributes.getValue("xmi:id"),
            attributes.getValue("source"), attributes.getValue("target"));
  }

  /**
   * Checks if a certain element is a transition start.
   *
   * @param qName The XML Qname
   * @param attributes The attributes of the XML node.
   *
   * @return true if it is a transition start.
   */
  public boolean isTransitionStartElement(final String qName, final Attributes attributes) {
    return checkElement(qName, attributes, "transition", "uml:Transition", null);
  }

  /**
   * Exits the current transition.
   *
   * @param qName The XML Qname
   */
  public void exitTransition(final String qName) {
    currentTransition = null;
  }

  /**
   * Checks if a certain element is a transition end.
   *
   * @param qName The XML Qname
   *
   * @return true if it is a transition end.
   */
  public boolean isTransitionEndElement(final String qName) {
    return checkElement(qName, null, "transition", null, null);
  }

  /**
   * Sets an effect to the current transition.
   *
   * @param qName The XML Qname
   * @param attributes The attributes of the XML node.
   */
  public void setEffectToTransition(final String qName, final Attributes attributes) {
    currentTransition.setEffect(attributes.getValue("body"));
  }

  /**
   * Checks if a certain element is a transition effect start.
   *
   * @param qName The XML Qname
   * @param attributes The attributes of the XML node.
   *
   * @return true if it is a transition effect start.
   */
  public boolean isTransitionEffectStartElement(final String qName, final Attributes attributes) {
    return checkElement(qName, attributes, "effect", "uml:OpaqueBehavior", null);
  }

  /**
   * Sets a trigger guard for the current transition.
   *
   * @param qName The XML Qname
   * @param attributes The attributes of the XML node.
   */
  public void setTriggerGuidToTransition(final String qName, final Attributes attributes) {
    currentTransition.setTriggerGuid(attributes.getValue("xmi:idref"));
  }

  /**
   * Checks if a certain element is a transition trigger.
   *
   * @param qName The XML Qname
   * @param attributes The attributes of the XML node.
   *
   * @return true if it is a trigger.
   */
  public boolean isTransitionTriggerStartElement(final String qName, final Attributes attributes) {
    return checkElement(qName, attributes, "trigger", null, null);
  }

  /**
   * Checks if a certain element is a guard start.
   *
   * @param qName The XML Qname
   * @param attributes The attributes of the XML node.
   *
   * @return true if it is a guard.
   */
  public boolean isGuardStartElement(final String qName, final Attributes attributes) {
    return checkElement(qName, attributes, "guard", "uml:Constraint", null);
  }

  /**
   * Checks if a certain element is a guard end.
   *
   * @param qName The XML Qname.
   * @return True if it is a guard.
   */
  public boolean isGuardEndElement(final String qName) {
    return checkElement(qName, null, "guard", null, null);
  }

  /**
   * Checks if a certain element is a specification.
   *
   * @param qName The XML Qname
   * @param attributes The attributes of the XML node.
   *
   * @return true if it is a specification.
   */
  public boolean isSpecificationStartElement(final String qName, final Attributes attributes) {
    return checkElement(qName, attributes, "specification", "uml:OpaqueExpression", null);
  }

  /**
   * Sets the guard for the current transition.
   *
   * @param qName The XML Qname
   * @param attributes The attributes of the XML node.
   */
  public void setTransitionGuard(final String qName, final Attributes attributes) {
    currentTransition.setGuard(attributes.getValue("body"));
  }

  private boolean checkElement(final String qName, final Attributes attributes,
      final String expectedQName, final String expectedType, final String expectedName) {
    final boolean isPackageElement = expectedQName.equals(qName);

    boolean isType = true;
    if (expectedType != null) {
      final String xmiType = attributes.getValue("xmi:type");
      isType = expectedType.equals(xmiType);
    }

    boolean isName = true;
    if (expectedName != null) {
      final String name = attributes.getValue("name");
      isName = expectedName.equals(name);
    }
    return isPackageElement && isType && isName;
  }
}
