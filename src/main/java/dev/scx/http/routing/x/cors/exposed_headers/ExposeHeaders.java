package dev.scx.http.routing.x.cors.exposed_headers;

public sealed interface ExposeHeaders permits ListExposeHeaders, WildcardExposeHeaders {

    /// 返回值可为 null, 表示没有 "Access-Control-Expose-Headers" 头
    String exposedHeadersString();

}
