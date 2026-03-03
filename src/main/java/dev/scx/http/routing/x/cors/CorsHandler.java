package dev.scx.http.routing.x.cors;

import dev.scx.function.Function1Void;
import dev.scx.http.routing.RoutingContext;
import dev.scx.http.routing.x.cors.allow_headers.AllowHeaders;
import dev.scx.http.routing.x.cors.allow_methods.AllowMethods;
import dev.scx.http.routing.x.cors.allow_origin.AllowOrigin;
import dev.scx.http.routing.x.cors.expose_headers.ExposeHeaders;

public interface CorsHandler extends Function1Void<RoutingContext, Throwable> {

    CorsHandler allowOrigin(AllowOrigin allowOrigin);

    CorsHandler allowMethods(AllowMethods allowMethods);

    CorsHandler allowHeaders(AllowHeaders allowHeaders);

    CorsHandler exposeHeaders(ExposeHeaders exposeHeaders);

    CorsHandler allowCredentials(boolean allowCredentials);

    CorsHandler maxAgeSeconds(long maxAgeSeconds);

}
