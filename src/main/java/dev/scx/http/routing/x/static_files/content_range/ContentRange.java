package dev.scx.http.routing.x.static_files.content_range;

import static dev.scx.http.routing.x.static_files.content_range.ContentRangeHelper.encodeContentRange;

/// HttpHeader Content-Range
///
/// 支持两种形式:
/// 1. bytes start-end/size
/// 2. bytes */size
///
/// @param start 普通范围时不能为空; unsatisfied 时必须为空
/// @param end   普通范围时不能为空; unsatisfied 时必须为空
/// @param size  资源总长度, 不可为空
/// @author scx567888
/// @version 0.0.1
public record ContentRange(Long start, Long end, Long size) {

    public ContentRange {
        if (size == null || size < 0) {
            throw new IllegalArgumentException("size must be >= 0");
        }

        var unsatisfied = start == null && end == null;

        if (!unsatisfied) {
            if (start == null || end == null) {
                throw new IllegalArgumentException("start and end must both be null or both be non-null");
            }

            if (start < 0) {
                throw new IllegalArgumentException("start must be >= 0");
            }

            if (end < 0) {
                throw new IllegalArgumentException("end must be >= 0");
            }

            if (start > end) {
                throw new IllegalArgumentException("start must be <= end");
            }

            if (end >= size) {
                throw new IllegalArgumentException("end must be < size");
            }
        }
    }

    public static ContentRange of(long start, long end, long size) {
        return new ContentRange(start, end, size);
    }

    public static ContentRange ofUnsatisfied(long size) {
        return new ContentRange(null, null, size);
    }

    public static ContentRange parse(String contentRangeStr) throws IllegalContentRangeException {
        return ContentRangeHelper.parseContentRange(contentRangeStr);
    }

    public boolean isUnsatisfied() {
        return start == null && end == null;
    }

    public String encode() {
        return encodeContentRange(this);
    }

}
