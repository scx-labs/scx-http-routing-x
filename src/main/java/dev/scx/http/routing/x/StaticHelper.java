package dev.scx.http.routing.x;


import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/// StaticHelper
///
/// @author scx567888
/// @version 0.0.1
public class StaticHelper {

    public static void sendStatic(Path path, RoutingContext context) {
        var request = context.request();
        var response = context.response();

        //参数校验
        var notExists = Files.notExists(path);
        if (notExists) {
            throw new NotFoundException();
        }
        //获取文件长度
        var fileLength = IOHelper.getFileSize(path);

        //1, 通知客户端我们支持 分段加载
        response.headers().set(ACCEPT_RANGES, "bytes");

        //2, 尝试解析 Range
        var rangeStr = request.getHeader("Range");

        //3, 设置 contentType (只有在未设置的时候才设置)
        if (response.contentType() == null) {
            var contentType = getMediaTypeByFile(path);
            response.contentType(contentType);
        }

        //3, 如果为空 则发送全量数据
        if (rangeStr == null) {
            response.send(path);
            return;
        }

        //4, 尝试解析
        var ranges = Range.parseRange(rangeStr);
        //目前我们只支持单个的部分请求
        if (ranges.size() == 1) {
            //获取第一个分段请求
            var range = ranges.get(0);
            var start = range.getStart();
            var end = range.getEnd(fileLength);

            //计算需要发送的长度
            var length = end - start + 1;

            //我们需要构建如下的结构
            // status: 206 Partial Content
            response.status(PARTIAL_CONTENT);
            // Content-Range: bytes 0-1023/146515
            response.setHeader(CONTENT_RANGE, "bytes " + start + "-" + end + "/" + fileLength);
            // Content-Length: 1024
            response.contentLength(length);
            //发送
            response.send(path, start, length);
        } else {
            // 这里是多个 部分请求 我们暂时不支持 所以全量发送
            response.send(path);
        }

    }

    public static ScxMediaType getMediaTypeByFile(Path path) {
        var fileFormat = FileFormat.findByFileName(path.getFileName().toString());
        if (fileFormat == null) {
            fileFormat = FileFormat.BIN;
        }
        var mediaType = fileFormat.mediaType();
        var contentType = ScxMediaType.of(mediaType);
        if (mediaType == TEXT_PLAIN || mediaType == TEXT_HTML || mediaType == APPLICATION_XML || mediaType == APPLICATION_JSON) {
            contentType.charset(StandardCharsets.UTF_8);
        }
        return contentType;
    }

}
