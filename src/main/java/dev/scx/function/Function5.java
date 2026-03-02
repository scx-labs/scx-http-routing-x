package dev.scx.function;

/// Function5
///
/// @author scx567888
/// @version 0.0.1
/// @see Function1
@FunctionalInterface
public interface Function5<A, B, C, D, E, R, X extends Throwable> {

    R apply(A a, B b, C c, D d, E e) throws X;

}
