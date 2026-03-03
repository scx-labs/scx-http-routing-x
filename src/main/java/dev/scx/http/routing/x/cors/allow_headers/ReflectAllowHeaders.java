package dev.scx.http.routing.x.cors.allow_headers;

public final class ReflectAllowHeaders implements AllowHeaders{

    public static final ReflectAllowHeaders REFLECT_ALLOW_HEADERS=new ReflectAllowHeaders();

    /// 保证单例
    private ReflectAllowHeaders() {

    }

    @Override
    public String allowedHeadersString(String requestHeadersString) {
        return requestHeadersString;
    }

}
