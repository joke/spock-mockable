package io.github.joke.spockmockable

import io.github.joke.spockmockable.Mockable
import io.github.joke.spockmockable.Person1
import spock.lang.Ignore
import spock.lang.Specification

//@Mockable(Person1)
class T01_Person1 extends Specification {

    def 'none static with constructor'() {
        setup:
        def person = Spy(new Person1())

        when:
        def g = person.greeting()

        then:
        person.greeting() >> 'es war einmal'

        expect:
        g == 'es war einmal'
    }

}
