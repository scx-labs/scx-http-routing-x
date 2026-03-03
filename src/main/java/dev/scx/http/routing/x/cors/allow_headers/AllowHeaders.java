package dev.scx.http.routing.x.cors.allow_headers;

import dev.scx.http.headers.ScxHttpHeaderName;

import static dev.scx.http.routing.x.cors.allow_headers.ReflectAllowHeaders.REFLECT_ALLOW_HEADERS;
import static dev.scx.http.routing.x.cors.allow_headers.WildcardAllowHeaders.WILDCARD_ALLOW_HEADERS;

public sealed interface AllowHeaders permits ListAllowHeaders, ReflectAllowHeaders, WildcardAllowHeaders {

    static ListAllowHeaders of(ScxHttpHeaderName... headerNames) {
        return new ListAllowHeaders(headerNames);
    }

    static ListAllowHeaders of(String... headerNames) {
        return new ListAllowHeaders(headerNames);
    }

    static ReflectAllowHeaders ofReflect() {
        return REFLECT_ALLOW_HEADERS;
    }

    static WildcardAllowHeaders ofWildcard() {
        return WILDCARD_ALLOW_HEADERS;
    }

    /// 输入可为 null, 表示没有 "Access-Control-Request-Headers" 头
    /// 返回值可为 null, 表示没有 "Access-Control-Allow-Headers" 头
    String allowedHeadersString(String requestHeadersString);

}
