package dev.scx.http.routing.x.cors.expose_headers;

public final class NoneExposeHeaders implements ExposeHeaders {

    public static final NoneExposeHeaders NONE_EXPOSE_HEADERS = new NoneExposeHeaders();

    /// 保证单例
    private NoneExposeHeaders() {
    }

    @Override
    public String exposedHeaders() {
        return null;
    }

}
