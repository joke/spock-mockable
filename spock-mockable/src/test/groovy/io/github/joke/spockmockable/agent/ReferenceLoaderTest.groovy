package io.github.joke.spockmockable.agent

import spock.lang.Specification
import spock.lang.Subject

class ReferenceLoaderTest extends Specification {

    PropertyReader propertyReader = DeepMock()

    @Subject
    def referenceLoader = Spy(new ReferenceLoader(propertyReader))

    def 'has classes'() {
        when:
        def res = referenceLoader.hasClasses()

        then:
        referenceLoader.extractProperty('classes') >> (classes as SortedSet)
        referenceLoader.extractProperty('packages') >> (packages as SortedSet)

        expect:
        res == expected

        where:
        classes | packages || expected
        []      | []       || false
        ['c']   | []       || true
        []      | ['p']    || true
        ['c']   | ['p']    || true
    }

    def 'extract property'() {
        setup:
        when:
        def res = referenceLoader.extractProperty('classes')

        then:
        1 * propertyReader.properties.getProperty('classes') >> 'classB,classA'
        1 * referenceLoader._
        0 * _

        expect:
        res ==~ ['classA', 'classB']
    }
}
