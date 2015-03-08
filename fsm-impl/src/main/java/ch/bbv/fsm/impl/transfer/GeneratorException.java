package ch.bbv.fsm.impl.transfer;

/**
 * Common exception for generator problems.
 */
public class GeneratorException extends Exception {

  private static final long serialVersionUID = 1L;

  /**
   * With message.
   *
   * @param msg The message.
   */
  public GeneratorException(final String msg) {
    super(msg);
  }

  /**
   * With message and cause.
   *
   * @param msg The message.
   * @param cause The cause.
   */
  public GeneratorException(final String msg, final Throwable cause) {
    super(msg, cause);
  }
}
