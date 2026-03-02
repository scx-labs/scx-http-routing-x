package dev.scx.function.test;

import dev.scx.function.*;
import org.testng.annotations.Test;

public class FunctionTest {

    public static void main(String[] args) throws Throwable {
        test1();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Test
    public static void test1() throws Throwable {

        Function0 f1 = () -> null;
        Function0Void f2 = () -> {};
        Function1 f3 = (a) -> null;
        Function1Void f4 = (a) -> {};
        Function2 f5 = (a, b) -> null;
        Function2Void f6 = (a, b) -> {};
        Function3 f7 = (a, b, c) -> null;
        Function3Void f8 = (a, b, c) -> {};
        Function4 f9 = (a, b, c, d) -> null;
        Function4Void f10 = (a, b, c, d) -> {};
        Function5 f11 = (a, b, c, d, e) -> null;
        Function5Void f12 = (a, b, c, d, e) -> {};
        Function6 f13 = (a, b, c, d, e, f) -> null;
        Function6Void f14 = (a, b, c, d, e, f) -> {};
        Function7 f15 = (a, b, c, d, e, f, g) -> null;
        Function7Void f16 = (a, b, c, d, e, f, g) -> {};
        Function8 f17 = (a, b, c, d, e, f, g, h) -> null;
        Function8Void f18 = (a, b, c, d, e, f, g, h) -> {};
        Function9 f19 = (a, b, c, d, e, f, g, h, i) -> null;
        Function9Void f20 = (a, b, c, d, e, f, g, h, i) -> {};


        f1.apply();
        f2.apply();
        f3.apply(1);
        f4.apply(1);
        f5.apply(1, 2);
        f6.apply(1, 2);
        f7.apply(1, 2, 3);
        f8.apply(1, 2, 3);
        f9.apply(1, 2, 3, 4);
        f10.apply(1, 2, 3, 4);
        f11.apply(1, 2, 3, 4, 5);
        f12.apply(1, 2, 3, 4, 5);
        f13.apply(1, 2, 3, 4, 5, 6);
        f14.apply(1, 2, 3, 4, 5, 6);
        f15.apply(1, 2, 3, 4, 5, 6, 7);
        f16.apply(1, 2, 3, 4, 5, 6, 7);
        f17.apply(1, 2, 3, 4, 5, 6, 7, 8);
        f18.apply(1, 2, 3, 4, 5, 6, 7, 8);
        f19.apply(1, 2, 3, 4, 5, 6, 7, 8, 9);
        f20.apply(1, 2, 3, 4, 5, 6, 7, 8, 9);

    }

}
