package io.github.joke.spockmockable

import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get

@Mockable(SimpleService)

class ControllerTestFromBase extends ControllerTestBase {

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
