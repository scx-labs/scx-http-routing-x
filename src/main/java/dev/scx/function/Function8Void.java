package dev.scx.function;

/// Function8Void
///
/// @author scx567888
/// @version 0.0.1
/// @see Function1
@FunctionalInterface
public interface Function8Void<A, B, C, D, E, F, G, H, X extends Throwable> {

    void apply(A a, B b, C c, D d, E e, F f, G g, H h) throws X;

}
