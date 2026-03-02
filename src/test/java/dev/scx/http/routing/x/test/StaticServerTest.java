package dev.scx.http.routing.x.test;

import dev.scx.http.routing.Route;
import dev.scx.http.routing.Router;
import dev.scx.http.routing.method_matcher.MethodMatcher;
import dev.scx.http.routing.path_matcher.PathMatcher;
import dev.scx.http.x.HttpServer;

import java.io.IOException;

public class StaticServerTest {

    public static void main(String[] args) throws IOException {
        test1();
    }

    public static void test1() throws IOException {
        Router router = Router.of();

        router.addRoute(-10000, Route.of(
            PathMatcher.any(),
            MethodMatcher.any(),
            ctx -> {
                ctx.request().response().send("123");
            }
        ));

        var httpServer = new HttpServer();
        httpServer.onRequest(router);
        httpServer.start(8080);
    }

}
