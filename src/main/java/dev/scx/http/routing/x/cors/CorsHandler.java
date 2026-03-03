package dev.scx.http.routing.x.cors;

import dev.scx.function.Function1Void;
import dev.scx.http.headers.ScxHttpHeaderName;
import dev.scx.http.method.ScxHttpMethod;
import dev.scx.http.routing.RoutingContext;
import dev.scx.http.routing.x.cors.allow_headers.AllowHeaders;
import dev.scx.http.routing.x.cors.allow_methods.AllowMethods;

// todo 支持 anyxxx
// Origins 有 any

public interface CorsHandler extends Function1Void<RoutingContext, Throwable> {

    CorsHandler allowedMethods(AllowMethods allowMethods);

    CorsHandler allowedOrigins(String... origins);

    CorsHandler allowedHeaders(AllowHeaders allowHeaders);

    CorsHandler exposedHeaders(ScxHttpHeaderName... headerNames);

    CorsHandler allowCredentials(boolean allow);

    CorsHandler maxAgeSeconds(int maxAgeSeconds);

}
