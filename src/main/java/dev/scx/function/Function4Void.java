package dev.scx.function;

/// Function4Void
///
/// @author scx567888
/// @version 0.0.1
/// @see Function1
@FunctionalInterface
public interface Function4Void<A, B, C, D, X extends Throwable> {

    void apply(A a, B b, C c, D d) throws X;

}
