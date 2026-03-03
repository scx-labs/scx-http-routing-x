package dev.scx.http.routing.x;

import dev.scx.function.Function1Void;
import dev.scx.http.headers.ScxHttpHeaderName;
import dev.scx.http.method.ScxHttpMethod;
import dev.scx.http.routing.RoutingContext;

public interface CorsHandler extends Function1Void<RoutingContext, Throwable> {

    CorsHandler allowedMethods(ScxHttpMethod... methods);

    CorsHandler allowedOrigins(String... origins);

    CorsHandler allowedHeaders(ScxHttpHeaderName... headerNames);

    CorsHandler exposedHeaders(ScxHttpHeaderName... headerNames);

    CorsHandler allowCredentials(boolean allow);

    CorsHandler maxAgeSeconds(int maxAgeSeconds);

}
