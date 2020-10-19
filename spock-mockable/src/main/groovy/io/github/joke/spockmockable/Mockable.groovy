package io.github.joke.spockmockable

import groovy.transform.CompileStatic
import io.github.joke.spockmockable.internal.MockableExtension
import org.codehaus.groovy.transform.GroovyASTTransformationClass
import org.spockframework.runtime.extension.ExtensionAnnotation

import java.lang.annotation.Retention
import java.lang.annotation.Target

import static java.lang.annotation.ElementType.TYPE
import static java.lang.annotation.RetentionPolicy.RUNTIME

@Target(TYPE)
@CompileStatic
@Retention(RUNTIME)
@ExtensionAnnotation(MockableExtension.class)
@GroovyASTTransformationClass('io.github.joke.spockmockable.internal.MockableTransformation')
@interface Mockable {

    Class<?>[] value();

    String[] canonicalClassNames();
}
