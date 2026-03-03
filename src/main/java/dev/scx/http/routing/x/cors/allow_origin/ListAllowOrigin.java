package dev.scx.http.routing.x.cors.allow_origin;

import java.util.Arrays;

public final class ListAllowOrigin implements AllowOrigin {

    private final String[] origins;

    public ListAllowOrigin(String... origins) {
        // todo 这里可能需要一些校验
        this.origins = Arrays.copyOf(origins, origins.length);
    }

    @Override
    public String xxxOrigin(String origin) {
        for (var o : this.origins) {
            if (o.equals(origin)) {
                return o;
            }
        }
        return null;
    }

}
