package dev.scx.function;

/// Function6Void
///
/// @author scx567888
/// @version 0.0.1
/// @see Function1
@FunctionalInterface
public interface Function6Void<A, B, C, D, E, F, X extends Throwable> {

    void apply(A a, B b, C c, D d, E e, F f) throws X;

}
