package dev.scx.http.routing.x.static_files.range;

import dev.scx.http.exception.ScxHttpException;
import dev.scx.http.status_code.ScxHttpStatusCode;

import static dev.scx.http.status_code.HttpStatusCode.BAD_REQUEST;

/// IllegalRangeException
///
/// @author scx567888
/// @version 0.0.1
public final class IllegalRangeException extends RuntimeException implements ScxHttpException {

    public IllegalRangeException(String message) {
        super(message);
    }

    public IllegalRangeException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public ScxHttpStatusCode statusCode() {
        return BAD_REQUEST;
    }

}
