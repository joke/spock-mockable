package io.github.joke.spockmockable.internal

import groovy.transform.VisibilityOptions

import java.lang.annotation.Retention
import java.lang.annotation.Target

import static groovy.transform.options.Visibility.PACKAGE_PRIVATE
import static java.lang.annotation.ElementType.TYPE
import static java.lang.annotation.RetentionPolicy.RUNTIME

@Target(TYPE)
@Retention(RUNTIME)
@VisibilityOptions(PACKAGE_PRIVATE)
@interface Mockable {

    /**
     * Used internally by {@link io.github.joke.spockmockable.internal.MockableASTTransformation}
     */
    String[] value() default [];

}