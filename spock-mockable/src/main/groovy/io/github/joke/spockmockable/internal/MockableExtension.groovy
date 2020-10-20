package io.github.joke.spockmockable.internal

import groovy.transform.CompileStatic
import groovy.transform.TupleConstructor
import io.github.joke.spockmockable.Mockable
import net.bytebuddy.agent.builder.AgentBuilder
import net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy
import net.bytebuddy.agent.builder.ResettableClassFileTransformer
import net.bytebuddy.asm.ModifierAdjustment
import net.bytebuddy.description.modifier.MethodManifestation
import net.bytebuddy.description.modifier.TypeManifestation
import net.bytebuddy.description.type.TypeDescription
import net.bytebuddy.matcher.ElementMatcher
import org.spockframework.runtime.SpockExecutionException
import org.spockframework.runtime.extension.AbstractAnnotationDrivenExtension
import org.spockframework.runtime.model.SpecInfo

import java.lang.instrument.Instrumentation

import static net.bytebuddy.agent.builder.AgentBuilder.Default
import static net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.REDEFINITION
import static net.bytebuddy.agent.builder.AgentBuilder.TypeStrategy.Default.REDEFINE
import static net.bytebuddy.description.modifier.Visibility.PROTECTED
import static net.bytebuddy.matcher.ElementMatchers.isFinal
import static net.bytebuddy.matcher.ElementMatchers.isPrivate
import static net.bytebuddy.matcher.ElementMatchers.none
import static org.avaje.agentloader.AgentLoader.loadAgentByMainClass

@CompileStatic
class MockableExtension extends AbstractAnnotationDrivenExtension<Mockable> {

    static Instrumentation instrumentation

    static List<String> registeredTransformersForClasses = []

    @Override
    void visitSpecAnnotation(Mockable annotation, SpecInfo spec) {
        def canonicalClassNames = annotation.canonicalClassNames() as List ?: []
        def unprocessedClasses = canonicalClassNames - registeredTransformersForClasses as List<String>
        if (!unprocessedClasses)
            return

        attachAgent()
        registeredTransformersForClasses += unprocessedClasses
        installTransformation(unprocessedClasses)
    }

    static installTransformation(List<String> unprocessedClasses) {
        new Default()
                .ignore(none())
                .with(REDEFINITION)
                .with(RedefinitionListener.instance)
                .with(REDEFINE)
                .with(InstallationListener.instance)
                .type({ TypeDescription t -> t.name in unprocessedClasses } as ElementMatcher)
                .transform { builder, typeDescription, classLoader, module ->
                    builder.visit(new ModifierAdjustment().withMethodModifiers(isPrivate(), PROTECTED))
                            .visit(new ModifierAdjustment().withMethodModifiers(isFinal(), MethodManifestation.PLAIN))
                            .visit(new ModifierAdjustment().withTypeModifiers(isFinal(), TypeManifestation.PLAIN))
                }
                .installOn(instrumentation)
    }

    private void attachAgent() {
        if (!loadAgentByMainClass(this.class.canonicalName, 'debug=0'))
            throw new SpockExecutionException('Could not attach agent for byte code modification')
    }

    static void agentmain(String arguments, Instrumentation instrumentation) {
        MockableExtension.instrumentation = instrumentation
    }

    @Singleton
    private static class RedefinitionListener extends RedefinitionStrategy.Listener.Adapter {

        Iterable<? extends List<Class<?>>> onError(int index, List<Class<?>> batch, Throwable throwable, List<Class<?>> types) {
            return Collections.emptyList()
        }

        @Override
        void onComplete(final int amount, final List<Class<?>> types, final Map<List<Class<?>>, Throwable> failures) {
            if (failures) {
                throw new ClassRedefinitionException(failures.keySet().flatten() as Set<Class<?>>)
            }
        }
    }

    @Singleton
    private static class InstallationListener extends AgentBuilder.InstallationListener.Adapter {
        @Override
        Throwable onError(final Instrumentation instrumentation, final ResettableClassFileTransformer classFileTransformer, final Throwable throwable) {
            throw new SpockExecutionException("Unable to redefined classes: ${(throwable as ClassRedefinitionException).classes}", throwable)
        }
    }

    @TupleConstructor
    private static class ClassRedefinitionException extends RuntimeException {

        Set<Class<?>> classes

    }

}
