package dev.scx.http.routing.x.cors.allow_methods;

public final class ReflcetAllowMethods implements AllowMethods{

    public static final ReflcetAllowMethods REFLCET_ALLOW_METHODS=new ReflcetAllowMethods();

    /// 保证单例
    private ReflcetAllowMethods() {

    }

    @Override
    public String allowedMethodsString(String requestMethodString) {
        return requestMethodString;
    }

}
