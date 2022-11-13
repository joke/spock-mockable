package io.github.joke.spockmockable.hooks

import io.github.joke.spockmockable.hooks.StaticMockExtension.Extractor
import org.spockframework.runtime.model.SpecInfo
import spock.lang.Specification
import spock.lang.Subject

class StaticMockExtensionTest extends Specification {

    @Subject
    def staticMockExtension = Spy(new StaticMockExtension())

    def 'visit spec'() {
        setup:
        SpecInfo specInfo = Mock()

        when:
        staticMockExtension.visitSpec(specInfo)

        then:
        1 * specInfo.addSetupInterceptor(_ as Extractor)
        1 * specInfo.addSetupSpecInterceptor(_ as Extractor)
        1 * staticMockExtension._
        0 * _
    }

}
