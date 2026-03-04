package dev.scx.http.routing.x.static_files;

import dev.scx.http.routing.RoutingContext;

import java.nio.file.Path;

import static dev.scx.http.method.HttpMethod.GET;
import static dev.scx.http.method.HttpMethod.HEAD;

public final class StaticFilesHandlerImpl implements StaticFilesHandler{

    private final Path root;

    public StaticFilesHandlerImpl(Path root) {
        this.root=root;
    }

    @Override
    public void apply(RoutingContext context) throws Throwable {
        var request = context.request();

        // 0, 只关心 GET 和 HEAD
        if (request.method() != GET && request.method() != HEAD) {
            context.next();
            return;
        }

        // 1, 获取捕获
        var p = context.pathMatch().capture("*");

    }

}
