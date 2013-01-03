package ch.bbv.fsm.impl.transfer.generator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringWhiteSpace;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.Test;

import ch.bbv.fsm.impl.transfer.GeneratorException;
import ch.bbv.fsm.impl.transfer.model.StateMachineModel;
import ch.bbv.fsm.impl.transfer.xmi.XmiModelReader;

import com.google.common.io.Files;
import com.google.common.io.Resources;

public class SingleFsmGeneratorTest {

	@Test
	public void convertSimpleEmitsCorrectJavaCode() throws Exception {
		// Arrange
		String expectedJavaCodeString = Resources.toString(
				this.getClass().getResource("/ch/bbv/fsm/impl/transfer/testfsms/SimpleFsmDefinition.java"),
				Charset.forName("windows-1252"));

		StateMachineModel stateMachineModel = XmiModelReader.readFromUrl(
				this.getClass().getResource("/StateDiagramTests.xmi"), "Simple");

		// Act
		String javaCode = SingleFsmGenerator.model2Java(stateMachineModel, "ch.bbv.fsm.impl.transfer.testfsms",
				"SimpleFsm");

		System.out.println(expectedJavaCodeString);
		System.out.println("-------------------");
		System.out.println(javaCode);

		if ("marcopoli".equals(System.getProperty("user.name"))) {
			Files.write(
					javaCode,
					new File(
							"C:/Users/MarcoPoli/Documents/Projects/BBV_FSM/trunk/fsm/fsm-impl/src/test/resources/ch/bbv/fsm/impl/transfer/testfsms/output.txt"),
					Charset.forName("windows-1252"));
		}

		// Assert
		assertThat(javaCode, is(equalToIgnoringWhiteSpace(expectedJavaCodeString)));
	}

	@Test
	public void generateParser() throws GeneratorException, IOException {
		StateMachineModel stateMachineModel = XmiModelReader.readFromUrl(
				this.getClass().getResource("/StateDiagramTests.xmi"), "XmiModelReader");
		String javaCode = SingleFsmGenerator.model2Java(stateMachineModel, "ch.bbv.fsm.impl.transfer.testfsms",
				"SimpleFsm");
		System.out.println(javaCode);
		if ("marcopoli".equals(System.getProperty("user.name"))) {
			Files.write(
					javaCode,
					new File(
							"C:/Users/MarcoPoli/Documents/Projects/BBV_FSM/trunk/fsm/fsm-impl/src/test/resources/ch/bbv/fsm/impl/transfer/testfsms/output2.txt"),
					Charset.forName("windows-1252"));
		}
	}
}
