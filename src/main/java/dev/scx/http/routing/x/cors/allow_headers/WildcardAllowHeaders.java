package dev.scx.http.routing.x.cors.allow_headers;

public final class WildcardAllowHeaders implements AllowHeaders{

    public static final WildcardAllowHeaders WILDCARD_ALLOW_HEADERS=new WildcardAllowHeaders();

    /// 保证单例
    private WildcardAllowHeaders() {

    }

    @Override
    public String allowedHeadersString(String requestHeadersString) {
        return "*";
    }

}
