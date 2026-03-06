package dev.scx.http.routing.x.static_files.content_range;

import dev.scx.http.exception.ScxHttpException;
import dev.scx.http.status_code.ScxHttpStatusCode;

import static dev.scx.http.status_code.HttpStatusCode.BAD_REQUEST;

public final class IllegalContentRangeException extends RuntimeException implements ScxHttpException {

    public IllegalContentRangeException(String message) {
        super(message);
    }

    public IllegalContentRangeException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public ScxHttpStatusCode statusCode() {
        return BAD_REQUEST;
    }

}
