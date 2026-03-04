package dev.scx.http.routing.x.static_files;

import dev.scx.function.Function1Void;
import dev.scx.http.routing.RoutingContext;

import java.nio.file.Path;

public interface StaticFilesHandler extends Function1Void<RoutingContext, Throwable> {

    static StaticFilesHandler of(Path root) {
        return new StaticFilesHandlerImpl(root);
    }

}
