package io.github.joke.spockmockable;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target(TYPE)
@Retention(RUNTIME)
@Repeatable(Mockables.class)
public @interface Mockable {

    /**
     * Addition a class name {@link java.lang.Class#getName()} to be made mockable.
     * Normally spock-mockable will automatically detect which classes need to be made mockable.
     * Under special circumstances however the type can not be detected automatically. In this particular cases you
     * can specify a class name manually.
     * This is especially useful if the class is generated at runtime.
     */
    String className();

    /**
     * Addition a class name {@link java.lang.Package#getName()} to be made mockable.
     * Normally spock-mockable will automatically detect which classes need to be made mockable.
     * Under special circumstances however the type can not be detected automatically. In this particular cases you
     * can specify a class name manually.
     * This is especially useful if the class is generated at runtime.
     */

    String packageName();

}
