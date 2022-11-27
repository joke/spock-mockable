package io.github.joke.tests;

final public class UnMockable {

    public final String getName() {
        return UnMockable.class.getName();
    }

    protected final String getSimpleName() {
        return UnMockable.class.getSimpleName();
    }

    private final String getCanonicalName() {
        return UnMockable.class.getCanonicalName();
    }
}
