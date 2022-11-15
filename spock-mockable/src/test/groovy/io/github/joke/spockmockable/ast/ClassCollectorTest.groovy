package io.github.joke.spockmockable.ast

import spock.lang.Specification
import spock.lang.Subject

class ClassCollectorTest extends Specification {

    @Subject
    def classCollector = new ClassCollector()

    def 'add class'() {
        setup:
        classCollector.addClass(String)

        expect:
        classCollector.classNames ==~ ['java.lang.String']
    }

    def 'add class by name'() {
        setup:
        classCollector.addClass('java.lang.String')

        expect:
        classCollector.classNames ==~ ['java.lang.String']
    }

    def 'add package'() {
        setup:
        classCollector.addPackage('java.lang')

        expect:
        classCollector.packageNames ==~ ['java.lang']
    }
}
