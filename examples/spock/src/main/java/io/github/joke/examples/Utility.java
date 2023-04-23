package io.github.joke.examples;

public class Utility {

    public static boolean state = false;

    public static String getName() {
        return Utility.class.getSimpleName();
    }

    public static void doUnsupported() {
        throw new UnsupportedOperationException();
    }
}
