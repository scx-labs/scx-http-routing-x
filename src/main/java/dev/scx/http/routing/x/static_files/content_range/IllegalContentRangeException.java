package dev.scx.http.routing.x.static_files.content_range;

public final class IllegalContentRangeException extends RuntimeException {

    public IllegalContentRangeException(String message) {
        super(message);
    }

    public IllegalContentRangeException(String message, Throwable cause) {
        super(message, cause);
    }

}
