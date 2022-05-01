package io.github.joke.spockmockable.tests

import io.github.joke.spockmockable.Mockable
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import spock.lang.Specification

@Mockable(SimpleService)
@WebMvcTest(controllers = SimpleController)
abstract class ControllerTestBase extends Specification {
}
