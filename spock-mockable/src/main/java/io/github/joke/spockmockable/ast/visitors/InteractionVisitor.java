package io.github.joke.spockmockable.ast.visitors;

import io.github.joke.spockmockable.ast.IsolationAnnotator;
import io.github.joke.spockmockable.ast.scopes.ClassNodeScope;
import lombok.RequiredArgsConstructor;
import org.codehaus.groovy.ast.CodeVisitorSupport;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;

import javax.inject.Inject;

import static org.spockframework.mock.runtime.InteractionBuilder.ADD_EQUAL_TARGET;
import static org.spockframework.mock.runtime.MockController.ADD_INTERACTION;

@ClassNodeScope
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class InteractionVisitor {

    private final IsolationAnnotator isolationAnnotator;

    public void visit(final MethodCallExpression origin) {
        final String methodName = origin.getMethodAsString();
        if (ADD_INTERACTION.equals(methodName)) {
            origin.getArguments().visit(new Visitor());
        }
    }

    @RequiredArgsConstructor
    private class Visitor extends CodeVisitorSupport {

        @Override
        public void visitMethodCallExpression(final MethodCallExpression call) {
            final String methodName = call.getMethodAsString();
            if (ADD_EQUAL_TARGET.equals(methodName)) {
                call.getArguments().visit(new TargetTypeVisitor());
            }
            call.getObjectExpression().visit(this);
        }
    }

    private class TargetTypeVisitor extends CodeVisitorSupport {

        @Override
        public void visitClassExpression(final ClassExpression expression) {
            isolationAnnotator.addIsolationAnnotation();
        }
    }
}
