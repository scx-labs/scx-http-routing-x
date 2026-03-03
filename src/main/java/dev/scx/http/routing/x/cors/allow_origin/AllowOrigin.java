package dev.scx.http.routing.x.cors.allow_origin;

import static dev.scx.http.routing.x.cors.allow_origin.WildcardAllowOrigin.WILDCARD_ALLOW_ORIGIN;

public sealed interface AllowOrigin permits ListAllowOrigin, WildcardAllowOrigin {

    static ListAllowOrigin of(String... origins) {
        return new ListAllowOrigin(origins);
    }

    static WildcardAllowOrigin ofWildcard() {
        return WILDCARD_ALLOW_ORIGIN;
    }

    String allowedOrigin(String origin);

}
