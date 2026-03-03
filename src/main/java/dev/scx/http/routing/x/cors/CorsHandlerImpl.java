package dev.scx.http.routing.x.cors;

import dev.scx.http.ScxHttpServerResponse;
import dev.scx.http.exception.ForbiddenException;
import dev.scx.http.headers.ScxHttpHeaderName;
import dev.scx.http.method.HttpMethod;
import dev.scx.http.method.ScxHttpMethod;
import dev.scx.http.routing.RoutingContext;
import dev.scx.http.routing.x.cors.allow_headers.AllowHeaders;
import dev.scx.http.routing.x.cors.allow_headers.WildcardAllowHeaders;
import dev.scx.http.routing.x.cors.allow_methods.AllowMethods;
import dev.scx.http.routing.x.cors.allow_methods.WildcardAllowMethods;
import dev.scx.http.routing.x.cors.allow_origin.AllowOrigin;
import dev.scx.http.routing.x.cors.allow_origin.WildcardAllowOrigin;
import dev.scx.http.routing.x.cors.expose_headers.ExposeHeaders;
import dev.scx.http.routing.x.cors.expose_headers.WildcardExposeHeaders;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Objects;

import static dev.scx.http.headers.HttpHeaderName.*;
import static java.util.Collections.addAll;

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
    private long maxAgeSeconds;

    public CorsHandlerImpl() {
        this.allowOrigin = new WildcardAllowOrigin();
        this.allowMethods = AllowMethods.ofWildcard();
        this.allowHeaders = AllowHeaders.ofWildcard();
        this.exposeHeaders = ExposeHeaders.ofWildcard();
        this.allowCredentials = false;
        this.maxAgeSeconds = 9999;
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
    public CorsHandlerImpl maxAgeSeconds(long maxAgeSeconds) {
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

        // todo
        if (isValidOrigin(origin)) {
            var accessControlRequestMethod = request.getHeader(ACCESS_CONTROL_REQUEST_METHOD);
            if (request.method() == HttpMethod.OPTIONS && accessControlRequestMethod != null) {
                // 预检 请求
                addCredentialsAndOriginHeader(response, origin);
                if (allowedMethodsString != null) {
                    response.setHeader(ACCESS_CONTROL_ALLOW_METHODS, allowedMethodsString);
                }
                if (allowedHeadersString != null) {
                    response.setHeader(ACCESS_CONTROL_ALLOW_HEADERS, allowedHeadersString);
                } else {
                    if (request.headers().contains(ACCESS_CONTROL_REQUEST_HEADERS)) {
                        // 回显请求标头
                        response.setHeader(ACCESS_CONTROL_ALLOW_HEADERS, request.getHeader(ACCESS_CONTROL_REQUEST_HEADERS));
                    }
                }
                if (maxAgeSeconds != null) {
                    response.setHeader(ACCESS_CONTROL_MAX_AGE, maxAgeSeconds);
                }

                response.statusCode(204).send();

            } else {
                addCredentialsAndOriginHeader(response, origin);
                if (exposedHeadersString != null) {
                    response.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, exposedHeadersString);
                }
                context.next();
            }
        } else {
            //向客户端报告 CORS 错误
            throw new ForbiddenException("CORS Rejected - Invalid origin");
        }
    }

    private void addCredentialsAndOriginHeader(ScxHttpServerResponse response, String origin) {
        if (allowCredentials) {
            response.setHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
            // Must be exact origin (not '*') in case of credentials
            response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, origin);
        } else {
            // Can be '*' too
            response.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, getAllowedOrigin(origin));
        }
    }

    private boolean isValidOrigin(String origin) {
        // "*" 的意思是全部 origin
        if (starOrigin()) {
            return true;
        }

        // 否则我们进行完全匹配
        for (var allowedOrigin : origins) {
            if (allowedOrigin.equals(origin)) {
                return true;
            }
        }

        return false;
    }

    private String getAllowedOrigin(String origin) {
        if (starOrigin()) {
            return "*";
        }
        return origin;
    }

}
