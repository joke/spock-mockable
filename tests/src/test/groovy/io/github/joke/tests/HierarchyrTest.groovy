package io.github.joke.tests

import spock.lang.Specification

class HierarchyrTest extends Specification {

    UnSpyableHierarchyA unSpyableHierarchyA = Spy()

    def 'mock base class'() {
        when:
        def res = unSpyableHierarchyA.AName

        then:
        unSpyableHierarchyA.CName >> 'hello'

        expect:
        res == 'hello'
    }
}
