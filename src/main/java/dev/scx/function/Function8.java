package dev.scx.function;

/// Function8
///
/// @author scx567888
/// @version 0.0.1
/// @see Function1
@FunctionalInterface
public interface Function8<A, B, C, D, E, F, G, H, R, X extends Throwable> {

    R apply(A a, B b, C c, D d, E e, F f, G g, H h) throws X;

}
