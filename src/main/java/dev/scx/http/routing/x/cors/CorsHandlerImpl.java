package dev.scx.http.routing.x.cors;

import dev.scx.http.ScxHttpServerResponse;
import dev.scx.http.method.HttpMethod;
import dev.scx.http.routing.RoutingContext;
import dev.scx.http.routing.x.cors.allow_headers.AllowHeaders;
import dev.scx.http.routing.x.cors.allow_methods.AllowMethods;
import dev.scx.http.routing.x.cors.allow_origin.AllowOrigin;
import dev.scx.http.routing.x.cors.allow_origin.WildcardAllowOrigin;
import dev.scx.http.routing.x.cors.expose_headers.ExposeHeaders;

import static dev.scx.http.headers.HttpHeaderName.*;

// todo 配置阶段需要保证不能出现非法组合.

/// CorsHandler
///
/// @author scx567888
/// @version 0.0.1
public class CorsHandlerImpl implements CorsHandler {

    private AllowOrigin allowOrigin;
    private AllowMethods allowMethods;
    private AllowHeaders allowHeaders;
    private ExposeHeaders exposeHeaders;
    private boolean allowCredentials;
    private Long maxAgeSeconds;

    public CorsHandlerImpl() {
        this.allowOrigin = new WildcardAllowOrigin();
        this.allowMethods = AllowMethods.ofWildcard();
        this.allowHeaders = AllowHeaders.ofWildcard();
        this.exposeHeaders = ExposeHeaders.ofWildcard();
        this.allowCredentials = false;
        this.maxAgeSeconds = 9999L;
    }

    @Override
    public CorsHandler allowOrigin(AllowOrigin allowOrigin) {
        this.allowOrigin = allowOrigin;
        return this;
    }

    @Override
    public CorsHandler allowMethods(AllowMethods allowMethods) {
        this.allowMethods = allowMethods;
        return this;
    }

    @Override
    public CorsHandler allowHeaders(AllowHeaders allowHeaders) {
        this.allowHeaders = allowHeaders;
        return this;
    }

    @Override
    public CorsHandler exposeHeaders(ExposeHeaders exposeHeaders) {
        this.exposeHeaders = exposeHeaders;
        return this;
    }

    @Override
    public CorsHandlerImpl allowCredentials(boolean allowCredentials) {
        this.allowCredentials = allowCredentials;
        return this;
    }

    @Override
    public CorsHandlerImpl maxAgeSeconds(Long maxAgeSeconds) {
        this.maxAgeSeconds = maxAgeSeconds;
        return this;
    }

    @Override
    public void apply(RoutingContext context) throws Throwable {
        var request = context.request();
        var response = request.response();

        var origin = context.request().getHeader(ORIGIN);

        // 1, 校验是否是 cors 请求.
        if (origin == null) {
            // 不是 CORS 请求 - 什么都不做 直接 next
            context.next();
            return;
        }

        // 2, 验证 origin
        var xxx = this.allowOrigin.xxxOrigin(origin);

        // 验证失败
        if (xxx == null) {
            context.next();
            return;
        }

        // 3, 验证成功
        // 3.1, 写入 Allow-Origin
        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, xxx);
        response.setHeader(VARY, "Origin");

        // 3.2, 写入 Allow-Credentials
        if (allowCredentials) {
            response.setHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        }

        // 4, 判断是否是预检请求
        var requestMethod = request.getHeader(ACCESS_CONTROL_REQUEST_METHOD);

        // 4.1, 是预检请求
        if (request.method() == HttpMethod.OPTIONS && requestMethod != null) {

            var allowedMethodsString = this.allowMethods.allowedMethodsString(requestMethod);
            if (allowedMethodsString != null) {
                response.setHeader(ACCESS_CONTROL_ALLOW_METHODS, allowedMethodsString);
            }

            var requestHeaders = request.getHeader(ACCESS_CONTROL_REQUEST_HEADERS);
            var allowedHeadersString = this.allowHeaders.allowedHeadersString(requestHeaders);
            if (allowedHeadersString != null) {
                response.setHeader(ACCESS_CONTROL_ALLOW_HEADERS, allowedHeadersString);
            }

            if (this.maxAgeSeconds != null) {
                response.setHeader(ACCESS_CONTROL_MAX_AGE, String.valueOf(maxAgeSeconds));
            }

            response.statusCode(204).send();

        } else {
            // 不是预检请求
            var exposedHeadersString = this.exposeHeaders.exposedHeadersString();
            if (exposedHeadersString != null) {
                response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, exposedHeadersString);
            }

            context.next();
        }

    }

}
