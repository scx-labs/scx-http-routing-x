package dev.scx.http.routing.x.cors.allow_headers;

import dev.scx.http.headers.ScxHttpHeaderName;

import java.util.LinkedHashSet;

public final class ListAllowHeaders implements AllowHeaders {

    private final String allowedHeadersString;

    public ListAllowHeaders(ScxHttpHeaderName... headerNames) {
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
                    "'*' is not allowed in ListAllowHeaders, use WildcardAllowHeaders instead");
            }

            if (trimmed.contains(",")) {
                throw new IllegalArgumentException(
                    "headerName must not contain ',' : " + trimmed);
            }

            set.add(trimmed);
        }

        this.allowedHeadersString = String.join(", ", set);
    }

    @Override
    public String allowedHeaders(String requestHeadersString) {
        return this.allowedHeadersString;
    }

}
