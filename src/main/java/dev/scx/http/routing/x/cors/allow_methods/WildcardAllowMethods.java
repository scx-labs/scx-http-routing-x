package dev.scx.http.routing.x.cors.allow_methods;

public final class WildcardAllowMethods implements AllowMethods {

    public static final WildcardAllowMethods WILDCARD_ALLOW_METHODS = new WildcardAllowMethods();

    /// 保证单例
    private WildcardAllowMethods() {

    }

    @Override
    public String allowedMethodsString(String requestMethodString) {
        return "*";
    }

}
