package io.github.joke.spockmockable.ast;

import com.google.auto.service.AutoService;
import groovy.transform.CompilationUnitAware;
import lombok.RequiredArgsConstructor;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.transform.ASTTransformation;
import org.codehaus.groovy.transform.AbstractASTTransformation;
import org.codehaus.groovy.transform.GroovyASTTransformation;
import org.jetbrains.annotations.Nullable;

import static org.codehaus.groovy.control.CompilePhase.CANONICALIZATION;
import static org.codehaus.groovy.control.CompilePhase.OUTPUT;
import static org.spockframework.runtime.GroovyRuntimeUtil.getProperty;

@AutoService(ASTTransformation.class)
@GroovyASTTransformation(phase = CANONICALIZATION)
public class MockableASTTransformation extends AbstractASTTransformation implements CompilationUnitAware {

    @Nullable
    private static CompilationUnit compilationUnit = null;

    @Nullable
    Processor processor;

    @Override
    public void visit(final ASTNode[] nodes, final SourceUnit sourceUnit) {
        if (processor == null) {
            throw new UnsupportedOperationException();
        }

        processor.analyze(sourceUnit);
    }

    @Override
    public void setCompilationUnit(final CompilationUnit unit) {
        compilationUnit = unit;
        processor = Processor.create(unit);
        unit.addNewPhaseOperation(new PropertyWriterOperation(processor), OUTPUT.getPhaseNumber());
    }

    @RequiredArgsConstructor
    private static class PropertyWriterOperation extends CompilationUnit.SourceUnitOperation {

        private final Processor processor;

        @Override
        public void call(final SourceUnit source) {
            if (compilationUnit == null) {
                throw new UnsupportedOperationException();
            }

            final boolean phaseComplete = (boolean) getProperty(compilationUnit, "phaseComplete");
            if (!phaseComplete) {
                processor.generateOutput();
                compilationUnit.completePhase();
            }
        }
    }

}
