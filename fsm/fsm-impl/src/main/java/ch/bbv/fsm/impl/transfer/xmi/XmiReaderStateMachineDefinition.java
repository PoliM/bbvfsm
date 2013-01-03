package ch.bbv.fsm.impl.transfer.xmi;

//=============================
//Generated file. DO NOT TOUCH!
//=============================

import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.impl.AbstractStateMachineDefinition;

enum States {
	Final, //
	InGuard, //
	InPackage, //
	InRegion, //
	InSM, //
	InTransition, //
	InTrigger, //
	Initial, //
	WaitingForPackage
}

enum Events {
	EndElement, //
	StartElement
}

public class XmiReaderStateMachineDefinition extends
		AbstractStateMachineDefinition<XmiReaderStateMachine, States, Events> {

	public XmiReaderStateMachineDefinition() {
		super("XmiReaderStateMachineDefinition", States.WaitingForPackage);
		XmiReaderStateMachine proto = this.getPrototype();
		in(States.InGuard).on(Events.StartElement).goTo(States.InGuard) //
				.execute(proto.setTransitionGuard(null, null)) //
				.onlyIf(proto.isSpecificationStartElement(null, null)) //
		;
		in(States.InGuard).on(Events.EndElement).goTo(States.InTransition) //
				.onlyIf(proto.isGuardEndElement(null)) //
		;
		in(States.InPackage).on(Events.StartElement).goTo(States.InTrigger) //
				.execute(proto.addTrigger(null, null)) //
				.onlyIf(proto.isTriggerStartElement(null, null)) //
		;
		in(States.InPackage).on(Events.EndElement).goTo(States.Final) //
				.onlyIf(proto.isPackageEndElement(null)) //
		;
		in(States.InPackage).on(Events.StartElement).goTo(States.InSM) //
				.execute(proto.addStateMachine(null, null)) //
				.onlyIf(proto.isStateMachineStartElement(null, null)) //
		;
		in(States.InRegion).on(Events.StartElement).goTo(States.InTransition) //
				.execute(proto.addTransition(null, null)) //
				.onlyIf(proto.isTransitionStartElement(null, null)) //
		;
		in(States.InRegion).on(Events.StartElement).goTo(States.InRegion) //
				.execute(proto.addPseudoStateModel(null, null)) //
				.onlyIf(proto.isPseudoStateStartElement(null, null)) //
		;
		in(States.InRegion).on(Events.EndElement).goTo(States.InSM) //
				.execute(proto.exitRegion(null)) //
				.onlyIf(proto.isRegionEndElement(null)) //
		;
		in(States.InRegion).on(Events.StartElement).goTo(States.InRegion) //
				.execute(proto.addState(null, null)) //
				.onlyIf(proto.isStateStartElement(null, null)) //
		;
		in(States.InRegion).on(Events.StartElement).goTo(States.InRegion) //
				.execute(proto.addFinalStateModel(null, null)) //
				.onlyIf(proto.isFinalStateStartElement(null, null)) //
		;
		in(States.InSM).on(Events.StartElement).goTo(States.InRegion) //
				.execute(proto.addRegion(null, null)) //
				.onlyIf(proto.isRegionStartElement(null, null)) //
		;
		in(States.InSM).on(Events.EndElement).goTo(States.InPackage) //
				.onlyIf(proto.isStateMachineEndElement(null)) //
		;
		in(States.InTransition).on(Events.StartElement).goTo(States.InTransition) //
				.execute(proto.setTriggerGuidToTransition(null, null)) //
				.onlyIf(proto.isTransitionTriggerStartElement(null, null)) //
		;
		in(States.InTransition).on(Events.StartElement).goTo(States.InGuard) //
				.onlyIf(proto.isGuardStartElement(null, null)) //
		;
		in(States.InTransition).on(Events.EndElement).goTo(States.InRegion) //
				.execute(proto.exitTransition(null)) //
				.onlyIf(proto.isTransitionEndElement(null)) //
		;
		in(States.InTransition).on(Events.StartElement).goTo(States.InTransition) //
				.execute(proto.setEffectToTransition(null, null)) //
				.onlyIf(proto.isTransitionEffectStartElement(null, null)) //
		;
		in(States.InTrigger).on(Events.EndElement).goTo(States.InPackage) //
				.onlyIf(proto.isTriggerEndElement(null)) //
		;
		in(States.WaitingForPackage).on(Events.StartElement).goTo(States.InPackage) //
				.onlyIf(proto.isPackageStartElementWithCorrectName(null, null)) //
		;
	}

	@Override
	protected XmiReaderStateMachine createStateMachine(StateMachine<States, Events> driver) {
		return new XmiReaderStateMachine(driver);
	}
}
