package dev.scx.function;

/// Function7
///
/// @author scx567888
/// @version 0.0.1
/// @see Function1
@FunctionalInterface
public interface Function7<A, B, C, D, E, F, G, R, X extends Throwable> {

    R apply(A a, B b, C c, D d, E e, F f, G g) throws X;

}
