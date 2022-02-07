package io.github.joke.spockmockable

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import spock.lang.Specification

@Mockable(packages = "io.github.joke.spockmockable")
@WebMvcTest(controllers = SimpleController)
class ControllerTestWithMockablePackage extends Specification {

    @SpringBean
    SimpleService simpleService = Mock()

    @Autowired
    MockMvc mockMvc

    def 'final from class is removed'() {
        when:
        def response = mockMvc.perform(get('/name')).andReturn().response

        then:
        1 * simpleService.name >> 'This is a mock'

        expect:
        response.contentAsString == 'This is a mock'
    }

}
