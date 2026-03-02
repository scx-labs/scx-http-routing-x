package dev.scx.function;

/// Function4
///
/// @author scx567888
/// @version 0.0.1
/// @see Function1
@FunctionalInterface
public interface Function4<A, B, C, D, R, X extends Throwable> {

    R apply(A a, B b, C c, D d) throws X;

}
