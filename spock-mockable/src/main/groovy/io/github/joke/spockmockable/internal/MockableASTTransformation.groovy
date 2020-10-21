package io.github.joke.spockmockable.internal

import groovy.transform.CompileStatic
import io.github.joke.spockmockable.Mockable
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.ast.expr.Expression
import org.codehaus.groovy.ast.expr.ListExpression
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.AbstractASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation

import static org.codehaus.groovy.control.CompilePhase.CANONICALIZATION

@CompileStatic
@GroovyASTTransformation(phase = CANONICALIZATION)
class MockableASTTransformation extends AbstractASTTransformation {

    @Override
    void visit(ASTNode[] nodes, SourceUnit source) {
        source.AST.classes.each {
            it.annotations
                    .findAll { it.classNode.redirect().name == Mockable.canonicalName }
                    .each {
                        def value = it.members.value

                        Expression newExpression = buildExpression(value)

                        it.members.clear()
                        it.addMember('canonicalClassNames', newExpression)
                    }
        }
    }

    private Expression buildExpression(Expression value) {
        Expression newExpression
        if (value instanceof ListExpression) {
            def canonicalClassNames = (value as ListExpression).expressions*.type.name.collect { new ConstantExpression(it) } ?: []
            newExpression = new ListExpression(canonicalClassNames as List<Expression>)
        } else {
            newExpression = new ConstantExpression(value.type.name)
        }
        newExpression
    }

}
