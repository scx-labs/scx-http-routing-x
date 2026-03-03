package dev.scx.http.routing.x.cors.allow_methods;

import dev.scx.http.headers.ScxHttpHeaderName;
import dev.scx.http.method.ScxHttpMethod;

import java.util.Arrays;
import java.util.stream.Collectors;

public final class ListAllowMethods implements AllowMethods{

    private final String allowedMethodsString;

    public ListAllowMethods(ScxHttpMethod... methods) {
        this.allowedMethodsString = Arrays.stream(methods)
            .map(ScxHttpMethod::value)
            .collect(Collectors.joining(", "));
    }

    public ListAllowMethods(String... methods) {
        this.allowedMethodsString = String.join(", ",methods);
    }

    @Override
    public String allowedMethodsString(String requestMethodString) {
        return allowedMethodsString;
    }

}
