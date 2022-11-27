package io.github.joke.tests

import spock.lang.Shared
import spock.lang.Specification

import static java.lang.reflect.Modifier.isFinal
import static java.lang.reflect.Modifier.isProtected
import static java.lang.reflect.Modifier.isPublic
import static org.spockframework.util.ReflectionUtil.getDeclaredMethodByName

class TransformationTest extends Specification {

    UnSpyable unSpyable = Spy()
    UnStubable unStubable = Stub()
    UnMockable unMockable = Mock()

    @Shared
    def classes = [UnMockable, UnSpyable, UnStubable] as List<Class<?>>

    def 'class is transformed'() {
        setup:
        def modifier = clazz.modifiers

        expect:
        !isFinal(modifier)

        where:
        clazz << classes
    }

    def 'getName is transformed'() {
        setup:
        def modifier = getDeclaredMethodByName(clazz, 'getName').modifiers

        expect:
        isPublic(modifier)
        !isFinal(modifier)

        where:
        clazz << classes
    }

    def 'getSimpleName is transformed'() {
        setup:
        def modifier = getDeclaredMethodByName(clazz, 'getSimpleName').modifiers

        expect:
        isProtected(modifier)
        !isFinal(modifier)

        where:
        clazz << classes
    }

    def 'getCanonicalName is transformed'() {
        setup:
        def modifier = getDeclaredMethodByName(clazz, 'getCanonicalName').modifiers

        expect:
        isProtected(modifier)
        !isFinal(modifier)

        where:
        clazz << classes
    }
}
