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
class ClassCollector {

    private final SortedSet<String> classNames = new TreeSet<>();

    private final SortedSet<String> packageNames = new TreeSet<>();

    void addClass(final Class<?> clazz) {
        classNames.add(clazz.getName());
    }

    void addClass(final String className) {
        classNames.add(className);
    }

    void addPackage(final String packageName) {
        packageNames.add(packageName);
    }

}
