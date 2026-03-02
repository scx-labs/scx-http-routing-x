package dev.scx.function;

/// Function0
///
/// @author scx567888
/// @version 0.0.1
/// @see Function1
@FunctionalInterface
public interface Function0<R, X extends Throwable> {

    R apply() throws X;

}
