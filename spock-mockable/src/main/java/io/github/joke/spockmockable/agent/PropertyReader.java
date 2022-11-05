package io.github.joke.spockmockable.agent;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spockframework.runtime.extension.ExtensionException;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
@Singleton
@NoArgsConstructor(onConstructor_ = @Inject)
public class PropertyReader {

    private static final String METHODS_FILE = "/META-INF/spock-mockable.properties";

    @SuppressWarnings("OrphanedFormatString")
    protected Properties load() {
        final Properties properties = new Properties();
        try (final InputStream stream = getClass().getResourceAsStream(METHODS_FILE)) {
            if (stream == null) {
                log.warn("@Mockable did not find the generated properties file '{}'. Either you did not annotate any tests or the build setup is broken.", METHODS_FILE);
                return properties;
            }

            properties.load(stream);
            return properties;
        } catch (final IOException e) {
            throw new ExtensionException("Unable to read properties file '%s' containing mockable class information", e)
                    .withArgs(METHODS_FILE);
        }
    }

}
