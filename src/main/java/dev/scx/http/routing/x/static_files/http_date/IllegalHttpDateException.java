package dev.scx.http.routing.x.static_files.http_date;

import dev.scx.http.exception.ScxHttpException;
import dev.scx.http.status_code.ScxHttpStatusCode;

import static dev.scx.http.status_code.HttpStatusCode.BAD_REQUEST;

/// IllegalContentLengthException
///
/// @author scx567888
/// @version 0.0.1
public final class IllegalHttpDateException extends RuntimeException implements ScxHttpException {

    public IllegalHttpDateException(String message) {
        super(message);
    }

    public IllegalHttpDateException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public ScxHttpStatusCode statusCode() {
        return BAD_REQUEST;
    }

}
