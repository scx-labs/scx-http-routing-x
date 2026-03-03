package dev.scx.http.routing.x.cors.allow_methods;

public final class WildcardAllowMethods implements AllowMethods{
    @Override
    public String allowedMethodsString(String requestMethodString) {
        return "";
    }
}
