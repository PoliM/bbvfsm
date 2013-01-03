package ch.bbv.fsm.impl.transfer.xmi;

import org.xml.sax.Attributes;

import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.impl.AbstractStateMachine;
import ch.bbv.fsm.impl.transfer.model.PseudostateKinds;
import ch.bbv.fsm.impl.transfer.model.RegionModel;
import ch.bbv.fsm.impl.transfer.model.StateMachineModel;
import ch.bbv.fsm.impl.transfer.model.TransitionModel;

public class XmiReaderStateMachine extends AbstractStateMachine<XmiReaderStateMachine, States, Events> {

	private String fsmNameToLookFor;

	private StateMachineModel stateMachine;

	private RegionModel currentRegion;

	private TransitionModel currentTransition;

	protected XmiReaderStateMachine(StateMachine<States, Events> driver) {
		super(driver);
	}

	public XmiReaderStateMachine() {
		super(null);
	}

	public StateMachineModel getStateMachineModel() {
		return stateMachine;
	}

	public void setFsmNameToLookFor(String fsmNameToLookFor) {
		this.fsmNameToLookFor = fsmNameToLookFor;
	}

	public boolean isPackageStartElementWithCorrectName(String qName, Attributes attributes) {
		return checkElement(qName, attributes, "packagedElement", "uml:Package", fsmNameToLookFor);
	}

	public boolean isPackageEndElement(String qName) {
		return checkElement(qName, null, "packagedElement", null, null);
	}

	public Void createTriggerModel(String qName, Attributes attributes) {
		stateMachine.addNewTrigger(attributes.getValue("xmi:id"), attributes.getValue("name"));
		return null;
	}

	public boolean isTriggerStartElement(String qName, Attributes attributes) {
		return checkElement(qName, attributes, "packagedElement", "uml:Trigger", null);
	}

	public boolean isTriggerEndElement(String qName) {
		return checkElement(qName, null, "packagedElement", null, null);
	}

	public boolean isStateMachineStartElement(String qName, Attributes attributes) {
		return checkElement(qName, attributes, "packagedElement", "uml:StateMachine", null);
	}

	public boolean isStateMachineEndElement(String qName) {
		return checkElement(qName, null, "packagedElement", null, null);
	}

	public Void createStateMachineModel(String qName, Attributes attributes) {
		stateMachine = new StateMachineModel(attributes.getValue("xmi:id"), attributes.getValue("name"));
		return null;
	}

	public Void addAndEnterRegionModel(String qName, Attributes attributes) {
		currentRegion = stateMachine.addNewRegion(attributes.getValue("xmi:id"), attributes.getValue("name"));
		return null;
	}

	public boolean isRegionStartElement(String qName, Attributes attributes) {
		return checkElement(qName, attributes, "region", "uml:Region", null);
	}

	public Void exitRegionModel(String qName) {
		currentRegion = null;
		return null;
	}

	public boolean isRegionEndElement(String qName) {
		return checkElement(qName, null, "region", null, null);
	}

	public Void addStateModel(String qName, Attributes attributes) {
		currentRegion.addNewState(attributes.getValue("xmi:id"), attributes.getValue("name"));
		return null;
	}

	public boolean isStateStartElement(String qName, Attributes attributes) {
		return checkElement(qName, attributes, "subvertex", "uml:State", null);
	}

	public Void addPseudoStateModel(String qName, Attributes attributes) {
		currentRegion.addNewPseudoState(attributes.getValue("xmi:id"), attributes.getValue("name"),
				PseudostateKinds.Initial);
		return null;
	}

	public boolean isPseudoStateStartElement(String qName, Attributes attributes) {
		return checkElement(qName, attributes, "subvertex", "uml:Pseudostate", null);
	}

	public Void addFinalStateModel(String qName, Attributes attributes) {
		currentRegion.addNewFinalState(attributes.getValue("xmi:id"), attributes.getValue("name"));
		return null;
	}

	public boolean isFinalStateStartElement(String qName, Attributes attributes) {
		return checkElement(qName, attributes, "subvertex", "uml:FinalState", null);
	}

	public Void addTransitionModel(String qName, Attributes attributes) {
		currentTransition = currentRegion.addNewTransition(attributes.getValue("xmi:id"),
				attributes.getValue("source"), attributes.getValue("target"));
		return null;
	}

	public boolean isTransitionStartElement(String qName, Attributes attributes) {
		return checkElement(qName, attributes, "transition", "uml:Transition", null);
	}

	public Void exitTransitionModel(String qName) {
		currentTransition = null;
		return null;
	}

	public boolean isTransitionEndElement(String qName) {
		return checkElement(qName, null, "transition", null, null);
	}

	public Void addEffectToTransition(String qName, Attributes attributes) {
		currentTransition.setEffect(attributes.getValue("body"));
		return null;
	}

	public boolean isTransitionEffectElement(String qName, Attributes attributes) {
		return checkElement(qName, attributes, "effect", "uml:OpaqueBehavior", null);
	}

	public Void setTriggerGuidToTransition(String qName, Attributes attributes) {
		currentTransition.setTriggerGuid(attributes.getValue("xmi:idref"));
		return null;
	}

	public boolean isTransitionTriggerElement(String qName, Attributes attributes) {
		return checkElement(qName, attributes, "trigger", null, null);
	}

	public boolean isGuardStartElement(String qName, Attributes attributes) {
		return checkElement(qName, attributes, "guard", "uml:Constraint", null);
	}

	public boolean isGuardEndElement(String qName) {
		return checkElement(qName, null, "guard", null, null);
	}

	public boolean isSpecificationStartElement(String qName, Attributes attributes) {
		return checkElement(qName, attributes, "specification", "uml:OpaqueExpression", null);
	}

	public Void setTransitionGuard(String qName, Attributes attributes) {
		currentTransition.setGuard(attributes.getValue("body"));
		return null;
	}

	private boolean checkElement(String qName, Attributes attributes, String expectedQName, String expectedType,
			String expectedName) {
		boolean isPackageElement = expectedQName.equals(qName);

		boolean isType = true;
		if (expectedType != null) {
			String xmiType = attributes.getValue("xmi:type");
			isType = expectedType.equals(xmiType);
		}

		boolean isName = true;
		if (expectedName != null) {
			String name = attributes.getValue("name");
			isName = expectedName.equals(name);
		}
		return isPackageElement && isType && isName;
	}
}
