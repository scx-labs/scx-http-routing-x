package dev.scx.http.routing.x.cors.allow_methods;

public interface AllowMethods {

    /// 输入可为 null, 表示没有 "Access-Control-Request-Method" 头
    /// 返回值可为 null, 表示没有 "Access-Control-Allow-Methods" 头
     String allowedMethodsString(String requestMethodString);

}
