package io.github.joke.examples;

public class Utility {

    public static boolean state1 = false;
    public static boolean state2 = false;

    public static String getName() {
        return Utility.class.getSimpleName();
    }

    public static void doUnsupported() {
        state2 = true;
        throw new UnsupportedOperationException();
    }
}
