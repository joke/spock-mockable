package io.github.joke.spockmockable.ast

import io.github.joke.spockmockable.ast.visitors.SpecificationVisitor
import spock.lang.Specification
import spock.lang.Subject

class SpecificationProcessorTest extends Specification {

    @Subject
    SpecificationProcessor specificationProcessor = Mock()

    SpecificationVisitor specificationVisitor = Mock()

    def 'process'() {
        when:
        specificationProcessor.process()

        then:
        1 * specificationVisitor.visit()
        1 * specificationProcessor.process() >> { callRealMethod() }
        1 * specificationProcessor.specificationVisitor() >> specificationVisitor
        0 * _
    }

}
