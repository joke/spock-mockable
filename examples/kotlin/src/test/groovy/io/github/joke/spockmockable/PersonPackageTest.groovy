package io.github.joke.spockmockable

import org.spockframework.mock.MockUtil
import spock.lang.Specification

@Mockable(packages = "io.github.joke.spockmockable")
class PersonPackageTest extends Specification {

    def mockUtil = new MockUtil()

    def 'final from class is removed'() {
        setup:
        Person person = Mock()

        expect:
        mockUtil.isMock person
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

    def 'final is removed and private on method is now protected'() {
        setup:
        Person person = Mock()

        when:
        def res = person.address.street

        then:
        1 * person.address >> new Address('Yellow Brick Road', 'Blue City')

        expect:
        res == 'Yellow Brick Road'
    }

}
