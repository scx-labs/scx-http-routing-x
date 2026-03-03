package dev.scx.http.routing.x.cors.allow_origin;

import java.util.LinkedHashSet;

public final class ListAllowOrigin implements AllowOrigin {

    private final String[] origins;

    public ListAllowOrigin(String... origins) {
        if (origins == null) {
            throw new NullPointerException("origins must not be null");
        }
        if (origins.length == 0) {
            throw new IllegalArgumentException("origins must not be empty");
        }

        var set = new LinkedHashSet<String>();

        for (String o : origins) {
            if (o == null) {
                throw new NullPointerException("origin must not be null");
            }

            var trimmed = o.trim();

            if (trimmed.isEmpty()) {
                throw new IllegalArgumentException("origin must not be blank");
            }

            if ("*".equals(trimmed)) {
                throw new IllegalArgumentException(
                    "'*' is not allowed in ListAllowOrigin, use WildcardAllowOrigin instead");
            }

            set.add(trimmed);
        }

        this.origins = set.toArray(String[]::new);
    }

    @Override
    public String allowedOrigin(String origin) {
        for (var o : this.origins) {
            if (o.equals(origin)) {
                return o;
            }
        }
        return null;
    }

}
