package io.github.joke.spockmockable.agent

import io.github.joke.spockoutputcapture.OutputCapture
import spock.lang.Specification
import spock.lang.Subject

class RedefinitionListenerTest extends Specification {

    @OutputCapture
    logs

    @Subject
    def redifinitionListener = new RedefinitionListener()

    def 'on complete'() {
        setup:
        redifinitionListener.onComplete(123, [String], [([String]): new RuntimeException('something bad')])

        expect:
        logs ==~ /(?sm)^.*DEBUG.*Successfully transformed classes: '\[class java\.lang\.String\]'.*$/
    }

    def 'on error'() {
        setup:
        redifinitionListener.onError(123, [String], new RuntimeException('something bad'), [String])

        expect:
        logs ==~ /(?sm)^.*WARN.*Could not transform classes: '\[class java\.lang\.String\]'.*$/
    }
}
