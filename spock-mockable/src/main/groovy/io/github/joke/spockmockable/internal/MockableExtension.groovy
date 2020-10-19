package io.github.joke.spockmockable.internal

import groovy.transform.CompileStatic
import io.github.joke.spockmockable.Mockable
import net.bytebuddy.agent.builder.AgentBuilder
import net.bytebuddy.agent.builder.ResettableClassFileTransformer
import net.bytebuddy.asm.ModifierAdjustment
import net.bytebuddy.description.type.TypeDescription
import net.bytebuddy.dynamic.DynamicType
import net.bytebuddy.matcher.ElementMatcher
import net.bytebuddy.utility.JavaModule
import org.spockframework.runtime.SpockExecutionException
import org.spockframework.runtime.extension.AbstractAnnotationDrivenExtension
import org.spockframework.runtime.model.SpecInfo

import java.lang.instrument.Instrumentation

import static net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.REDEFINITION
import static net.bytebuddy.agent.builder.AgentBuilder.TypeStrategy.Default.REDEFINE
import static net.bytebuddy.description.modifier.MethodManifestation.PLAIN
import static net.bytebuddy.description.modifier.Visibility.PROTECTED
import static net.bytebuddy.matcher.ElementMatchers.*
import static org.avaje.agentloader.AgentLoader.loadAgentByMainClass

@CompileStatic
class MockableExtension extends AbstractAnnotationDrivenExtension<Mockable> {

    static Instrumentation instrumentation

    static List<String> registeredTransformersForClasses = []

    static void agentmain(String arguments, Instrumentation instrumentation) {
        MockableExtension.instrumentation = instrumentation
    }

    @Override
    void visitSpecAnnotation(Mockable annotation, SpecInfo spec) {
//        attachAgent()
//
//        new AgentBuilder.Default()
//                .ignore(none())
//                .with(REDEFINITION)
////                .with(AgentBuilder.RedefinitionStrategy.Listener.StreamWriting.toSystemError())
//                .with(new ErrorListener() as AgentBuilder.RedefinitionStrategy.Listener)
////                .with(AgentBuilder.Listener.StreamWriting.toSystemError().withTransformationsOnly())
//                .with(new ErrorListener() as AgentBuilder.Listener)
//                .with(REDEFINE)
////                .with(AgentBuilder.InstallationListener.StreamWriting.toSystemError())
//                .with(new ErrorListener() as AgentBuilder.InstallationListener)
//                .type({ TypeDescription t -> t.name in 'io.github.joke.spockmockable.Person1' } as ElementMatcher)
//                .transform { builder, typeDescription, classLoader, module ->
//                    builder.visit(new ModifierAdjustment().withMethodModifiers(isPrivate(), PROTECTED))
//                            .visit(new ModifierAdjustment().withMethodModifiers(isFinal(), PLAIN))
//                }
//                .installOn(instrumentation)
//
//        return

        def canonicalClassNames = annotation.canonicalClassNames() as List ?: []
        def unprocessedClasses = canonicalClassNames - registeredTransformersForClasses as List<String>
        if (!unprocessedClasses)
            return

        attachAgent()
        registeredTransformersForClasses += unprocessedClasses
        installTransformation(unprocessedClasses)
    }

    static installTransformation(List<String> unprocessedClasses) {
        new AgentBuilder.Default()
                .ignore(none())
                .with(REDEFINITION)
                .with(new ErrorListener() as AgentBuilder.InstallationListener)
                .with(REDEFINE)
                .type({ TypeDescription t -> t.name in unprocessedClasses } as ElementMatcher)
                .transform { builder, typeDescription, classLoader, module ->
                    builder.visit(new ModifierAdjustment().withMethodModifiers(isPrivate(), PROTECTED))
                            .visit(new ModifierAdjustment().withMethodModifiers(isFinal(), PLAIN))
                }
                .installOn(instrumentation)
    }

    private void attachAgent() {
        if (!loadAgentByMainClass(this.class.canonicalName, 'debug=0')) {
            throw new SpockExecutionException('Could not attach agent for byte code modification')
        }
    }

    private static class ErrorListener implements AgentBuilder.RedefinitionStrategy.Listener, AgentBuilder.InstallationListener, AgentBuilder.Listener {


        @Override
        void onBatch(final int index, final List<Class<?>> batch, final List<Class<?>> types) {

        }

        @Override
        Iterable<? extends List<Class<?>>> onError(final int index, final List<Class<?>> batch, final Throwable throwable, final List<Class<?>> types) {
//            throw new SpockExecutionException("Unable to redefined classes: ${types}", throwable)
        }

        @Override
        void onComplete(final int amount, final List<Class<?>> types, final Map<List<Class<?>>, Throwable> failures) {
        }

        @Override
        void onBeforeInstall(final Instrumentation instrumentation, final ResettableClassFileTransformer classFileTransformer) {

        }

        @Override
        void onInstall(final Instrumentation instrumentation, final ResettableClassFileTransformer classFileTransformer) {

        }

        @Override
        Throwable onError(final Instrumentation instrumentation, final ResettableClassFileTransformer classFileTransformer, final Throwable throwable) {
            return null
        }

        @Override
        void onReset(final Instrumentation instrumentation, final ResettableClassFileTransformer classFileTransformer) {

        }

        @Override
        void onDiscovery(final String typeName, final ClassLoader classLoader, final JavaModule module, final boolean loaded) {

        }

        @Override
        void onTransformation(final TypeDescription typeDescription, final ClassLoader classLoader, final JavaModule module, final boolean loaded, final DynamicType dynamicType) {

        }

        @Override
        void onIgnored(final TypeDescription typeDescription, final ClassLoader classLoader, final JavaModule module, final boolean loaded) {

        }

        @Override
        void onError(final String typeName, final ClassLoader classLoader, final JavaModule module, final boolean loaded, final Throwable throwable) {
            int a =0
        }

        @Override
        void onComplete(final String typeName, final ClassLoader classLoader, final JavaModule module, final boolean loaded) {

        }
    }

}
