package dev.scx.http.routing.x.static_files;

import dev.scx.exception.ScxWrappedException;
import dev.scx.http.routing.RoutingContext;
import dev.scx.http.routing.x.static_files.content_range.ContentRange;
import dev.scx.http.routing.x.static_files.range.Range;
import dev.scx.io.exception.ScxOutputException;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import static dev.scx.http.headers.HttpHeaderName.*;
import static dev.scx.http.status_code.HttpStatusCode.PARTIAL_CONTENT;
import static dev.scx.http.status_code.HttpStatusCode.RANGE_NOT_SATISFIABLE;

public final class StaticFilesHandlerHelper {

    /// 将捕获转换为 相对路径
    public static Path restToRelativePath(String rest) {
        if (rest == null) {
            // 没有 "*" 捕获 (模板未使用通配符), 视为根路径
            return Path.of("");
        }
        // 移除 全部前导 "/"
        var i = 0;
        while (i < rest.length() && rest.charAt(i) == '/') {
            i = i + 1;
        }
        return Path.of(rest.substring(i));
    }

    public static void sendFile(Path target, BasicFileAttributes attr, RoutingContext context) {
        var request = context.request();
        var response = request.response();

        // 1, 让客户端知道我们支持分段加载
        response.setHeader(ACCEPT_RANGES, "bytes");

        // 2, 判断是不是 Range 请求
        var rangeStr = request.getHeader(RANGE);
        // todo 这里解析错误待处理 返回 416 ?
        var range = rangeStr != null ? Range.parse(rangeStr) : null;

        // 3, 不是 Range 请求, 发送完整文件.
        if (range == null) {
            // 这里由于浏览器经常会中断连接
            // 为了防止频繁的 异常信息, 这里降噪处理.
            // 后边的 Range 同理.
            try {
                response.send(target.toFile());
            } catch (ScxWrappedException e) {
                // 忽略所有 写出异常.
                if (e.getCause() instanceof ScxOutputException) {
                    return;
                }
                // 其余正常抛出
                throw e;
            }
            return;
        }

        // 4, 发送 Range 响应.
        var size = attr.size();

        var contentRange = resolve(range, size);

        if (contentRange.isUnsatisfied()) {
            response.statusCode(RANGE_NOT_SATISFIABLE);
            response.setHeader(CONTENT_RANGE, contentRange.encode());
            response.send();
            return;
        }

        // 设置 206
        response.statusCode(PARTIAL_CONTENT);
        response.setHeader(CONTENT_RANGE, contentRange.encode());

        // 这里降噪
        try {
            long offset = contentRange.start();
            long length = contentRange.end() - contentRange.start() + 1;
            response.send(target.toFile(), offset, length);
        } catch (ScxWrappedException e) {
            // 忽略所有 写出异常.
            if (e.getCause() instanceof ScxOutputException) {
                return;
            }
            // 其余正常抛出
            throw e;
        }

    }

    public static ContentRange resolve(Range range, long size) {

        if (size == 0) {
            return ContentRange.ofUnsatisfied(size);
        }

        var start = range.start();
        var end = range.end();

        if (start != null && end != null) {

            if (start >= size) {
                return ContentRange.ofUnsatisfied(size);
            }

            long realEnd = Math.min(end, size - 1);

            return ContentRange.of(start, realEnd, size);
        }

        if (start != null) {

            if (start >= size) {
                return ContentRange.ofUnsatisfied(size);
            }

            return ContentRange.of(start, size - 1, size);
        }

        // suffix
        long suffix = end;

        if (suffix == 0) {
            return ContentRange.ofUnsatisfied(size);
        }

        start = Math.max(size - suffix, 0);
        end = size - 1;

        return ContentRange.of(start, end, size);
    }

}
