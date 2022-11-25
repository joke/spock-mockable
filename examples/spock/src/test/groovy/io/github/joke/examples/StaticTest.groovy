package io.github.joke.examples

import spock.lang.Specification

class StaticTest extends Specification {

    def 'mock static method'() {
        setup:
        Spy(Utility) // create spy. the instance does not really matter

        def staticCaller = new StaticCaller()

        when: 'call real method'
        def res1 = staticCaller.name

        then:
        1 * Utility.name

        and:
        when: 'mock value'
        def res2 = staticCaller.name

        then:
        1 * Utility.name >> 'mocked'

        and:
        when: 'multiple values'
        def res3 = staticCaller.name
        def res4 = staticCaller.name

        then:
        2 * Utility.name >>> ['first', 'second']

        and:
        when: 'return other value'
        def res5 = staticCaller.name

        then:
        1 * Utility.name >> { callRealMethod() }

        expect:
        res1 == 'Utility'
        res2 == 'mocked'
        res3 == 'first'
        res4 == 'second'
        res5 == 'Utility'
    }

    def 'original response restored'() {
        expect:
        new StaticCaller().name == 'Utility'
    }
}
