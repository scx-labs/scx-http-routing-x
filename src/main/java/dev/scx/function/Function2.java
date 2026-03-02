package dev.scx.function;

/// Function2
///
/// @author scx567888
/// @version 0.0.1
/// @see Function1
@FunctionalInterface
public interface Function2<A, B, R, X extends Throwable> {

    R apply(A a, B b) throws X;

}
