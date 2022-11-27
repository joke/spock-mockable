package io.github.joke.tests;

final class Static {

    public static final String getName() {
        return Static.class.getName();
    }

    protected static final String getSimpleName() {
        return Static.class.getSimpleName();
    }

    private static final String getCanonicalName() {
        return Static.class.getCanonicalName();
    }
}
