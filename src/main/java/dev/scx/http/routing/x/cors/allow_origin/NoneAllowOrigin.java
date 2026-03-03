package dev.scx.http.routing.x.cors.allow_origin;

public final class NoneAllowOrigin implements AllowOrigin{

    public static final NoneAllowOrigin NONE_ALLOW_ORIGIN=new NoneAllowOrigin();

    /// 保证单例
    private NoneAllowOrigin() {

    }

    @Override
    public String allowedOrigin(String origin) {
        return null;
    }

}
