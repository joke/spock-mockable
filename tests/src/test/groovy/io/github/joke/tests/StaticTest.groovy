package io.github.joke.tests

import spock.lang.Specification

class StaticTest extends Specification {

    def setupSpec() {
        Spy(Static)
    }

    def 'class is transformed'() {
        when:
        def res = Static."$methodName"

        then:
        Static."$methodName" >> 'Hello'
        0 * _

        expect:
        res == 'Hello'

        where:
        methodName << ['name', 'simpleName', 'canonicalName']
    }
}
