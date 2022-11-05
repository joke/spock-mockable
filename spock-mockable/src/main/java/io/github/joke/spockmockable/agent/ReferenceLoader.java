package io.github.joke.spockmockable.agent;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toCollection;
import static java.util.Optional.ofNullable;

@Slf4j
@Getter
@Singleton
public class ReferenceLoader {

    private final SortedSet<String> classes;
    private final SortedSet<String> packages;

    @Inject
    public ReferenceLoader() {
        final Properties properties = new PropertyReader().load();
        classes = extractProperty(properties, "classes");
        packages = extractProperty(properties, "packages");
    }

    private static TreeSet<String> extractProperty(final Properties properties, final String classes) {
        return ofNullable(properties.getProperty(classes))
                .map(string -> string.split(","))
                .map(Arrays::stream)
                .orElseGet(Stream::empty)
                .collect(toCollection(TreeSet::new));
    }

    boolean hasClasses() {
        return !packages.isEmpty() || !classes.isEmpty();
    }
}
