package dev.scx.http.routing.x.cors.expose_headers;

public final class WildcardExposeHeaders implements ExposeHeaders {

    public static final WildcardExposeHeaders WILDCARD_EXPOSE_HEADERS = new WildcardExposeHeaders();

    /// 保证单例
    private WildcardExposeHeaders() {

    }

    @Override
    public String exposedHeaders() {
        return "*";
    }

}
