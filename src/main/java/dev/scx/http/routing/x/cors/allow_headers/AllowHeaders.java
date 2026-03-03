package dev.scx.http.routing.x.cors.allow_headers;

public sealed interface AllowHeaders permits ListAllowHeaders, ReflectAllowHeaders, WildcardAllowHeaders {

    /// 输入可为 null, 表示没有 "Access-Control-Request-Headers" 头
    /// 返回值可为 null, 表示没有 "Access-Control-Allow-Headers" 头
    String allowedHeadersString(String requestHeadersString);

}
