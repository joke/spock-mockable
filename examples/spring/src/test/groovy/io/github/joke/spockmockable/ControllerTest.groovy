package io.github.joke.spockmockable

import org.spockframework.spring.SpringBean
import org.spockframework.spring.SpringSpy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder.*
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

@Mockable(SimpleService)
@WebMvcTest(controllers = SimpleController)
class ControllerTest extends Specification {

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
