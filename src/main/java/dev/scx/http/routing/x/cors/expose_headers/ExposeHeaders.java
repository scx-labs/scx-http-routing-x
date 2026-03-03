package dev.scx.http.routing.x.cors.expose_headers;

import dev.scx.http.headers.ScxHttpHeaderName;

import static dev.scx.http.routing.x.cors.expose_headers.WildcardExposeHeaders.WILDCARD_EXPOSE_HEADERS;

public sealed interface ExposeHeaders permits ListExposeHeaders, WildcardExposeHeaders {

    static ListExposeHeaders of(ScxHttpHeaderName... headerNames) {
        return new ListExposeHeaders(headerNames);
    }

    static ListExposeHeaders of(String... headerNames) {
        return new ListExposeHeaders(headerNames);
    }

    static WildcardExposeHeaders ofWildcard() {
        return WILDCARD_EXPOSE_HEADERS;
    }

    /// 返回值可为 null, 表示没有 "Access-Control-Expose-Headers" 头
    String exposedHeadersString();

}
