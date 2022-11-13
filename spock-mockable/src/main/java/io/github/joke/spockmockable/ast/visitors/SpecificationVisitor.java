package io.github.joke.spockmockable.ast.visitors;

import io.github.joke.spockmockable.ast.scopes.ClassNodeScope;
import lombok.RequiredArgsConstructor;
import org.codehaus.groovy.ast.AnnotatedNode;
import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.control.SourceUnit;

import javax.inject.Inject;

@ClassNodeScope
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class SpecificationVisitor {

    private final ClassNode classNode;
    private final SourceUnit sourceUnit;
    private final MockVisitor mockVisitor;
    private final AnnotationVisitor annotationVisitor;
    private final InteractionVisitor interactionVisitor;

    public void visit() {
        new Visitor().visitClass(classNode);
    }

    @RequiredArgsConstructor
    private class Visitor extends ClassCodeVisitorSupport {

        @Override
        public void visitAnnotations(final AnnotatedNode node) {
            annotationVisitor.visit(node);
        }

        @Override
        public void visitMethodCallExpression(final MethodCallExpression call) {
            mockVisitor.visit(call);
            interactionVisitor.visit(call);
            super.visitMethodCallExpression(call);
        }

        @Override
        protected SourceUnit getSourceUnit() {
            return sourceUnit;
        }
    }
}
