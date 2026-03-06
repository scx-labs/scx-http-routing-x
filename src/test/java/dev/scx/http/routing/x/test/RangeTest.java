package dev.scx.http.routing.x.test;

import dev.scx.http.routing.x.static_files.range.IllegalRangeException;
import dev.scx.http.routing.x.static_files.range.Range;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RangeTest {

    public static void main() {
        test1();
    }

    @Test
    public static void test1() {
        var range1 = Range.parse("bytes=0-499");
        Assert.assertEquals(range1.encode(), "bytes=0-499");

        var range2 = Range.parse("bytes=500-");
        Assert.assertEquals(range2.encode(), "bytes=500-");

        Assert.assertThrows(IllegalRangeException.class, () -> {
            Range.parse("bytes=");
        });

        // ======= 合法用例 =======

        var range3 = Range.parse("bytes=-500");
        Assert.assertEquals(range3.encode(), "bytes=-500");

        var range4 = Range.parse(" bytes=0-499 ");
        Assert.assertEquals(range4.encode(), "bytes=0-499");

        var range5 = Range.parse("BYTES=0-499");
        Assert.assertEquals(range5.encode(), "bytes=0-499");

        var range6 = Range.parse("bytes=0 - 499");
        Assert.assertEquals(range6.encode(), "bytes=0-499");

        var range7 = Range.parse("bytes=  0-499");
        Assert.assertEquals(range7.encode(), "bytes=0-499");

        var range8 = Range.parse("bytes=0-499  ");
        Assert.assertEquals(range8.encode(), "bytes=0-499");

        var range9 = Range.parse("bytes=500 -");
        Assert.assertEquals(range9.encode(), "bytes=500-");

        var range10 = Range.parse("bytes=- 500");
        Assert.assertEquals(range10.encode(), "bytes=-500");

        // 多段，只取第一段
        var range11 = Range.parse("bytes=0-1,4-5");
        Assert.assertEquals(range11.encode(), "bytes=0-1");

        var range12 = Range.parse("bytes= 0-1 , 4-5 ");
        Assert.assertEquals(range12.encode(), "bytes=0-1");

        var range13 = Range.parse("bytes=0-0,1-1,2-2");
        Assert.assertEquals(range13.encode(), "bytes=0-0");

        var range14 = Range.parse("bytes=0-999999999999");
        Assert.assertEquals(range14.encode(), "bytes=0-999999999999");

        var range15 = Range.parse("bytes=0-0");
        Assert.assertEquals(range15.encode(), "bytes=0-0");

        var range16 = Range.parse("bytes=0-");
        Assert.assertEquals(range16.encode(), "bytes=0-");

        // ======= 非法用例 =======

        Assert.assertThrows(IllegalRangeException.class, () -> {
            Range.parse("");
        });

        Assert.assertThrows(IllegalRangeException.class, () -> {
            Range.parse("   ");
        });

        Assert.assertThrows(IllegalRangeException.class, () -> {
            Range.parse("bytes"); // 没有 '='
        });

        Assert.assertThrows(IllegalRangeException.class, () -> {
            Range.parse("bytes 0-499"); // 没有 '='
        });

        Assert.assertThrows(IllegalRangeException.class, () -> {
            Range.parse("items=0-10"); // unit 不支持
        });

        Assert.assertThrows(IllegalRangeException.class, () -> {
            Range.parse("bytes=0"); // 没有 '-'
        });

        Assert.assertThrows(IllegalRangeException.class, () -> {
            Range.parse("bytes=-"); // start/end 都空 (构造函数也会拦)
        });

        Assert.assertThrows(IllegalRangeException.class, () -> {
            Range.parse("bytes=--500"); // 非法数字
        });

        Assert.assertThrows(IllegalRangeException.class, () -> {
            Range.parse("bytes=abc-def"); // 非数字
        });

        Assert.assertThrows(IllegalRangeException.class, () -> {
            Range.parse("bytes=500-499"); // start > end
        });

        Assert.assertThrows(IllegalRangeException.class, () -> {
            Range.parse("bytes=-0-1"); // 乱格式
        });

        Assert.assertThrows(IllegalRangeException.class, () -> {
            Range.parse("bytes=,0-1"); // 第一段为空 应当拒绝
        });

        var range17 = Range.parse("bytes=0-1,");
        Assert.assertEquals(range17.encode(), "bytes=0-1");

        Assert.assertThrows(IllegalRangeException.class, () -> {
            Range.parse("bytes=0--1"); // end 非法
        });

        Assert.assertThrows(IllegalRangeException.class, () -> {
            Range.parse("bytes=-500-"); // 非法
        });

        var range18 = Range.parse("bytes=0-499, -");
        Assert.assertEquals(range18.encode(), "bytes=0-499");

    }

}
