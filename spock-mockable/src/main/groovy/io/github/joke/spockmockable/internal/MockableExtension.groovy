package io.github.joke.spockmockable.internal

import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor
import groovy.util.logging.Slf4j
import io.github.joke.spockmockable.Mockable
import net.bytebuddy.agent.ByteBuddyAgent
import net.bytebuddy.agent.builder.AgentBuilder
import net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy
import net.bytebuddy.agent.builder.ResettableClassFileTransformer
import net.bytebuddy.asm.ModifierAdjustment
import net.bytebuddy.description.modifier.MethodManifestation
import net.bytebuddy.description.modifier.TypeManifestation
import net.bytebuddy.description.type.TypeDescription
import net.bytebuddy.dynamic.DynamicType
import net.bytebuddy.matcher.ElementMatcher
import net.bytebuddy.utility.JavaModule
import org.spockframework.runtime.SpockExecutionException
import org.spockframework.runtime.extension.AbstractGlobalExtension

import java.lang.instrument.Instrumentation

import static net.bytebuddy.agent.builder.AgentBuilder.Default
import static net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.REDEFINITION
import static net.bytebuddy.agent.builder.AgentBuilder.TypeStrategy.Default.REDEFINE
import static net.bytebuddy.description.modifier.Visibility.PROTECTED
import static net.bytebuddy.matcher.ElementMatchers.isFinal
import static net.bytebuddy.matcher.ElementMatchers.isPrivate
import static net.bytebuddy.matcher.ElementMatchers.none

@Slf4j
@CompileStatic
class MockableExtension extends AbstractGlobalExtension {

    static Set<String> discoveredClasses = []
    static Set<String> processedClasses = []
    static Instrumentation instrumentation = ByteBuddyAgent.install()
    static ResettableClassFileTransformer = installTransformation()

    static installTransformation() {
        new Default()
                .ignore(none())
                .with(Listener.instance)
                .with(REDEFINITION)
                .with(RedefinitionListener.instance)
                .with(REDEFINE)
                .with(InstallationListener.instance)
                .type(MockableAnnotationMatcher.instance)
                .transform(Transformer.instance)
                .installOn(instrumentation)
    }

    @Singleton
    private static class MockableAnnotationMatcher implements ElementMatcher<TypeDescription> {
        @Override
        boolean matches(TypeDescription typeDescription) {
            def mockableAnnotation = typeDescription?.getDeclaredAnnotations()?.ofType(Mockable.class)?.load()
            if (mockableAnnotation != null) {
                discoveredClasses.addAll(mockableAnnotation.canonicalClassNames().flatten())
                return false
            }
            if (typeDescription.name in processedClasses) {
                return false
            }
            if (typeDescription.name in discoveredClasses) {
                discoveredClasses - typeDescription.name
                processedClasses + typeDescription.name
                return true
            }
            return false
        }
    }

    @Singleton
    private static class RedefinitionListener extends RedefinitionStrategy.Listener.Adapter {
        @Override
        void onComplete(final int amount, final List<Class<?>> types, final Map<List<Class<?>>, Throwable> failures) {
            if (failures) {
                throw new ClassRedefinitionException(failures.keySet().flatten() as Set<Class<?>>)
            }
        }
    }

    @Singleton
    private static class Transformer implements AgentBuilder.Transformer {
        @Override
        DynamicType.Builder<?> transform(DynamicType.Builder<?> builder, TypeDescription typeDescription, ClassLoader classLoader, JavaModule module) {
            builder.visit(new ModifierAdjustment().withMethodModifiers(isPrivate(), PROTECTED))
                    .visit(new ModifierAdjustment().withMethodModifiers(isFinal(), MethodManifestation.PLAIN))
                    .visit(new ModifierAdjustment().withTypeModifiers(isFinal(), TypeManifestation.PLAIN))
        }
    }

    @Singleton
    private static class InstallationListener extends AgentBuilder.InstallationListener.Adapter {
        @Override
        Throwable onError(final Instrumentation instrumentation, final ResettableClassFileTransformer classFileTransformer, final Throwable throwable) {
            throw new SpockExecutionException("Unable to redefined classes: ${(throwable as ClassRedefinitionException).classes}.", throwable)
        }
    }

    @Singleton
    private static class Listener extends AgentBuilder.Listener.Adapter {
        @Override
        void onError(String typeName, ClassLoader classLoader, JavaModule module, boolean loaded, Throwable throwable) {
            log.error('Unable to redefined classes.', throwable)
        }
    }

    @TupleConstructor
    private static class ClassRedefinitionException extends RuntimeException {
        Set<Class<?>> classes
    }

}
