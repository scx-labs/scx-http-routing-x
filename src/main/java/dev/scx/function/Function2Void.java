package dev.scx.function;

/// Function2Void
///
/// @author scx567888
/// @version 0.0.1
/// @see Function1
@FunctionalInterface
public interface Function2Void<A, B, X extends Throwable> {

    void apply(A a, B b) throws X;

}
