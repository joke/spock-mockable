package io.github.joke.spockmockable.ast

import org.codehaus.groovy.ast.AnnotatedNode
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.ClassNode
import spock.lang.Specification
import spock.lang.Subject

import static io.github.joke.spockmockable.ast.IsolationAnnotator.ISOLATED

class IsolationAnnotatorTest extends Specification {

    ClassNode classNode = Mock()

    @Subject
    def isolationAnnotator = new IsolationAnnotator(classNode)

    def 'add isolation annotation'() {
        when:
        isolationAnnotator.addIsolationAnnotation()

        then:
        1 * classNode.getAnnotations(ISOLATED) >> []
        1 * classNode.addAnnotation({ AnnotationNode it ->
            it.classNode.name == 'spock.lang.Isolated'
            it.members.value.text == 'Static method mock'
        } as AnnotationNode)
    }

    def 'add isolation annotation already exists'() {
        setup:
        AnnotatedNode annotatedNode = Mock()

        when:
        isolationAnnotator.addIsolationAnnotation()

        then:
        1 * classNode.getAnnotations(ISOLATED) >> [annotatedNode]
        0 * _
    }
}
