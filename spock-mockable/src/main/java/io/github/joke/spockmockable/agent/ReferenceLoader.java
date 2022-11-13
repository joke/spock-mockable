package io.github.joke.spockmockable.agent;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Arrays;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toCollection;
import static java.util.Optional.ofNullable;

@Slf4j
@Getter
@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class ReferenceLoader {

    private final PropertyReader propertyReader;

    @Getter(lazy = true, onMethod_ = {@NotNull, @SuppressWarnings("NullAway")})
    private final SortedSet<String> classes = extractProperty("classes");

    @Getter(lazy = true, onMethod_ = {@NotNull, @SuppressWarnings("NullAway")})
    private final SortedSet<String> packages = extractProperty("packages");

    boolean hasClasses() {
        return !getPackages().isEmpty() || !getClasses().isEmpty();
    }

    private TreeSet<String> extractProperty(final String propertyName) {
        return ofNullable(propertyReader)
                .map(PropertyReader::getProperties)
                .map(properties -> properties.getProperty(propertyName))
                .map(string -> string.split(","))
                .map(Arrays::stream)
                .orElseGet(Stream::empty)
                .collect(toCollection(TreeSet::new));
    }
}
