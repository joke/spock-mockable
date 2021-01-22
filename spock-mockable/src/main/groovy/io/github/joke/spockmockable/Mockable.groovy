package io.github.joke.spockmockable

import groovy.transform.Canonical
import groovy.transform.CompileStatic
import io.github.joke.spockmockable.internal.MockableASTTransformation
import io.github.joke.spockmockable.internal.MockableExtension
import org.codehaus.groovy.transform.GroovyASTTransformationClass
import org.spockframework.runtime.extension.AbstractGlobalExtension
import org.spockframework.runtime.extension.ExtensionAnnotation

import java.lang.annotation.Retention
import java.lang.annotation.Target
import java.lang.reflect.Array

import static java.lang.annotation.ElementType.TYPE
import static java.lang.annotation.RetentionPolicy.RUNTIME

/**
 * Increase mockability of classes by replacing private and final via byte code modification.
 *
 * @Mockable can be used in combination with <a href="http://spockframework.org/">Spock Framework</a>.
 *
 * Spock is not capable of mocking private or final classes or methods because they are not accessible via inheritance.
 * spock-mockable uses JVM instrumentation to modify these classes upon class loading and apply looser access
 * restrictions. In consequence Spock then capable of mocking these classes.
 */
@Target(TYPE)
@Retention(RUNTIME)
@GroovyASTTransformationClass(classes = MockableASTTransformation.class)
@interface Mockable {

    /**
     * Classes that should be made mockable
     */
    Class<?>[] value();

    /**
     * Used internally by {@link MockableASTTransformation}
     */
    String[] canonicalClassNames() default [];
}
