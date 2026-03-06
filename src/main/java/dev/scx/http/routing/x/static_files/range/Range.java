package dev.scx.http.routing.x.static_files.range;

import static dev.scx.http.routing.x.static_files.range.RangeHelper.encodeRange;

/// HttpHeader Range
///
/// @param start 可以为空, 但与此同时 end 不能为空
/// @param end   可以为空, 但与此同时 start 不能为空
/// @author scx567888
/// @version 0.0.1
public record Range(Long start, Long end) {

    public Range {
        if (start == null && end == null) {
            throw new IllegalArgumentException("start and end cannot both be null");
        }

        if (start != null && start < 0) {
            throw new IllegalArgumentException("start must be >= 0");
        }

        if (end != null && end < 0) {
            throw new IllegalArgumentException("end must be >= 0");
        }

        if (start != null && end != null && start > end) {
            throw new IllegalArgumentException("start must be <= end");
        }
    }

    /// 对于多段, 只取第一段
    public static Range parse(String rangeStr) throws IllegalRangeException {
        return RangeHelper.parseRange(rangeStr);
    }

    public String encode() {
        return encodeRange(this);
    }

}
