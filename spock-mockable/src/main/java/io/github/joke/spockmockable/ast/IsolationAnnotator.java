package io.github.joke.spockmockable.ast;

import io.github.joke.spockmockable.ast.scopes.ClassNodeScope;
import lombok.RequiredArgsConstructor;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import spock.lang.Isolated;

import javax.inject.Inject;

@ClassNodeScope
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class IsolationAnnotator {

    private static final ClassNode ISOLATED = ClassHelper.make(Isolated.class);

    private final ClassNode classNode;

    public void addIsolationAnnotation() {
        if (classNode.getAnnotations(ISOLATED).isEmpty()) {
            final AnnotationNode annotation = new AnnotationNode(ISOLATED);
            annotation.setMember("value", new ConstantExpression("Static method mock"));
            classNode.addAnnotation(annotation);
        }
    }
}
