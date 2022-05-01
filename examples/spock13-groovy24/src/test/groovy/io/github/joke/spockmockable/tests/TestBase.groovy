package io.github.joke.spockmockable.tests

import io.github.joke.spockmockable.Mockable;
import spock.lang.Specification

@Mockable([Person, Person.Address])
abstract class TestBase extends Specification {
}
