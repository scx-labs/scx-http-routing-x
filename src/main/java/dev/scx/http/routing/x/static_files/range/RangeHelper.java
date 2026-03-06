package dev.scx.http.routing.x.static_files.range;

import static java.lang.Long.parseLong;

/// RangeHelper
///
/// @author scx567888
/// @version 0.0.1
public final class RangeHelper {

    public static Range parseRange(String rangeStr) throws IllegalRangeException {

        // 1, 去两端空白
        rangeStr = rangeStr.trim();

        // 2, 必须是 'bytes=' 开头 (忽略大小写)
        if (!rangeStr.regionMatches(true, 0, "bytes=", 0, 6)) {
            throw new IllegalRangeException("Invalid range header: " + rangeStr);
        }

        var spec = rangeStr.substring(6).trim();

        // 只取第一段.
        var firstPart = spec.split(",", 2)[0].trim();

        var range = firstPart.split("-", 2);

        if (range.length != 2) {
            throw new IllegalRangeException("Invalid range header: " + rangeStr);
        }

        var startStr = range[0].trim();
        var endStr = range[1].trim();

        try {
            var start = startStr.isEmpty() ? null : parseLong(startStr);
            var end = endStr.isEmpty() ? null : parseLong(endStr);
            return new Range(start, end);
        } catch (Exception e) {
            throw new IllegalRangeException("Invalid range header: " + rangeStr, e);
        }

    }

    public static String encodeRange(Range range) {
        var start = range.start();
        var end = range.end();

        if (start != null) {
            if (end != null) {
                return "bytes=" + start + "-" + end;
            } else {
                return "bytes=" + start + "-";
            }
        } else {
            // 永不存在 start 和 end 同时 为 null 的情况
            // 所以这里可以认为 end 永不为 null.
            return "bytes=-" + end;
        }

    }

}
