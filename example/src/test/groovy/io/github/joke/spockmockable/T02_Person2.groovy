package io.github.joke.spockmockable

import io.github.joke.spockmockable.Mockable
import io.github.joke.spockmockable.Person2
import spock.lang.Specification

@Mockable(Person2)
class T02_Person2 extends Specification {

    def 'none static with constructor'() {
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
