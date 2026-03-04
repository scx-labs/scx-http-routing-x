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
        var start = range.getStart();
        var end = range.getEnd(attr.size());

        // 计算需要发送的长度
        var length = end - start + 1;

        // 我们需要构建如下的结构
        // status: 206 Partial Content
        response.statusCode(PARTIAL_CONTENT);
        // Content-Range: bytes 0-1023/146515
        response.setHeader(CONTENT_RANGE, "bytes " + start + "-" + end + "/" + attr.size());
        // 发送
        response.send(target.toFile(), start, length);

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

}
