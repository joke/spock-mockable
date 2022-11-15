package io.github.joke.spockmockable.ast

import org.codehaus.groovy.control.SourceUnit
import spock.lang.Specification
import spock.lang.Subject

import static org.codehaus.groovy.ast.ClassHelper.makeWithoutCaching

class SourceUnitProcessorTest extends Specification {

    @Subject
    SourceUnitProcessor sourceUnitProcessor = Mock()

    SourceUnit sourceUnit = DeepMock()
    SpecificationProcessor.Factory factory = Mock()
    SpecificationProcessor specificationProcessor = Mock()

    def stringNodeClass = makeWithoutCaching(String)
    def specificationNodeClass = makeWithoutCaching(SourceUnitProcessorTest)

    def 'process'() {
        when:
        sourceUnitProcessor.process()

        then:
        1 * sourceUnitProcessor.sourceUnit() >> sourceUnit
        1 * sourceUnitProcessor.process() >> { callRealMethod() }
        1 * sourceUnitProcessor.specificationProcessorFactory() >> factory
        1 * sourceUnit.AST.classes >> [specificationNodeClass, stringNodeClass]
        1 * factory.create(specificationNodeClass) >> specificationProcessor
        1 * specificationProcessor.process() >> {}
    }
}
