package io.github.joke.spockmockable


import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import spock.lang.Specification

@Mockable(SimpleService)
@WebMvcTest(controllers = SimpleController)
abstract class ControllerTestBase extends Specification {
}
