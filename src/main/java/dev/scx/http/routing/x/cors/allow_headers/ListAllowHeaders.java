package dev.scx.http.routing.x.cors.allow_headers;

import dev.scx.http.headers.ScxHttpHeaderName;

import java.util.Arrays;
import java.util.stream.Collectors;

public final class ListAllowHeaders implements AllowHeaders {

    private final String allowedHeadersString;

    public ListAllowHeaders(ScxHttpHeaderName... headerNames) {
        this.allowedHeadersString = Arrays.stream(headerNames)
            .map(ScxHttpHeaderName::value)
            .collect(Collectors.joining(", "));
    }

    public ListAllowHeaders(String... headerNames) {
        this.allowedHeadersString = String.join(", ", headerNames);
    }

    @Override
    public String allowedHeaders(String requestHeadersString) {
        return this.allowedHeadersString;
    }

}
