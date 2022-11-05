package io.github.joke.spockmockable.ast;

import lombok.RequiredArgsConstructor;
import org.codehaus.groovy.ast.AnnotatedNode;
import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.control.SourceUnit;

import javax.inject.Inject;

@RequiredArgsConstructor(onConstructor_ = @Inject)
class SpecificationVisitor {

    private final SourceUnit sourceUnit;
    private final MockVisitor mockVisitor;

    private final AnnotationVisitor annotationVisitor;

    public void visit(final ClassNode classNode) {
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
            super.visitMethodCallExpression(call);
        }

        @Override
        protected SourceUnit getSourceUnit() {
            return sourceUnit;
        }
    }

}
