package dev.scx.http.routing.x.cors.expose_headers;

import dev.scx.http.headers.ScxHttpHeaderName;

import java.util.Arrays;
import java.util.stream.Collectors;

public final class ListExposeHeaders implements ExposeHeaders {

    private final String exposedHeadersString;

    public ListExposeHeaders(ScxHttpHeaderName... headerNames) {
        this.exposedHeadersString = Arrays.stream(headerNames)
            .map(ScxHttpHeaderName::value)
            .collect(Collectors.joining(", "));
    }

    public ListExposeHeaders(String... headerNames) {
        this.exposedHeadersString = String.join(", ", headerNames);
    }

    @Override
    public String exposedHeaders() {
        return this.exposedHeadersString;
    }

}
