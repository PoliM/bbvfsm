package ch.bbv.fsm.impl.documentation;

import java.io.StringReader;

import org.apache.commons.scxml.io.SCXMLParser;
import org.apache.commons.scxml.model.SCXML;
import org.apache.commons.scxml.model.State;
import org.junit.Assert;
import org.junit.Test;

import ch.bbv.fsm.HistoryType;
import ch.bbv.fsm.impl.SimpleStateMachine;
import ch.bbv.fsm.impl.SimpleStateMachineDefinition;
import ch.bbv.fsm.impl.StatesAndEvents.Events;
import ch.bbv.fsm.impl.StatesAndEvents.States;
import ch.bbv.fsm.impl.documentation.scxml.SCXMLVisitor;

public class SCXMLGeneratorTest {

	// @Test
	// public void testSCXMLGeneratorWhenTransitionWithGuardsThenCorrectGuards()
	// throws IOException, SAXException {
	//
	// SCXMLStateMachineDefinition definition = new SCXMLStateMachineDefinition(
	// "simple", States.A);
	// definition.in(States.A).on(Events.B).goTo(States.B);
	// definition.in(States.A).on(Events.C).goTo(States.C)
	// .onlyIf(TrueFunction.class);
	//
	// // Action
	// StringBuffer result = definition
	// .generateDocumentation(new SCXMLGenerator());
	// SCXML parsedObject = (SCXML) SCXMLParser.newInstance().parse(
	// new StringReader(result.toString()));
	//
	// System.out.println(result);
	//
	// InternalState state_A = (InternalState) parsedObject.getChildren().get(
	// States.A.toString());
	// Transition transition_A_B = (Transition) state_A.getTransitionsList()
	// .get(0);
	//
	// Assert.assertEquals("Invalid number of States.", 3, parsedObject
	// .getChildren().size());
	// Assert.assertNotNull("Condition is null.", transition_A_B.getCond());
	// Assert.assertEquals("true", transition_A_B.getCond());
	//
	// }

	@Test
	public void testSCXMLGeneratorWhenSubStatesThenCorrectSubStates()
			throws Exception {

		SimpleStateMachineDefinition<States, Events> definition = new SimpleStateMachineDefinition<States, Events>(
				"simple", States.A);
		definition.in(States.A).on(Events.B).goTo(States.B);
		definition.in(States.A).on(Events.C).goTo(States.C);
		definition.in(States.B).on(Events.A).goTo(States.A);
		definition.in(States.D).on(Events.D).goTo(States.D);
		definition.in(States.D).on(Events.C1b).goTo(States.E);

		definition.defineHierarchyOn(States.C, States.D, HistoryType.DEEP,
				States.D, States.E);

		// Action
		SCXMLVisitor<SimpleStateMachine<States, Events>, States, Events> visitor = new SCXMLVisitor<SimpleStateMachine<States, Events>, States, Events>();
		definition.traverseModel(visitor);
		StringBuffer result = visitor.getScxml();

		SCXML parsedObject = (SCXML) SCXMLParser.newInstance().parse(
				new StringReader(result.toString()));

		State state_C = (State) parsedObject.getChildren().get(
				States.C.toString());
		Assert.assertEquals(
				"Invalid number of States in the Root InternalState/Scope.", 3,
				parsedObject.getChildren().size());
		Assert.assertEquals("Invalid number of States in the SuperScope_C", 2,
				state_C.getChildren().size());
		Assert.assertNotNull("Composite is null.", state_C);
		Assert.assertTrue("State_C is not a Superstate.", state_C.isComposite());
	}

	@Test
	public void testSCXMLGeneratorWhenCascadeSubStatesThenCorrectSubStates()
			throws Exception {

		// Arrange)
		SimpleStateMachineDefinition<States, Events> definition = new SimpleStateMachineDefinition<States, Events>(
				"simple", States.A);

		// root scope (A)
		definition.in(States.A).on(Events.B).goTo(States.B);

		// super state (B->B1,B1)
		definition.in(States.B1).on(Events.B2).goTo(States.B2);
		definition.in(States.B2).on(Events.C).goTo(States.C);
		definition.defineHierarchyOn(States.B, States.B1, HistoryType.DEEP,
				States.B1, States.B2, States.C);

		// super state (C->C1,C2)
		definition.in(States.C1).on(Events.C1b).goTo(States.C2);
		definition.defineHierarchyOn(States.C, States.C1, HistoryType.DEEP,
				States.C1, States.C2);

		// Action
		SCXMLVisitor<SimpleStateMachine<States, Events>, States, Events> visitor = new SCXMLVisitor<SimpleStateMachine<States, Events>, States, Events>();
		definition.traverseModel(visitor);
		StringBuffer result = visitor.getScxml();

		SCXML parsedObject = (SCXML) SCXMLParser.newInstance().parse(
				new StringReader(result.toString()));

		State state_B = (State) parsedObject.getChildren().get(
				States.B.toString());
		State state_C = (State) state_B.getChildren().get(States.C.toString());

		// Assert
		Assert.assertEquals("Invalid number of States in the Root Scope.", 2,
				parsedObject.getChildren().size());

		Assert.assertNotNull("Composite is null.", state_B);
		Assert.assertEquals("Invalid number of States in the SuperScope_B", 3,
				state_B.getChildren().size());
		Assert.assertTrue("State_B is not a Superstate.", state_B.isComposite());

		Assert.assertNotNull("Composite is null.", state_C);
		Assert.assertEquals("Invalid number of States in the SuperScope_C", 2,
				state_C.getChildren().size());
		Assert.assertTrue("State_C is not a Superstate.", state_C.isComposite());

		Assert.assertEquals("State_B is not the Superstate of State_C.",
				States.B.toString(), state_C.getParent().getId());

	}
}
