package dev.scx.function;

/// Function9Void
///
/// @author scx567888
/// @version 0.0.1
/// @see Function1
@FunctionalInterface
public interface Function9Void<A, B, C, D, E, F, G, H, I, X extends Throwable> {

    void apply(A a, B b, C c, D d, E e, F f, G g, H h, I i) throws X;

}
