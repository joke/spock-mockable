package io.github.joke.spockmockable.internal;

import groovy.lang.GroovySystem;
import groovy.transform.CompilationUnitAware;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.transform.AbstractASTTransformation;
import org.codehaus.groovy.transform.GroovyASTTransformation;
import org.spockframework.runtime.GroovyRuntimeUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Callable;

import static java.util.stream.Collectors.toList;
import static java.lang.String.join;
import static java.util.Collections.emptyList;
import static org.codehaus.groovy.control.CompilePhase.CANONICALIZATION;
import static org.codehaus.groovy.control.CompilePhase.OUTPUT;
import static org.codehaus.groovy.reflection.ReflectionUtils.getMethods;
import static org.spockframework.runtime.GroovyRuntimeUtil.getProperty;
import static org.spockframework.runtime.GroovyRuntimeUtil.invokeMethod;

@GroovyASTTransformation(phase = CANONICALIZATION)
public class MockableASTTransformation extends AbstractASTTransformation implements CompilationUnitAware {

    private static final Set<String> detectedClasses = new HashSet<>();
    private static final Set<String> detectedPackages = new HashSet<>();
    private static CompilationUnit compilationUnit = null;

    @Override
    public void visit(final ASTNode[] nodes, final SourceUnit source) {
        Arrays.stream(nodes)
                .filter(AnnotationNode.class::isInstance)
                .map(AnnotationNode.class::cast)
                .forEach(this::processAnnotation);
    }

    private void processAnnotation(final AnnotationNode annotation) {
        detectedClasses.addAll(getClassNames(annotation));
        detectedPackages.addAll(getPackageNames(annotation));
        annotation.getMembers().clear();
    }

    @SuppressWarnings("unchecked")
    private List<String> getClassNames(AnnotationNode annotation) {
        final String methodName = GroovySystem.getVersion().startsWith("2.") ? "getClassList" : "getMemberClassList";
        final List<ClassNode> classNodes = (List<ClassNode>) invokeMethod(this, methodName, annotation, "value");
        return emptyIfNull(classNodes).stream()
                .map(ClassNode::getName)
                .collect(toList());
    }

    @SuppressWarnings("unchecked")
    private List<String> getPackageNames(AnnotationNode annotation) {
        final String methodName = GroovySystem.getVersion().startsWith("2.") ? "getMemberList" : "getMemberStringList";
        final List<String> packages = (List<String>) invokeMethod(this, methodName, annotation, "packages");
        return emptyIfNull(packages);
    }

    @Override
    public void setCompilationUnit(final CompilationUnit unit) {
        if (compilationUnit != null)
            return;

        compilationUnit = unit;
        unit.addNewPhaseOperation(new PropertyWriterOperation(), OUTPUT.getPhaseNumber());
    }

    private static void writeProperties() {
        final File metaInf = new File(compilationUnit.getConfiguration().getTargetDirectory(), "META-INF");
        metaInf.mkdirs();

        final File propertyFile = new File(metaInf, "spock-mockable.properties");

        final Properties properties = new Properties();
        properties.setProperty("classes", join(",", detectedClasses));
        properties.setProperty("packages", join(",", detectedPackages));

        try (final FileOutputStream output = new FileOutputStream(propertyFile)) {
            properties.store(output, "io.github.joke:spock-mockable");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> List<T> emptyIfNull(final List<T> list) {
        return list == null ? emptyList() : list;
    }

    private static class PropertyWriterOperation extends CompilationUnit.SourceUnitOperation {
        public void call(SourceUnit source) {
            final boolean phaseComplete = (boolean) getProperty(compilationUnit, "phaseComplete");
            if (!phaseComplete) {
                writeProperties();
                compilationUnit.completePhase();
            }
        }
    }
}
