package io.github.joke.spockmockable.tests

import io.github.joke.spockmockable.Mockable
import org.spockframework.mock.MockUtil
import spock.lang.Specification

@Mockable(Company)
class CompanyTest extends Specification {
    def mockUtil = new MockUtil()

    def 'final class is mocked'() {
        given: 'a company mock'
        Company company = Mock()

        expect: 'instance is mocked'
        mockUtil.isMock(company)
    }

    def 'final modifier from method which contains a lambda is public'() {
        given: 'a company mock'
        Company company = Mock() {
            hire(_) >> [new Person('Harley', 'Quinn', new Address('New Jersey Street', 'Gotham City'))]
        }

        and: 'a bunch of people looking to be hired'
        def wantingToBeHired = [
                new Person('Kal', 'El', new Address('Delaware Street', 'Metropolis')),
                new Person('Bruce', 'Wayne', new Address('New York Street', 'Gotham City'))]

        when: 'the company tries to hire some people'
        def hired = company.hire(wantingToBeHired)

        then:
        with(hired) {
            assert size() == 1
            assert get(0).firstName == 'Harley'
            assert get(0).lastName == 'Quinn'
            assert get(0).address.city == 'Gotham City'
        }
    }
}
