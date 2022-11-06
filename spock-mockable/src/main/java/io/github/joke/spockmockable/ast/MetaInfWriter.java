package io.github.joke.spockmockable.ast;

import lombok.RequiredArgsConstructor;
import org.codehaus.groovy.control.CompilationUnit;

import javax.inject.Inject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import static java.lang.String.join;

@RequiredArgsConstructor(onConstructor_ = @Inject)
class MetaInfWriter {

    private final CompilationUnit compilationUnit;

    private final ClassCollector classCollector;

    void write() {
        final String classes = join(",", classCollector.getClassNames());
        final String packages = join(",", classCollector.getPackageNames());

        final File metaInf = new File(compilationUnit.getConfiguration().getTargetDirectory(), "META-INF");
        metaInf.mkdirs();
        final File propertyFile = new File(metaInf, "spock-mockable.properties");
        final Properties properties = new Properties();
        properties.put("classes", classes);
        properties.put("packages", packages);

        try (final FileOutputStream output = new FileOutputStream(propertyFile)) {
            properties.store(output, "io.github.joke:spock-mockable");
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

}