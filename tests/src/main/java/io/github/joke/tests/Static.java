package io.github.joke.tests;

final class Static {

    public static boolean stateA = false;
    public static boolean stateB = false;

    public static final String getName() {
        return Static.class.getName();
    }

    protected static final String getSimpleName() {
        return Static.class.getSimpleName();
    }

    private static final String getCanonicalName() {
        return Static.class.getCanonicalName();
    }

    public static final void modifyStateAndException() {
        stateA = false;
        throw new UnsupportedOperationException();
    }
}
