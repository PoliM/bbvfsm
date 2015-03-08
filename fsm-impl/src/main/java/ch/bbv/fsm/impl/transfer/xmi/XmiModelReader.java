package ch.bbv.fsm.impl.transfer.xmi;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import ch.bbv.fsm.events.ExceptionEventArgs;
import ch.bbv.fsm.events.StateMachineEventAdapter;
import ch.bbv.fsm.events.TransitionExceptionEventArgs;
import ch.bbv.fsm.impl.transfer.GeneratorException;
import ch.bbv.fsm.impl.transfer.model.StateMachineModel;

import com.google.common.collect.Lists;

public class XmiModelReader extends DefaultHandler {

	private XmiReaderStateMachine xmiReaderFsm;

	private List<Exception> exceptions = Lists.newLinkedList();

	private XmiModelReader(String fsmName) {
		XmiReaderStateMachineDefinition fsmDef = new XmiReaderStateMachineDefinition();

		fsmDef.addEventHandler(new StateMachineEventAdapter<XmiReaderStateMachine, States, Events>() {

			@Override
			public void onTransitionThrowsException(
					TransitionExceptionEventArgs<XmiReaderStateMachine, States, Events> arg) {
				exceptions.add(arg.getException());
			}

			@Override
			public void onExceptionThrown(ExceptionEventArgs<XmiReaderStateMachine, States, Events> arg) {
				exceptions.add(arg.getException());
			}
		});
		xmiReaderFsm = fsmDef.createPassiveStateMachine("XmiReaderFSM");
		xmiReaderFsm.setFsmNameToLookFor(fsmName);
		xmiReaderFsm.start();
	}

	public static StateMachineModel readFromUrl(URL resource, String fsmName) throws GeneratorException {

		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			XmiModelReader xmiModelReader = new XmiModelReader(fsmName);

			saxParser.parse(resource.openStream(), xmiModelReader);

			if (xmiModelReader.exceptions.isEmpty()) {
				return xmiModelReader.getStateMachineModel();
			}
			throw new GeneratorException("Exceptions occured. Here is the first", xmiModelReader.exceptions.get(0));
		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new GeneratorException("Parsing esception", e);
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		xmiReaderFsm.fire(Events.StartElement, qName, attributes);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		xmiReaderFsm.fire(Events.EndElement, qName);
	}

	private StateMachineModel getStateMachineModel() {
		return xmiReaderFsm.getStateMachineModel();
	}

}
