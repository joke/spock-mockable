package io.github.joke.spockmockable.agent

import io.github.joke.spockoutputcapture.OutputCapture
import spock.lang.Isolated
import spock.lang.Specification
import spock.lang.Subject

@Isolated
class TransformationStarterTest extends Specification {

    @OutputCapture logs

    ReferenceLoader referenceLoader = Mock()
    ClassTransformer classTransformer = Mock()

    @Subject
    def transformationStarter = new TransformationStarter(referenceLoader, classTransformer)

    def 'start if enabled'() {
        setup:
        System.setProperty('spock-mockable.disabled', 'false')

        when:
        transformationStarter.start()

        then:
        1 * referenceLoader.hasClasses() >> true
        1 * classTransformer.applyTransformation()
        0 * _

        expect:
        logs !==~ /(?sm)^.*@Mockable transformation is disabled.*$/
    }

    def 'not started twice'() {
        setup:
        System.setProperty('spock-mockable.disabled', 'false')

        when:
        transformationStarter.start()
        transformationStarter.start()

        then:
        1 * referenceLoader.hasClasses() >> true
        1 * classTransformer.applyTransformation()
        0 * _
    }

    def 'do not start if disabled'() {
        setup:
        System.setProperty('spock-mockable.disabled', 'true')

        when:
        transformationStarter.start()

        then:
        0 * _

        expect:
        logs ==~ /(?sm)^.*@Mockable transformation is disabled by system property 'spock-mockable.disabled=true'.*$/
    }

    def 'do not start if classes are empty'() {
        setup:
        System.setProperty('spock-mockable.disabled', 'false')

        when:
        transformationStarter.start()

        then:
        1 * referenceLoader.hasClasses() >> false
        0 * _
    }

    def 'do not start if classes are empty'() {
        setup:
        System.setProperty('spock-mockable.disabled', 'false')

        when:
        transformationStarter.start()

        then:
        1 * referenceLoader.hasClasses() >> false
        0 * _
    }

}
