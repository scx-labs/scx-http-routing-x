package dev.scx.http.routing.x.cors.allow_methods;

import dev.scx.http.method.ScxHttpMethod;

import java.util.LinkedHashSet;

public final class ListAllowMethods implements AllowMethods {

    private final String allowedMethodsString;

    public ListAllowMethods(ScxHttpMethod... methods) {
        if (methods == null) {
            throw new NullPointerException("methods must not be null");
        }
        if (methods.length == 0) {
            throw new IllegalArgumentException("methods must not be empty");
        }

        var set = new LinkedHashSet<String>();

        for (var m : methods) {
            if (m == null) {
                throw new NullPointerException("methods must not be null");
            }

            String trimmed = m.value().trim();

            if (trimmed.isEmpty()) {
                throw new IllegalArgumentException("methods must not be blank");
            }

            if ("*".equals(trimmed)) {
                throw new IllegalArgumentException(
                    "'*' is not allowed in ListAllowMethods, use WildcardAllowMethods instead");
            }

            if (trimmed.contains(",")) {
                throw new IllegalArgumentException(
                    "methods must not contain ',' : " + trimmed);
            }

            set.add(trimmed);
        }

        this.allowedMethodsString = String.join(", ", set);
    }

    @Override
    public String allowedMethods(String requestMethodString) {
        return allowedMethodsString;
    }

}
