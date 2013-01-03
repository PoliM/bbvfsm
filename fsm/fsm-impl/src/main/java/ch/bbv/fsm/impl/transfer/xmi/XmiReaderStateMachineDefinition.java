package ch.bbv.fsm.impl.transfer.xmi;

import ch.bbv.fsm.StateMachine;
import ch.bbv.fsm.impl.AbstractStateMachineDefinition;

enum Events {
	StartElement, EndElement
}

enum States {
	WaitingForPackage, InPackage, InSM, InRegion, InTransition, End, InTrigger, InGuard
}

class XmiReaderStateMachineDefinition extends AbstractStateMachineDefinition<XmiReaderStateMachine, States, Events> {

	public XmiReaderStateMachineDefinition() {
		super("XmiReaderStateMachineDefinition", States.WaitingForPackage);

		XmiReaderStateMachine proto = this.getPrototype();

		in(States.WaitingForPackage).on(Events.StartElement).goTo(States.InPackage) //
				.onlyIf(proto.isPackageStartElementWithCorrectName(null, null));
		in(States.InPackage).on(Events.StartElement).goTo(States.InSM) //
				.execute(proto.createStateMachineModel(null, null)) //
				.onlyIf(proto.isStateMachineStartElement(null, null));
		in(States.InPackage).on(Events.StartElement).goTo(States.InTrigger) //
				.execute(proto.createTriggerModel(null, null)) //
				.onlyIf(proto.isTriggerStartElement(null, null));
		in(States.InTrigger).on(Events.EndElement).goTo(States.InPackage) //
				.onlyIf(proto.isTriggerEndElement(null));
		in(States.InPackage).on(Events.EndElement).goTo(States.End) //
				.onlyIf(proto.isPackageEndElement(null));
		in(States.InSM).on(Events.StartElement).goTo(States.InRegion) //
				.execute(proto.addAndEnterRegionModel(null, null)) //
				.onlyIf(proto.isRegionStartElement(null, null));
		in(States.InSM).on(Events.EndElement).goTo(States.InPackage) //
				.onlyIf(proto.isStateMachineEndElement(null));
		in(States.InRegion).on(Events.StartElement).goTo(States.InRegion) //
				.execute(proto.addStateModel(null, null)) //
				.onlyIf(proto.isStateStartElement(null, null));
		in(States.InRegion).on(Events.StartElement).goTo(States.InRegion) //
				.execute(proto.addPseudoStateModel(null, null)) //
				.onlyIf(proto.isPseudoStateStartElement(null, null));
		in(States.InRegion).on(Events.StartElement).goTo(States.InRegion) //
				.execute(proto.addFinalStateModel(null, null)) //
				.onlyIf(proto.isFinalStateStartElement(null, null));
		in(States.InRegion).on(Events.StartElement).goTo(States.InTransition) //
				.execute(proto.addTransitionModel(null, null)) //
				.onlyIf(proto.isTransitionStartElement(null, null));
		in(States.InRegion).on(Events.EndElement).goTo(States.InSM) //
				.execute(proto.exitRegionModel(null)) //
				.onlyIf(proto.isRegionEndElement(null));
		in(States.InTransition).on(Events.EndElement).goTo(States.InRegion) //
				.execute(proto.exitTransitionModel(null)) //
				.onlyIf(proto.isTransitionEndElement(null));
		in(States.InTransition).on(Events.StartElement).goTo(States.InTransition) //
				.execute(proto.addEffectToTransition(null, null)) //
				.onlyIf(proto.isTransitionEffectElement(null, null));
		in(States.InTransition).on(Events.StartElement).goTo(States.InTransition) //
				.execute(proto.setTriggerGuidToTransition(null, null)) //
				.onlyIf(proto.isTransitionTriggerElement(null, null));
		in(States.InTransition).on(Events.StartElement).goTo(States.InGuard) //
				.onlyIf(proto.isGuardStartElement(null, null));
		in(States.InGuard).on(Events.EndElement).goTo(States.InTransition) //
				.onlyIf(proto.isGuardEndElement(null));
		in(States.InGuard).on(Events.StartElement).goTo(States.InGuard) //
				.execute(proto.setTransitionGuard(null, null)) //
				.onlyIf(proto.isSpecificationStartElement(null, null));
	}

	@Override
	protected XmiReaderStateMachine createStateMachine(final StateMachine<States, Events> driver) {
		return new XmiReaderStateMachine(driver);
	}
}