package ch.bbv.fsm.impl.transfer.xmi;

// =============================
// Generated file. DO NOT TOUCH!
// =============================

import org.xml.sax.Attributes;

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


public class XmiReaderStateMachineDefinition extends AbstractStateMachineDefinition<XmiReaderStateMachine, States, Events> {

  public XmiReaderStateMachineDefinition() {
    super("XmiReaderStateMachineDefinition", States.WaitingForPackage);
    in(States.InGuard).on(Events.StartElement).goTo(States.InGuard) //
        .execute(XmiReaderStateMachine::setTransitionGuard)//
        .onlyIf((sm, p) -> sm.isSpecificationStartElement((String) p[0], (Attributes) p[1])) //
    ;
    in(States.InGuard).on(Events.EndElement).goTo(States.InTransition) //
        .onlyIf((sm, p) -> sm.isGuardEndElement((String) p[0])) //
    ;
    in(States.InPackage).on(Events.StartElement).goTo(States.InTrigger) //
        .execute(XmiReaderStateMachine::addTrigger)//
        .onlyIf((sm, p) -> sm.isTriggerStartElement((String) p[0], (Attributes) p[1])) //
    ;
    in(States.InPackage).on(Events.EndElement).goTo(States.Final) //
        .onlyIf((sm, p) -> sm.isPackageEndElement((String) p[0])) //
    ;
    in(States.InPackage).on(Events.StartElement).goTo(States.InSM) //
        .execute(XmiReaderStateMachine::addStateMachine)//
        .onlyIf((sm, p) -> sm.isStateMachineStartElement((String) p[0], (Attributes) p[1])) //
    ;
    in(States.InRegion).on(Events.StartElement).goTo(States.InTransition) //
        .execute(XmiReaderStateMachine::addTransition)//
        .onlyIf((sm, p) -> sm.isTransitionStartElement((String) p[0], (Attributes) p[1])) //
    ;
    in(States.InRegion).on(Events.StartElement).goTo(States.InRegion) //
        .execute(XmiReaderStateMachine::addPseudoStateModel)//
        .onlyIf((sm, p) -> sm.isPseudoStateStartElement((String) p[0], (Attributes) p[1])) //
    ;
    in(States.InRegion).on(Events.EndElement).goTo(States.InSM) //
        .execute(XmiReaderStateMachine::exitRegion)//
        .onlyIf((sm, p) -> sm.isRegionEndElement((String) p[0])) //
    ;
    in(States.InRegion).on(Events.StartElement).goTo(States.InRegion) //
        .execute(XmiReaderStateMachine::addState)//
        .onlyIf((sm, p) -> sm.isStateStartElement((String) p[0], (Attributes) p[1])) //
    ;
    in(States.InRegion).on(Events.StartElement).goTo(States.InRegion) //
        .execute(XmiReaderStateMachine::addFinalStateModel)//
        .onlyIf((sm, p) -> sm.isFinalStateStartElement((String) p[0], (Attributes) p[1])) //
    ;
    in(States.InSM).on(Events.StartElement).goTo(States.InRegion) //
        .execute(XmiReaderStateMachine::addRegion)//
        .onlyIf((sm, p) -> sm.isRegionStartElement((String) p[0], (Attributes) p[1])) //
    ;
    in(States.InSM).on(Events.EndElement).goTo(States.InPackage) //
        .onlyIf((sm, p) -> sm.isStateMachineEndElement((String) p[0])) //
    ;
    in(States.InTransition).on(Events.StartElement).goTo(States.InTransition) //
        .execute(XmiReaderStateMachine::setTriggerGuidToTransition)//
        .onlyIf((sm, p) -> sm.isTransitionTriggerStartElement((String) p[0], (Attributes) p[1])) //
    ;
    in(States.InTransition).on(Events.StartElement).goTo(States.InGuard) //
        .onlyIf((sm, p) -> sm.isGuardStartElement((String) p[0], (Attributes) p[1])) //
    ;
    in(States.InTransition).on(Events.EndElement).goTo(States.InRegion) //
        .execute(XmiReaderStateMachine::exitTransition)//
        .onlyIf((sm, p) -> sm.isTransitionEndElement((String) p[0])) //
    ;
    in(States.InTransition).on(Events.StartElement).goTo(States.InTransition) //
        .execute(XmiReaderStateMachine::setEffectToTransition)//
        .onlyIf((sm, p) -> sm.isTransitionEffectStartElement((String) p[0], (Attributes) p[1])) //
    ;
    in(States.InTrigger).on(Events.EndElement).goTo(States.InPackage) //
        .onlyIf((sm, p) -> sm.isTriggerEndElement((String) p[0])) //
    ;
    in(States.WaitingForPackage).on(Events.StartElement).goTo(States.InPackage)
    //
        .onlyIf((sm, p) -> sm.isPackageStartElementWithCorrectName((String) p[0], (Attributes) p[1])) //
    ;
  }

  @Override
  protected XmiReaderStateMachine createStateMachine(final StateMachine<States, Events> driver) {
    return new XmiReaderStateMachine(driver);
  }
}
