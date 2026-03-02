package dev.scx.http.routing.x;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


/// StaticHandler
///
/// @author scx567888
/// @version 0.0.1
public class StaticHandler implements Function1Void<RoutingContext, Throwable> {

    private final Path root;

    public StaticHandler(Path root) {
        this.root = root;
    }

    @Override
    public void apply(RoutingContext routingContext) throws Throwable {
        var request = routingContext.request();

        if (request.method() != GET && request.method() != HEAD) {
            routingContext.next();
            return;
        }

        var p = routingContext.pathParams().get("*");
        var filePath = getFilePath(p);
        var notExists = Files.notExists(filePath);
        //文件不存在
        if (notExists) {
            routingContext.next();
            return;
        }

        try {
            var attr = Files.readAttributes(filePath, BasicFileAttributes.class);
            var lastModifiedTime = attr.lastModifiedTime().toInstant().atZone(ZoneId.of("GMT")).format(DateTimeFormatter.RFC_1123_DATE_TIME);
            var etag = "\"" + attr.lastModifiedTime().toMillis() + "\"";

            // 检查 If-None-Match 和 If-Modified-Since 头
            var ifNoneMatch = request.getHeader("If-None-Match");
            var ifModifiedSince = request.getHeader("If-Modified-Since");

            if (etag.equals(ifNoneMatch) || lastModifiedTime.equals(ifModifiedSince)) {
                routingContext.response().status(304).send();
                return;
            }

            routingContext.response().setHeader(CACHE_CONTROL, "public,immutable,max-age=2628000");
            routingContext.response().setHeader(ETAG, etag);
            routingContext.response().setHeader(LAST_MODIFIED, lastModifiedTime);

            sendStatic(filePath, routingContext);
        } catch (IOException e) {
            routingContext.next();
        }

    }

    public Path getFilePath(String p) {
        //我们需要支持单文件形式 既无论请求路径为什么都返回单文件
        var regularFile = Files.isRegularFile(root);
        if (regularFile) {
            return root;
        }
        var path = root.resolve(p).normalize();
        // 1, 重要!!! , 防止访问到上级文件的情况
        if (!path.startsWith(root)) {
            throw new NotFoundException();
        }
        if (Files.isDirectory(path)) {
            return path.resolve("index.html");
        } else {
            return path;
        }
    }

}
