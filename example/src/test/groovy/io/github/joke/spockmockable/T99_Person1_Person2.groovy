package io.github.joke.spockmockable

import spock.lang.Ignore
import spock.lang.Specification

//@Ignore
@Mockable([Person1, Person2])
class T99_Person1_Person2 extends Specification {

    def 'none static with constructor 1'() {
        setup:
        def person = Spy(new Person1())

        when:
        def g = person.greeting()

        then:
        person.greeting() >> 'es war einmal'

        expect:
        g == 'es war einmal'
    }

    def 'none static with constructor 2'() {
        setup:
        def person = Spy(new Person2())

        when:
        def g = person.greeting()

        then:
        person.greeting() >> 'es war einmal'

        expect:
        g == 'es war einmal'
    }
}
