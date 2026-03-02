package dev.scx.function;

/// Function1Void
///
/// @author scx567888
/// @version 0.0.1
/// @see Function1
@FunctionalInterface
public interface Function1Void<A, X extends Throwable> {

    void apply(A a) throws X;

}
