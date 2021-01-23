package io.github.joke.spockmockable;

import spock.lang.Specification;

@Mockable([Person, Person.Address])
abstract class TestBase extends Specification {
}
