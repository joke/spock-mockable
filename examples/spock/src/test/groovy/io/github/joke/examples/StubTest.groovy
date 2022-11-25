package io.github.joke.examples

import org.spockframework.mock.MockUtil
import spock.lang.Specification

class StubTest extends Specification {

    def mockUtil = new MockUtil()

    def 'final from class is removed'() {
        setup:
        Person person = Stub()

        expect:
        mockUtil.isMock person
    }

    def 'final from subclass is removed'() {
        setup:
        Person.Address address = Stub()

        expect:
        mockUtil.isMock address
    }

    def 'final from method is removed'() {
        setup:
        Person person = Stub()

        when:
        def res = person.firstName

        then:
        person.firstName >> 'Dorothy'

        expect:
        res == 'Dorothy'
    }

    def 'private on method is now protected'() {
        setup:
        Person person = Stub()

        when:
        def res = person.lastName

        then:
        person.lastName >> 'Gale'

        expect:
        res == 'Gale'
    }

    def 'final is removed and private on method is now protected'() {
        setup:
        Person person = Stub()

        when:
        def res = person.address.street

        then:
        person.address >> new Person.Address('Yellow Brick Road')

        expect:
        res == 'Yellow Brick Road'
    }

}
