package io.github.joke.spockmockable

import org.spockframework.mock.MockUtil

class PersonTestWithInheritance extends TestBase {

    def mockUtil = new MockUtil()

    def 'final from class is removed'() {
        setup:
        Person person = Mock()

        expect:
        mockUtil.isMock person
    }

    def 'final from subclass is removed'() {
        setup:
        Person.Address address = Mock()

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

    def 'privat on method is now protected'() {
        setup:
        Person person = Mock()

        when:
        def res = person.lastName

        then:
        1 * person.lastName >> 'Gale'

        expect:
        res == 'Gale'
    }

    def 'final is removed and privat on method is now protected'() {
        setup:
        Person person = Mock()

        when:
        def res = person.address.street

        then:
        1 * person.address >> new Person.Address('Yellow Brick Road')

        expect:
        res == 'Yellow Brick Road'
    }

}
