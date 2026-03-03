package dev.scx.http.routing.x.cors.allow_methods;

public sealed interface AllowMethods permits ListAllowMethods, ReflcetAllowMethods, WildcardAllowMethods {

    /// 输入可为 null, 表示没有 "Access-Control-Request-Method" 头
    /// 返回值可为 null, 表示没有 "Access-Control-Allow-Methods" 头
     String allowedMethodsString(String requestMethodString);

}
