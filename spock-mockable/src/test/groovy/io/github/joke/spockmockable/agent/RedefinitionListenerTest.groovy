package io.github.joke.spockmockable.agent

import io.github.joke.spockoutputcapture.OutputCapture
import spock.lang.Execution
import spock.lang.Specification
import spock.lang.Stepwise
import spock.lang.Subject

import static org.spockframework.runtime.model.parallel.ExecutionMode.SAME_THREAD

@Stepwise
@Execution(SAME_THREAD)
class RedefinitionListenerTest extends Specification {

    @OutputCapture
    logs

    @Subject
    def redefinitionListener = new RedefinitionListener()

    def 'on complete'() {
        setup:
        redefinitionListener.onComplete(123, [String], [([String]): new RuntimeException('something bad')])

        expect:
        logs ==~ /(?sm)^.*DEBUG.*Successfully transformed classes: \[class java\.lang\.String\].*$/
    }

    def 'do not log completion on empty list'() {
        setup:
        redefinitionListener.onComplete(123, [], [([String]): new RuntimeException('something bad')])

        expect:
        logs !==~ /(?sm)^.*DEBUG.*Successfully transformed classes: \[class java\.lang\.String\].*$/
    }

    def 'on error'() {
        setup:
        redefinitionListener.onError(123, [String], new RuntimeException('something bad'), [String])

        expect:
        logs ==~ /(?sm)^.*WARN.*Could not transform classes. something bad: \[class java\.lang\.String\].*$/
    }
}
