package objects.exceptions;

/**
 * Created by wlasc on 07.06.2016.
 */
public class BadDataException extends Exception {


    public BadDataException() {
        super();
    }

    public BadDataException(String message) {
        super(message);
    }

    public BadDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public BadDataException(Throwable cause) {
        super(cause);
    }
}
