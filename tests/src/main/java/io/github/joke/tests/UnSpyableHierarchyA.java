package io.github.joke.tests;

public class UnSpyableHierarchyA extends UnSpyableHierarchyB {

    public final String getAName() {
        return getBName();
    }
}
