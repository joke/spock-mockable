package io.github.joke.spockmockable.tests

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import spock.lang.Specification

@WebMvcTest(controllers = SimpleController)
abstract class ControllerTestBase extends Specification {
}
