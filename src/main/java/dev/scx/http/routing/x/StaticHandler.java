package dev.scx.http.routing.x;


import dev.scx.function.Function1Void;
import dev.scx.http.exception.NotFoundException;
import dev.scx.http.routing.RoutingContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static dev.scx.http.headers.HttpHeaderName.*;
import static dev.scx.http.method.HttpMethod.GET;
import static dev.scx.http.method.HttpMethod.HEAD;
//import static dev.scx.http.routing.x.StaticHelper.sendStatic;


/// StaticHandler
///
/// @author scx567888
/// @version 0.0.1
public class StaticHandler implements Function1Void<RoutingContext, Throwable> {

    private final Path root;

    public StaticHandler(Path root) {
        this.root = root;
    }

    /**
     * 将 TemplatePathMatcher 的 "*" 捕获值（可能为 "", "/", "/a/b", "/a/b/"）
     * 转换为用于文件系统 resolve 的相对路径（永不以 "/" 开头）。
     */
    private static String toRelativeStaticPath(String p) {
        if (p == null || p.isEmpty() || "/".equals(p)) {
            return "index.html";
        }

        // p 形如 "/a/b" 或 "/a/b/"
        if (p.charAt(0) == '/') {
            p = p.substring(1); // 关键：去掉 leading slash，避免 resolve 变成绝对路径
        }

        // 目录请求：".../" -> ".../index.html"
        if (p.isEmpty() || p.endsWith("/")) {
            return p + "index.html";
        }

        return p;
    }

    @Override
    public void apply(RoutingContext routingContext) throws Throwable {
        var request = routingContext.request();

        if (request.method() != GET && request.method() != HEAD) {
            routingContext.next();
            return;
        }

        var p = routingContext.pathMatch().capture("*");
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
                routingContext.request().response().statusCode(304).send();
                return;
            }

            routingContext.request().response().setHeader(CACHE_CONTROL, "public,immutable,max-age=2628000");
            routingContext.request().response().setHeader(ETAG, etag);
            routingContext.request().response().setHeader(LAST_MODIFIED, lastModifiedTime);

//            sendStatic(filePath, routingContext);
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

        // 目录模式：把 wildcard 捕获映射为相对路径
        String rel = toRelativeStaticPath(p);

        // 建议把 root 先变成 absolute+normalized，避免 startsWith 在相对路径上语义怪异
        Path rootAbs = root.toAbsolutePath().normalize();
        Path resolved = rootAbs.resolve(rel).normalize();

        // 防穿越：必须仍在 root 内
        if (!resolved.startsWith(rootAbs)) {
            throw new NotFoundException();
        }

        return resolved;
    }

}
