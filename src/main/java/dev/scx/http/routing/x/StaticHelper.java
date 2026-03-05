package dev.scx.http.routing.x;


import dev.scx.http.exception.NotFoundException;
import dev.scx.http.media_type.FileFormat;
import dev.scx.http.media_type.ScxMediaType;
import dev.scx.http.routing.RoutingContext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static dev.scx.http.headers.HttpHeaderName.ACCEPT_RANGES;
import static dev.scx.http.headers.HttpHeaderName.CONTENT_RANGE;
import static dev.scx.http.status_code.HttpStatusCode.PARTIAL_CONTENT;

/// StaticHelper
///
/// @author scx567888
/// @version 0.0.1
public class StaticHelper {

    public static void sendStatic(Path path, RoutingContext context) {
        var request = context.request();
        var response = request.response();

        //参数校验
        var notExists = Files.notExists(path);
        if (notExists) {
            throw new NotFoundException();
        }
        //获取文件长度
        long fileLength = 0;
        try {
            fileLength = Files.size(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //1, 通知客户端我们支持 分段加载
        response.headers().set(ACCEPT_RANGES, "bytes");

        //2, 尝试解析 Range
        var rangeStr = request.getHeader("Range");

        //3, 如果为空 则发送全量数据
        if (rangeStr == null) {
            response.send(path.toFile());
            return;
        }

        //4, 尝试解析
        var range = request.headers().range();
        //目前我们只支持单个的部分请求

        //获取第一个分段请求
        var start = range.getStart();
        var end = range.getEnd(fileLength);

        //计算需要发送的长度
        var length = end - start + 1;

        //我们需要构建如下的结构
        // status: 206 Partial Content
        response.statusCode(PARTIAL_CONTENT);
        // Content-Range: bytes 0-1023/146515
        response.setHeader(CONTENT_RANGE, "bytes " + start + "-" + end + "/" + fileLength);
        // Content-Length: 1024
        response.contentLength(length);
        //发送
        response.send(path.toFile(), start, length);

    }



}
