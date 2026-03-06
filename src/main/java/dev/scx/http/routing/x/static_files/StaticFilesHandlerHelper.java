package dev.scx.http.routing.x.static_files;

import dev.scx.exception.ScxWrappedException;
import dev.scx.http.routing.RoutingContext;
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

    // todo 降噪 逻辑待优化
    public static void sendFile(Path target, BasicFileAttributes attr, RoutingContext context) {
        var request = context.request();
        var response = request.response();

        // 1, 让客户端知道我们支持分段加载
        response.setHeader(ACCEPT_RANGES, "bytes");

        // 2, 判断是不是 Range 请求
        var rangeStr = request.getHeader(RANGE);
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

        var byteRange = normalizeRange(range.start(), range.end(), size);

        if (byteRange == null) {
            response.statusCode(RANGE_NOT_SATISFIABLE);
            response.setHeader("Content-Range", "bytes */" + size);
            response.send();
            return;
        }

        long start = byteRange.offset();
        long length = byteRange.length();
        long end = byteRange.last();

        // 设置 206
        response.statusCode(PARTIAL_CONTENT);

        response.setHeader(CONTENT_RANGE, "bytes " + start + "-" + end + "/" + size);

        // 这里降噪
        try {
            response.send(target.toFile(), start, length);
        } catch (ScxWrappedException e) {
            // 忽略所有 写出异常.
            if (e.getCause() instanceof ScxOutputException) {
                return;
            }
            // 其余正常抛出
            throw e;
        }

    }

    public static ByteRange normalizeRange(Long start, Long end, long size) {

        // 空文件无法满足任何 range
        if (size <= 0) {
            return null;
        }

        long offset;
        long last;

        if (start != null && end != null) {
            // bytes=start-end

            if (start < 0) {
                return null;
            }

            if (start >= size) {
                return null;
            }

            offset = start;

            last = Math.min(end, size - 1);

            if (last < offset) {
                return null;
            }

        } else if (start != null) {
            // bytes=start-

            if (start < 0) {
                return null;
            }

            if (start >= size) {
                return null;
            }

            offset = start;
            last = size - 1;

        } else if (end != null) {
            // bytes=-suffix

            long suffix = end;

            if (suffix <= 0) {
                return null;
            }

            if (suffix >= size) {
                offset = 0;
            } else {
                offset = size - suffix;
            }

            last = size - 1;

        } else {
            return null;
        }

        long length = last - offset + 1;

        return new ByteRange(offset, length, last);
    }

    record ByteRange(long offset, long length, long last) {

    }

}
