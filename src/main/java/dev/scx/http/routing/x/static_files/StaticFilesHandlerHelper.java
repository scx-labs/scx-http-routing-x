package dev.scx.http.routing.x.static_files;

import dev.scx.http.media_type.FileFormat;
import dev.scx.http.media_type.ScxMediaType;
import dev.scx.http.routing.RoutingContext;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import static dev.scx.http.headers.HttpHeaderName.ACCEPT_RANGES;
import static dev.scx.http.headers.HttpHeaderName.CONTENT_RANGE;
import static dev.scx.http.media_type.MediaType.*;
import static dev.scx.http.status_code.HttpStatusCode.PARTIAL_CONTENT;
import static dev.scx.http.status_code.HttpStatusCode.RANGE_NOT_SATISFIABLE;

final class StaticFilesHandlerHelper {

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

        // 2, 设置 contentType
        var mediaType = getMediaType(target);
        response.contentType(mediaType);

        // 3, 判断是不是 Range 请求
        var range = request.headers().range();

        // 4, 不是 Range 请求, 发送完整文件.
        if (range == null) {
            response.send(target.toFile());
            return;
        }

        // 5, 发送 Range 响应.

        var size = attr.size();

        var byteRange = normalizeRange(range.start(), range.end(), size);

        if (byteRange == null) {
            response.statusCode(RANGE_NOT_SATISFIABLE);
            response.setHeader("Content-Range", "bytes */" + size);
            response.send();
            return;
        }

        long offset = byteRange.offset();
        long length = byteRange.length();
        long last = byteRange.last();

        response.statusCode(PARTIAL_CONTENT);

        response.setHeader(CONTENT_RANGE, "bytes " + offset + "-" + last + "/" + size);

        response.send(target.toFile(), offset, length);

    }

    public static ScxMediaType getMediaType(Path path) {
        var fileFormat = FileFormat.findByFileName(path.getFileName().toString());
        if (fileFormat == null) {
            fileFormat = FileFormat.BIN;
        }
        var mediaType = fileFormat.mediaType();

        // 针对 常见文本文件 我们设置一下 格式
        if (mediaType == TEXT_PLAIN || mediaType == TEXT_HTML || mediaType == APPLICATION_XML || mediaType == APPLICATION_JSON) {
            return ScxMediaType.of(mediaType).charset(StandardCharsets.UTF_8);
        }
        return mediaType;
    }

    record ByteRange(long offset, long length, long last) {

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

}
