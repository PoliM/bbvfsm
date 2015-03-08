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

/**
 * This is the SAX based parser for XMI state machines.
 */
public final class XmiModelReader extends DefaultHandler {

  private final XmiReaderStateMachine xmiReaderFsm;

  private final List<Exception> exceptions = Lists.newLinkedList();

  private XmiModelReader(final String fsmName) {
    final XmiReaderStateMachineDefinition fsmDef = new XmiReaderStateMachineDefinition();

    fsmDef.addEventHandler(new StateMachineEventAdapter<XmiReaderStateMachine, States, Events>() {

      @Override
      public void onTransitionThrowsException(
          final TransitionExceptionEventArgs<XmiReaderStateMachine, States, Events> arg) {
        exceptions.add(arg.getException());
      }

      @Override
      public void onExceptionThrown(
          final ExceptionEventArgs<XmiReaderStateMachine, States, Events> arg) {
        exceptions.add(arg.getException());
      }
    });
    xmiReaderFsm = fsmDef.createPassiveStateMachine("XmiReaderFSM");
    xmiReaderFsm.setFsmNameToLookFor(fsmName);
    xmiReaderFsm.start();
  }

  /**
   * Parses an XMI of a state machine and creates the corresponding model.
   *
   * @param resource The XMI resource URL.
   * @param fsmName The name of the state machine.
   * @return The parsed model of the state machine.
   * @throws GeneratorException In case something bad happens.
   */
  public static StateMachineModel readFromUrl(final URL resource, final String fsmName)
      throws GeneratorException {

    try {
      final SAXParserFactory factory = SAXParserFactory.newInstance();
      final SAXParser saxParser = factory.newSAXParser();

      final XmiModelReader xmiModelReader = new XmiModelReader(fsmName);

      saxParser.parse(resource.openStream(), xmiModelReader);

      if (xmiModelReader.exceptions.isEmpty()) {
        return xmiModelReader.getStateMachineModel();
      }
      throw new GeneratorException("Exceptions occured. Here is the first",
          xmiModelReader.exceptions.get(0));
    } catch (ParserConfigurationException | SAXException | IOException e) {
      throw new GeneratorException("Parsing esception", e);
    }
  }

  @Override
  public void startElement(final String uri, final String localName, final String qName,
      final Attributes attributes) throws SAXException {
    xmiReaderFsm.fire(Events.StartElement, qName, attributes);
  }

  @Override
  public void endElement(final String uri, final String localName, final String qName)
      throws SAXException {
    xmiReaderFsm.fire(Events.EndElement, qName);
  }

  private StateMachineModel getStateMachineModel() {
    return xmiReaderFsm.getStateMachineModel();
  }

}
