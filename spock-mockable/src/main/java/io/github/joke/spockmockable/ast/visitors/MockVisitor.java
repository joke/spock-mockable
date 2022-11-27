package io.github.joke.spockmockable.ast.visitors;

import io.github.joke.spockmockable.ast.ClassCollector;
import io.github.joke.spockmockable.ast.scopes.ClassNodeScope;
import lombok.RequiredArgsConstructor;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.CodeVisitorSupport;
import org.codehaus.groovy.ast.Variable;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.MethodCallExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.control.SourceUnit;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;
import java.util.Collections;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;
import static org.spockframework.util.Identifiers.BUILT_IN_METHODS;

@ClassNodeScope
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class MockVisitor {

    private final static SortedSet<String> METHOD_IDENTIFIER = BUILT_IN_METHODS.stream()
            .map(builtIn -> builtIn + "Impl")
            .collect(collectingAndThen(toCollection(TreeSet::new), Collections::unmodifiableSortedSet));
    private final SourceUnit sourceUnit;
    private final ClassCollector classCollector;

    public void visit(final MethodCallExpression origin) {
        new Visitor(origin).visitMethodCallExpression(origin);
    }

    @RequiredArgsConstructor
    private class Visitor extends CodeVisitorSupport {

        private final MethodCallExpression origin;

        boolean classDetected = false;

        @Override
        public void visitMethodCallExpression(final MethodCallExpression call) {
            final String methodName = call.getMethodAsString();
            if (methodName != null && METHOD_IDENTIFIER.contains(methodName)) {
                call.getArguments().visit(this);
            }
        }

        @Override
        public void visitClassExpression(final ClassExpression expression) {
            final Optional<Class<?>> clazz = Optional.of(expression)
                    .map(Expression::getType)
                    .flatMap(this::extractClass);
            checkAndLogError(clazz.orElse(null));
        }

        @Override
        public void visitVariableExpression(final VariableExpression expression) {
            final Optional<? extends Class<?>> clazz = Optional.of(expression)
                    .map(VariableExpression::getAccessedVariable)
                    .map(Variable::getType)
                    .flatMap(this::extractClass);
            checkAndLogError(clazz.orElse(null));
        }

        protected void logError() {
            sourceUnit.getErrorCollector().addErrorAndContinue(
                    "Unable to determine expression type. This primarily happens when passing a dynamic typed variable to Spy().\n" +
                            "Please rewrite your test either way:\n" +
                            "--------------------------------\n" +
                            "def person = createInstance()\n" +
                            "Person mySpy = Spy(person)\n" +
                            "-------------- or --------------\n" +
                            "Person person = createInstance()\n" +
                            "def mySpy = Spy(person)\n" +
                            "--------------------------------\n"
                    , origin, sourceUnit);
        }

        protected Optional<Class<?>> extractClass(final ClassNode classNode) {
            return Optional.of(classNode)
                    .map(ClassNode::getTypeClass)
                    .filter(c -> !"java.lang.Object".equals(c.getCanonicalName()))
                    .map(c -> (Class<?>) c);
        }

        protected void checkAndLogError(@Nullable final Class<?> clazz) {
            if (clazz != null) {
                classDetected = true;
                traverseClassHierarchy(clazz);
            } else if (!classDetected) {
                logError();
            }
        }

        protected void traverseClassHierarchy(final Class<?> clazz) {
            Class<?> current = clazz;
            while (isTransformable(current)) {
                classCollector.addClass(current);
                current = current.getSuperclass();
            }
        }

        protected boolean isTransformable(final Class<?> clazz) {
            return clazz != Object.class && !clazz.isInterface();
        }
    }
}
