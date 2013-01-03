package ch.bbv.fsm.impl.transfer.xmi;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringWhiteSpace;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.nio.charset.Charset;

import org.junit.Test;

import ch.bbv.fsm.impl.transfer.model.StateMachineModel;

import com.google.common.io.Resources;

public class XmiModelReaderTest {

	@Test
	public void readingXmiEmitsCorrectModel() throws Exception {
		String expectedStateMachineString = Resources.toString(this.getClass().getResource("/SimpleModel.txt"),
				Charset.forName("windows-1252"));

		StateMachineModel sm = XmiModelReader.readFromUrl(this.getClass().getResource("/StateDiagramTests.xmi"),
				"Simple");

		assertThat(sm, is(notNullValue()));

		System.out.println(expectedStateMachineString);
		System.out.println("-------------------");
		System.out.println(sm.toString());

		assertThat(sm.toString(), is(equalToIgnoringWhiteSpace(expectedStateMachineString)));
	}
}
