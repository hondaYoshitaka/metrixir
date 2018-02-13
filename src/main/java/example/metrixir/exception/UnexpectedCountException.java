package example.metrixir.exception;

/**
 * @author y_honda_
 */
public class UnexpectedCountException extends RuntimeException {

    private static final long serialVersionUID = -4907916127463224702L;

    public UnexpectedCountException(final int actual,
                                    final int expected) {
        super("expected count " + actual + ", but actual: " + expected);
    }
}
