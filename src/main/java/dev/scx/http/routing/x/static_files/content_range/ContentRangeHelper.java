package dev.scx.http.routing.x.static_files.content_range;

import static java.lang.Long.parseLong;

/// ContentRangeHelper
///
/// @author scx567888
/// @version 0.0.1
public final class ContentRangeHelper {

    public static ContentRange parseContentRange(String contentRangeStr) throws IllegalContentRangeException {

        // 1, 去两端空白
        contentRangeStr = contentRangeStr.trim();

        // 2, 必须是 'bytes ' 开头 (忽略大小写)
        if (!contentRangeStr.regionMatches(true, 0, "bytes ", 0, 6)) {
            throw new IllegalContentRangeException("Invalid content-range header: " + contentRangeStr);
        }

        var rest = contentRangeStr.substring(6).trim();

        var slashIndex = rest.indexOf('/');
        if (slashIndex < 0) {
            throw new IllegalContentRangeException("Invalid content-range header: " + contentRangeStr);
        }

        var rangePart = rest.substring(0, slashIndex).trim();
        var lengthPart = rest.substring(slashIndex + 1).trim();

        try {
            var completeLength = parseLong(lengthPart);

            if ("*".equals(rangePart)) {
                return ContentRange.ofUnsatisfied(completeLength);
            }

            var arr = rangePart.split("-", 2);
            if (arr.length != 2) {
                throw new IllegalContentRangeException("Invalid content-range header: " + contentRangeStr);
            }

            var start = parseLong(arr[0].trim());
            var end = parseLong(arr[1].trim());

            return ContentRange.of(start, end, completeLength);
        } catch (IllegalContentRangeException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalContentRangeException("Invalid content-range header: " + contentRangeStr, e);
        }
    }

    public static String encodeContentRange(ContentRange contentRange) {
        if (contentRange.isUnsatisfied()) {
            return "bytes */" + contentRange.size();
        }
        return "bytes " + contentRange.start() + "-" + contentRange.end() + "/" + contentRange.size();
    }

}
