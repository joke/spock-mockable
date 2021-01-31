package io.github.joke.spockmockable;

import org.codehaus.groovy.transform.GroovyASTTransformationClass;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target(TYPE)
@Retention(RUNTIME)
@GroovyASTTransformationClass("io.github.joke.spockmockable.internal.MockableASTTransformation")

public @interface Mockable {

    /**
     * Classes that should be made mockable
     */
    Class<?>[] value() default {};

}