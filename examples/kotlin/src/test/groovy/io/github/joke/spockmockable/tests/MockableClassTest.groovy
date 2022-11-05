package io.github.joke.spockmockable.tests

import org.spockframework.mock.MockUtil
import spock.lang.Specification

class MockableClassTest extends Specification {

    def mockUtil = new MockUtil()

    def 'final from class is removed'() {
        setup:
        Person person = Mock()

        expect:
        mockUtil.isMock person
    }

    def 'final from subclass is removed'() {
        setup:
        Address address = Mock()

        expect:
        mockUtil.isMock address
    }

    def 'final from method is removed'() {
        setup:
        Person person = Mock()

        when:
        def res = person.firstName

        then:
        1 * person.firstName >> 'Dorothy'

        expect:
        res == 'Dorothy'
    }

    def 'private on method is now protected'() {
        setup:
        Person person = Mock()

        when:
        def res = person.lastName

        then:
        1 * person.lastName >> 'Gale'

        expect:
        res == 'Gale'
    }

}
