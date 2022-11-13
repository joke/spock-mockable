package io.github.joke.spockmockable.hooks

import org.junit.platform.engine.EngineDiscoveryRequest
import org.junit.platform.engine.ExecutionRequest
import org.junit.platform.engine.UniqueId
import spock.lang.Specification
import spock.lang.Subject

class EngineTest extends Specification {

    @Subject
    def engine = Spy(new Engine())

    def 'get id'() {
        when:
        def res = engine.id

        then:
        1 * engine._
        0 * _

        expect:
        res == 'spock-mockable'
    }

    def 'discover'() {
        setup:
        UniqueId uniqueId = Mock()
        EngineDiscoveryRequest engineDiscoveryRequest = Mock()

        when:
        def res = engine.discover(engineDiscoveryRequest, uniqueId)

        then:
        1 * engine.id >> 'spock-mockable'
        1 * engine._
        0 * _

        expect:
        res.uniqueId == uniqueId
        res.displayName == 'spock-mockable'
    }

    def 'execute'() {
        setup:
        ExecutionRequest executionRequest = Mock()

        when:
        engine.execute(executionRequest)

        then:
        1 * engine._
        0 * _
    }

}
