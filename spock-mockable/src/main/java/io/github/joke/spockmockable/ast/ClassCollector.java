package io.github.joke.spockmockable.ast;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.SortedSet;
import java.util.TreeSet;

@Getter
@Singleton
@NoArgsConstructor(onConstructor_ = @Inject)
public class ClassCollector {

    private final SortedSet<String> classNames = new TreeSet<>();

    private final SortedSet<String> packageNames = new TreeSet<>();

    public void addClass(final Class<?> clazz) {
        classNames.add(clazz.getName());
    }

    public void addClass(final String className) {
        classNames.add(className);
    }

    public void addPackage(final String packageName) {
        packageNames.add(packageName);
    }
}
