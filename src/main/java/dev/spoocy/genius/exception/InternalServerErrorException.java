package dev.spoocy.genius.exception;

/**
 * @author Spoocy99 | GitHub: Spoocy99
 */

public class InternalServerErrorException extends GeniusApiException {

    public InternalServerErrorException() {
        super();
    }

    public InternalServerErrorException(String message) {
        super(message);
    }

    public InternalServerErrorException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
