package dev.scx.http.routing.x.cors.allow_origin;

import static dev.scx.http.routing.x.cors.allow_origin.NoneAllowOrigin.NONE_ALLOW_ORIGIN;
import static dev.scx.http.routing.x.cors.allow_origin.WildcardAllowOrigin.WILDCARD_ALLOW_ORIGIN;

public sealed interface AllowOrigin permits ListAllowOrigin, WildcardAllowOrigin, NoneAllowOrigin {

    static ListAllowOrigin of(String... origins) {
        return new ListAllowOrigin(origins);
    }

    static WildcardAllowOrigin ofWildcard() {
        return WILDCARD_ALLOW_ORIGIN;
    }

    static NoneAllowOrigin ofNone() {
        return NONE_ALLOW_ORIGIN;
    }

    String allowedOrigin(String origin);

}
