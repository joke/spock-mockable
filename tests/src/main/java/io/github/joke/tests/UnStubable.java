package io.github.joke.tests;

final public class UnStubable {

    public final String getName() {
        return UnStubable.class.getName();
    }

    protected final String getSimpleName() {
        return UnStubable.class.getSimpleName();
    }

    private final String getCanonicalName() {
        return UnStubable.class.getCanonicalName();
    }
}
