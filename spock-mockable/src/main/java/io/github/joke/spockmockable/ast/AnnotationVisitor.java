package io.github.joke.spockmockable.ast;

import io.github.joke.spockmockable.Mockable;
import lombok.RequiredArgsConstructor;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.AnnotatedNode;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassCodeVisitorSupport;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.control.SourceUnit;

import javax.inject.Inject;
import java.util.Map;

import static java.util.Optional.ofNullable;
import static org.codehaus.groovy.ast.ClassHelper.makeWithoutCaching;

@RequiredArgsConstructor(onConstructor_ = @Inject)
class AnnotationVisitor {

    private static final ClassNode MOCKABLE = makeWithoutCaching(Mockable.class);

    private final SourceUnit sourceUnit;

    private final ClassCollector classCollector;

    public void visit(final AnnotatedNode node) {
        new Visitor().visitAnnotations(node);
    }

    @RequiredArgsConstructor
    private class Visitor extends ClassCodeVisitorSupport {

        @Override
        protected void visitAnnotation(final AnnotationNode node) {
            if (MOCKABLE.equals(node.getClassNode())) {
                final Map<String, Expression> members = node.getMembers();

                ofNullable(members.get("className"))
                        .map(ASTNode::getText)
                        .ifPresent(classCollector::addClass);
                ofNullable(members.get("packageName"))
                        .map(ASTNode::getText)
                        .ifPresent(classCollector::addPackage);
            }
        }

        @Override
        protected SourceUnit getSourceUnit() {
            return sourceUnit;
        }
    }

}
