package dev.scx.http.routing.x.cors.allow_methods;

import dev.scx.http.method.ScxHttpMethod;

import static dev.scx.http.routing.x.cors.allow_methods.ReflcetAllowMethods.REFLCET_ALLOW_METHODS;
import static dev.scx.http.routing.x.cors.allow_methods.WildcardAllowMethods.WILDCARD_ALLOW_METHODS;

public sealed interface AllowMethods permits ListAllowMethods, ReflcetAllowMethods, WildcardAllowMethods {

    static ListAllowMethods of(ScxHttpMethod... methods) {
        return new ListAllowMethods(methods);
    }

    static ReflcetAllowMethods ofReflect() {
        return REFLCET_ALLOW_METHODS;
    }

    static WildcardAllowMethods ofWildcard() {
        return WILDCARD_ALLOW_METHODS;
    }

    String allowedMethods(String requestMethodString);

}
