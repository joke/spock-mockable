package io.github.joke.spockmockable.agent

import net.bytebuddy.asm.AsmVisitorWrapper.ForDeclaredMethods
import net.bytebuddy.asm.ModifierAdjustment
import net.bytebuddy.description.modifier.FieldManifestation
import net.bytebuddy.description.modifier.MethodManifestation
import net.bytebuddy.description.modifier.TypeManifestation
import net.bytebuddy.description.modifier.Visibility
import net.bytebuddy.description.type.TypeDescription
import net.bytebuddy.dynamic.DynamicType.Builder
import net.bytebuddy.matcher.ElementMatchers
import net.bytebuddy.pool.TypePool.Resolution.NoSuchTypeException
import net.bytebuddy.utility.JavaModule
import org.spockframework.mock.ISpockMockObject
import org.spockframework.mock.runtime.ByteBuddyInvoker
import spock.lang.Specification
import spock.lang.Subject

import java.lang.instrument.Instrumentation
import java.security.ProtectionDomain

class ClassTransformerTest extends Specification {

    ReferenceLoader referenceLoader = Mock()
    Instrumentation instrumentation = Mock()
    DiscoveryListener discoveryListener = Mock()
    InstallationListener installationListener = Mock()
    RedefinitionListener redefinitionListener = Mock()

    @Subject
    def classTransformer = Spy(new ClassTransformer(referenceLoader, instrumentation, discoveryListener, installationListener, redefinitionListener))

    def 'transform'() {
        setup:
        Builder<?> builder = Mock()
        TypeDescription typeDescription = Mock()
        ClassLoader classLoader = Mock()
        JavaModule javaModule = Mock()
        ProtectionDomain protectionDomain = Mock()
        ModifierAdjustment privateClassAdjustment = Mock()
        ModifierAdjustment protectedMethodAdjustment = Mock()
        ModifierAdjustment finalMethodAdjustment = Mock()
        ModifierAdjustment privateFieldAdjustment = Mock()
        ForDeclaredMethods staticMethodAdvice = Mock()

        when:
        def res = classTransformer.transform(builder, typeDescription, classLoader, javaModule, protectionDomain)

        then:
        classTransformer.privateClassAdjustment() >> privateClassAdjustment
        classTransformer.protectedMethodAdjustment() >> protectedMethodAdjustment
        classTransformer.finalMethodAdjustment() >> finalMethodAdjustment
        classTransformer.privateFieldAdjustment() >> privateFieldAdjustment
        classTransformer.staticMethodAdvice() >> staticMethodAdvice

        1 * builder.visit(privateClassAdjustment) >> builder
        1 * builder.visit(protectedMethodAdjustment) >> builder
        1 * builder.visit(finalMethodAdjustment) >> builder
        1 * builder.visit(privateFieldAdjustment) >> builder
        1 * builder.visit(staticMethodAdvice) >> builder
        1 * classTransformer._
        0 * _

        expect:
        res == builder
    }

    def 'private class adjustment'() {
        setup:
        def res = classTransformer.privateClassAdjustment()

        expect:
        verifyAll(res) {
            typeAdjustments*.matcher == [ElementMatchers.isFinal()]
            typeAdjustments*.resolver*.modifierContributors == [[TypeManifestation.PLAIN]]
        }
    }

    def 'protected method adjustment'() {
        setup:
        def res = classTransformer.protectedMethodAdjustment()

        expect:
        verifyAll(res) {
            methodAdjustments*.matcher*.matchers == [[ElementMatchers.isMethod(), ElementMatchers.isPrivate()]]
            methodAdjustments*.resolver*.modifierContributors == [[Visibility.PROTECTED]]
        }
    }

    def 'final method adjustment'() {
        setup:
        def res = classTransformer.finalMethodAdjustment()

        expect:
        verifyAll(res) {
            methodAdjustments*.matcher*.matchers == [[ElementMatchers.isMethod(), ElementMatchers.isFinal()]]
            methodAdjustments*.resolver*.modifierContributors == [[MethodManifestation.PLAIN]]
        }
    }

    def 'private field adjustment'() {
        setup:
        def res = classTransformer.privateFieldAdjustment()

        expect:
        verifyAll(res) {
            fieldAdjustments*.matcher == [ElementMatchers.isPrivate()]
            fieldAdjustments*.resolver*.modifierContributors == [[FieldManifestation.PLAIN]]
        }
    }

    def 'static method advice'() {
        setup:
        def res = classTransformer.staticMethodAdvice()

        expect:
        verifyAll(res) {
            entries*.matcher*.matchers == [[ElementMatchers.isMethod(), ElementMatchers.isStatic()]]
            entries*.methodVisitorWrappers.flatten()[0].methodEnter.adviceMethod.executable.declaringClass == StaticMockHandler
            entries*.methodVisitorWrappers.flatten()[0].methodExit.adviceMethod.executable.declaringClass == StaticMockHandler
        }
    }

    def 'package matches referenced packages'() {
        setup:
        TypeDescription typeDescription = DeepMock()

        when:
        def res = classTransformer.packageMatches(typeDescription)

        then:
        1 * typeDescription.package.name >> 'packA'
        1 * referenceLoader.packages >> { [packageName] as SortedSet }

        expect:
        res == expected

        where:
        packageName || expected
        'packA'     || true
        'packB'     || false
    }

    def 'safe needs transforming returns false on exception'() {
        setup:
        TypeDescription typeDescription = DeepMock()

        when:
        def res = classTransformer.safeNeedsTransforming(typeDescription)

        then:
        1 * classTransformer.needsTransforming(typeDescription) >> needsTransforming
        1 * classTransformer._
        0 * _

        expect:
        res == expected

        where:
        needsTransforming || expected
        false             || false
        true              || true
    }

    def 'safe needs transforming returns false on exception'() {
        setup:
        TypeDescription typeDescription = DeepMock()

        when:
        def res = classTransformer.safeNeedsTransforming(typeDescription)

        then:
        1 * classTransformer.needsTransforming(typeDescription) >> { throw new NoSuchTypeException('myType') }
        1 * classTransformer._
        0 * _

        expect:
        !res
    }

    def 'needs transforming'() {
        setup:
        TypeDescription typeDescription = DeepMock()

        when:
        def res = classTransformer.needsTransforming(typeDescription)

        then:
        classTransformer.isInternal(typeDescription) >> isInternal
        classTransformer.classMatches(typeDescription) >> classMatches
        classTransformer.packageMatches(typeDescription) >> packageMatches

        expect:
        res == expected

        where:
        isInternal | classMatches | packageMatches || expected
        false      | false        | false          || false
        false      | false        | true           || true
        false      | true         | false          || true
        false      | true         | true           || true
        true       | false        | false          || false
        true       | false        | true           || false
        true       | true         | false          || false
        true       | true         | true           || false
    }

    def 'class matches'() {
        setup:
        TypeDescription typeDescription = DeepMock()

        when:
        def res = classTransformer.classMatches(typeDescription)

        then:
        1 * typeDescription.name >> 'classA'
        1 * referenceLoader.classes >> { [className] as SortedSet }
        1 * classTransformer._
        0 * _

        expect:
        res == expected

        where:
        className || expected
        'classA'  || true
        'classB'  || false
    }

    def 'package matches: empty package name'() {
        setup:
        TypeDescription typeDescription = DeepMock()

        when:
        def res = classTransformer.packageMatches(typeDescription)

        then:
        1 * typeDescription.package >> null
        1 * classTransformer._
        0 * _

        expect:
        !res
    }

    def 'is internal'() {
        setup:
        TypeDescription typeDescription = Mock()

        when:
        def res = classTransformer.isInternal(typeDescription)

        then:
        typeDescription.isAssignableTo(Specification) >> assignableSpecification
        typeDescription.isAssignableTo(ByteBuddyInvoker) >> assignableByteBuddyInvoker
        typeDescription.isAssignableTo(ISpockMockObject) >> assignableISpockMockObject

        expect:
        res == expected

        where:
        assignableISpockMockObject | assignableByteBuddyInvoker | assignableSpecification || expected
        false                      | false                      | false                   || false
        false                      | false                      | true                    || true
        false                      | true                       | false                   || true
        true                       | false                      | false                   || true
        true                       | true                       | true                    || true
    }
}
