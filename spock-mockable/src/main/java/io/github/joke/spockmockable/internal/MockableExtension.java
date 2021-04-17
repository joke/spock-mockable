package io.github.joke.spockmockable.internal;

import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.agent.builder.AgentBuilder.InitializationStrategy;
import net.bytebuddy.agent.builder.ResettableClassFileTransformer;
import net.bytebuddy.asm.ModifierAdjustment;
import net.bytebuddy.description.modifier.MethodManifestation;
import net.bytebuddy.description.modifier.TypeManifestation;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.utility.JavaModule;
import org.slf4j.Logger;
import org.spockframework.runtime.extension.AbstractGlobalExtension;
import org.spockframework.runtime.extension.ExtensionException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.instrument.Instrumentation;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Stream;

import static java.lang.Boolean.parseBoolean;
import static java.lang.System.getProperty;
import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.toSet;
import static net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.RETRANSFORMATION;
import static net.bytebuddy.agent.builder.AgentBuilder.TypeStrategy.Default.REDEFINE;
import static net.bytebuddy.description.modifier.Visibility.PROTECTED;
import static net.bytebuddy.matcher.ElementMatchers.isFinal;
import static net.bytebuddy.matcher.ElementMatchers.isPrivate;
import static net.bytebuddy.matcher.ElementMatchers.none;
import static org.slf4j.LoggerFactory.getLogger;

public class MockableExtension extends AbstractGlobalExtension {

    private static final Logger log = getLogger(MockableExtension.class);

    private static final String PROPERTIES_FILE = "/META-INF/spock-mockable.properties";

    private static Instrumentation instrumentation;

    static {
        if (parseBoolean(getProperty("spock-mockable.disabled", "false"))) {
            log.info("@Mockable transformation is disabled by system property 'spock-mockable.disabled=true'");
        } else {
            log.info("Activating @Mockable transformation");
            instrumentation = ByteBuddyAgent.install();
            installTransformer();
        }
    }

    private static void installTransformer() {
        buildAndInstallTransformer(extractClassesFromPropertyResource());
    }

    private static Set<String> extractClassesFromPropertyResource() {
        try (final InputStream stream = MockableExtension.class.getResourceAsStream(PROPERTIES_FILE)) {
            if (stream == null) {
                log.warn("@Mockable did not find the generated properties file '{}'. Either you did not annotate any tests or the build setup is broken.", PROPERTIES_FILE);
                return emptySet();
            }

            return readClassesFromProperties(stream);
        } catch (final IOException e) {
            throw new ExtensionException("Unable to read properties file '%s' containing mockable class information", e)
                    .withArgs(PROPERTIES_FILE);
        }
    }

    private static Set<String> readClassesFromProperties(final InputStream stream) throws IOException {
        final Properties properties = new Properties();
        properties.load(stream);

        return Stream.of(properties.getProperty("classes", "")
                .split(","))
                .collect(toSet());
    }

    private static void buildAndInstallTransformer(final Set<String> classes) {
        new AgentBuilder.Default()
                .ignore(none())
                .with(new InstallationListener())
                .with(new DiscoveryListener())
                .with(InitializationStrategy.NoOp.INSTANCE)
                .with(RETRANSFORMATION)
                .with(REDEFINE)
                .type(typeDescription -> classes.contains(typeDescription.getName()))
                .transform(MockableExtension::transform)
                .installOn(instrumentation);
    }

    private static DynamicType.Builder<?> transform(final DynamicType.Builder<?> builder, final TypeDescription typeDescription, final ClassLoader classLoader, final JavaModule module) {
        return builder.visit(new ModifierAdjustment().withMethodModifiers(isPrivate(), PROTECTED))
                .visit(new ModifierAdjustment().withMethodModifiers(isFinal(), MethodManifestation.PLAIN))
                .visit(new ModifierAdjustment().withTypeModifiers(isFinal(), TypeManifestation.PLAIN));
    }

    private static class InstallationListener extends AgentBuilder.InstallationListener.Adapter {
        @Override
        public Throwable onError(final Instrumentation instrumentation, final ResettableClassFileTransformer classFileTransformer, final Throwable throwable) {
            throw new ExtensionException("Unable install spock-mockable transformation", throwable);
        }
    }

    private static class DiscoveryListener extends AgentBuilder.Listener.Adapter {
        @Override
        public void onError(final String typeName, final ClassLoader classLoader, final JavaModule module, final boolean loaded, final Throwable throwable) {
            log.warn("Could not transform class '{}'", typeName, throwable);
        }

        @Override
        public void onComplete(final String typeName, final ClassLoader classLoader, final JavaModule module, final boolean loaded) {
            log.trace("Transformed class {}'", typeName);
        }
    }

}
