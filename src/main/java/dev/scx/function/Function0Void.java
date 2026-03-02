package dev.scx.function;

/// Function0Void
///
/// @author scx567888
/// @version 0.0.1
/// @see Function1
@FunctionalInterface
public interface Function0Void<X extends Throwable> {

    void apply() throws X;

}
