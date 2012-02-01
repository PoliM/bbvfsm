/**
 * 
 */
package ch.bbv.fsm.dsl;

/**
 * Exception that can be thrown when an {@link ch.bbv.fsm.action.EmbeddedAction} class doesn't is not defined correctly.
 * This means that the action class <br>
 * <br>
 * - is it an static class, or <br>
 * - it is a regular class., i.e. there is no declaring class, and <br>
 * - there is a default constructor.
 * 
 * 
 * @author Mario Martinez (bbv Software Services AG)
 * 
 */
public class IllegalActionClassDefinitionException extends RuntimeException {

	/**
	 *  
	 */
	private static final long serialVersionUID = -8381081501090239905L;

	/**
	 * 
	 */
	public IllegalActionClassDefinitionException() {
	}

	/**
	 * @param message
	 *            The exception detailed message.
	 * @param cause
	 *            The cause.
	 */
	public IllegalActionClassDefinitionException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
