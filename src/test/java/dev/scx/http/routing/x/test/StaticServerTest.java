package dev.scx.http.routing.x.test;

import dev.scx.http.routing.Router;
import dev.scx.http.routing.x.StaticHandler;
import dev.scx.http.routing.x.cors.CorsHandler;
import dev.scx.http.routing.x.static_files.StaticFilesHandler;
import dev.scx.http.x.HttpServer;

import java.io.IOException;
import java.nio.file.Path;

public class StaticServerTest {

    public static void main(String[] args) throws IOException {
        test1();
    }

    public static void test1() throws IOException {
        Router router = Router.of();

        // 尽可能靠前
        router.route(-10000, CorsHandler.of());

        router.route("/*", StaticFilesHandler.of(Path.of("C:\\Users\\scx\\Projects\\page3")));

        var httpServer = new HttpServer();
        httpServer.onRequest(router);
        httpServer.start(8080);
    }

}
