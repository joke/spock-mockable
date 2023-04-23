package io.github.joke.examples;

public class StaticCaller {

    public String getName() {
        return Utility.getName();
    }

    public void callUnsupported() {
        Utility.doUnsupported();
    }

}
