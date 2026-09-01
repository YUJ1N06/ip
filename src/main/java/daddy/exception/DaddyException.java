package daddy.exception;

/**
 * Represents an error that can be explained directly to a Daddy user.
 */
public class DaddyException extends Exception {
    private static final long serialVersionUID = 1L;

    /** Creates an error with a user-friendly correction message. */
    public DaddyException(String message) {
        super(message);
    }
}
