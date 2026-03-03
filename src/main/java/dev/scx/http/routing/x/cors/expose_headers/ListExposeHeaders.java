package dev.scx.http.routing.x.cors.expose_headers;

import dev.scx.http.headers.ScxHttpHeaderName;

import java.util.LinkedHashSet;

public final class ListExposeHeaders implements ExposeHeaders {

    private final String exposedHeadersString;

    public ListExposeHeaders(ScxHttpHeaderName... headerNames) {
        if (headerNames == null) {
            throw new NullPointerException("headerNames must not be null");
        }
        if (headerNames.length == 0) {
            throw new IllegalArgumentException("headerNames must not be empty");
        }

        var set = new LinkedHashSet<String>();

        for (var h : headerNames) {
            if (h == null) {
                throw new NullPointerException("headerName must not be null");
            }

            String trimmed = h.value().trim();

            if (trimmed.isEmpty()) {
                throw new IllegalArgumentException("headerName must not be blank");
            }

            if ("*".equals(trimmed)) {
                throw new IllegalArgumentException(
                    "'*' is not allowed in ListExposeHeaders, use WildcardExposeHeaders instead");
            }

            if (trimmed.contains(",")) {
                throw new IllegalArgumentException(
                    "headerName must not contain ',' : " + trimmed);
            }

            set.add(trimmed);
        }

        this.exposedHeadersString = String.join(", ", set);
    }

    @Override
    public String exposedHeaders() {
        return this.exposedHeadersString;
    }

}
