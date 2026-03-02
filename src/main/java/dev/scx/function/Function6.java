package dev.scx.function;

/// Function6
///
/// @author scx567888
/// @version 0.0.1
/// @see Function1
@FunctionalInterface
public interface Function6<A, B, C, D, E, F, R, X extends Throwable> {

    R apply(A a, B b, C c, D d, E e, F f) throws X;

}
