package dev.scx.http.routing.x.cors;

import dev.scx.http.method.HttpMethod;
import dev.scx.http.routing.RoutingContext;
import dev.scx.http.routing.x.cors.allow_headers.AllowHeaders;
import dev.scx.http.routing.x.cors.allow_headers.WildcardAllowHeaders;
import dev.scx.http.routing.x.cors.allow_methods.AllowMethods;
import dev.scx.http.routing.x.cors.allow_methods.WildcardAllowMethods;
import dev.scx.http.routing.x.cors.allow_origin.AllowOrigin;
import dev.scx.http.routing.x.cors.expose_headers.ExposeHeaders;
import dev.scx.http.routing.x.cors.expose_headers.WildcardExposeHeaders;

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
        // todo 这里现在的组合应该不合法.
        this.allowOrigin = AllowOrigin.ofWildcard();
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
        if (allowCredentials) {
            if (allowMethods instanceof WildcardAllowMethods) {
                throw new IllegalArgumentException("can not use 'WildcardAllowMethods' with 'allowCredentials=true'.");
            }
        }
        this.allowMethods = allowMethods;
        return this;
    }

    @Override
    public CorsHandler allowHeaders(AllowHeaders allowHeaders) {
        if (allowCredentials) {
            if (allowHeaders instanceof WildcardAllowHeaders) {
                throw new IllegalArgumentException("can not use 'WildcardAllowHeaders' with 'allowCredentials=true'.");
            }
        }
        this.allowHeaders = allowHeaders;
        return this;
    }

    @Override
    public CorsHandler exposeHeaders(ExposeHeaders exposeHeaders) {
        if (allowCredentials) {
            if (exposeHeaders instanceof WildcardExposeHeaders) {
                throw new IllegalArgumentException("can not use 'WildcardExposeHeaders' with 'allowCredentials=true'.");
            }
        }
        this.exposeHeaders = exposeHeaders;
        return this;
    }

    @Override
    public CorsHandlerImpl allowCredentials(boolean allowCredentials) {
        // todo 这里需要反向检查.
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
        var allowedOrigin = this.allowOrigin.allowedOrigin(origin);

        // 验证失败
        if (allowedOrigin == null) {
            context.next();
            return;
        }

        // 3, 验证成功
        // 3.1, 写入 Allow-Origin
        response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, allowedOrigin);
        response.setHeader(VARY, "Origin");

        // 3.2, 写入 Allow-Credentials
        if (allowCredentials) {
            response.setHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        }

        // 4, 判断是否是预检请求
        var requestMethod = request.getHeader(ACCESS_CONTROL_REQUEST_METHOD);

        // 4.1, 是预检请求
        if (request.method() == HttpMethod.OPTIONS && requestMethod != null) {

            var allowedMethods = this.allowMethods.allowedMethods(requestMethod);
            if (allowedMethods != null) {
                response.setHeader(ACCESS_CONTROL_ALLOW_METHODS, allowedMethods);
            }

            var requestHeaders = request.getHeader(ACCESS_CONTROL_REQUEST_HEADERS);
            var allowedHeaders = this.allowHeaders.allowedHeaders(requestHeaders);
            if (allowedHeaders != null) {
                response.setHeader(ACCESS_CONTROL_ALLOW_HEADERS, allowedHeaders);
            }

            if (this.maxAgeSeconds != null) {
                response.setHeader(ACCESS_CONTROL_MAX_AGE, String.valueOf(maxAgeSeconds));
            }

            response.statusCode(204).send();

        } else {
            // 不是预检请求
            var exposedHeaders = this.exposeHeaders.exposedHeaders();
            if (exposedHeaders != null) {
                response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, exposedHeaders);
            }

            context.next();
        }

    }

}
