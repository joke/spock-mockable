package io.github.joke.examples

import org.spockframework.mock.MockUtil
import spock.lang.Specification

class SpyTest extends Specification {

    def mockUtil = new MockUtil()

    def 'final from class is removed'() {
        setup:
        def instance = new Person()
        Person person = Spy(instance)

        expect:
        mockUtil.isMock person
    }

    def 'testing class detection'() {
        setup:
        def person = new Person()
        Person personSpy = Spy(person)
        Person.Address address = new Person.Address('my-street')
        def addressSpy = Spy(address)

        when:
        def res = personSpy.address.street

        then:
        1 * personSpy.address >> addressSpy
        1 * addressSpy.street >> 'mocked street'

        expect:
        res == 'mocked street'
    }

}
