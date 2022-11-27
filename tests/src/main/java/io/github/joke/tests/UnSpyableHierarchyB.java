package io.github.joke.tests;

public class UnSpyableHierarchyB extends UnSpyableHierarchyC {

    protected final String getBName() {
        return getCName();
    }
}
