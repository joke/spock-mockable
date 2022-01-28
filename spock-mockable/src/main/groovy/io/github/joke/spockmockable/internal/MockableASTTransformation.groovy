package io.github.joke.spockmockable.internal

import groovy.transform.CompilationUnitAware
import groovy.transform.CompileStatic
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.control.CompilationUnit
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.AbstractASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation

import static java.nio.charset.StandardCharsets.UTF_8
import static org.codehaus.groovy.control.CompilationUnit.SourceUnitOperation
import static org.codehaus.groovy.control.CompilePhase.CANONICALIZATION
import static org.codehaus.groovy.control.CompilePhase.OUTPUT

@GroovyASTTransformation(phase = CANONICALIZATION)
class MockableASTTransformation extends AbstractASTTransformation implements CompilationUnitAware {

    private static Set<String> detectedClasses = [] as Set<String>
    private static Set<String> detectedPackages = [] as Set<String>
    private static CompilationUnit compilationUnit

    @Override
    void visit(ASTNode[] nodes, SourceUnit source) {
        def annotation = nodes[0] as AnnotationNode
        detectedClasses.addAll(getClassList(annotation, 'value')*.name as List)
        detectedPackages.addAll(getMemberStringList(annotation, 'packages')?:[])
        annotation.members.clear()
    }

    @Override
    void setCompilationUnit(CompilationUnit unit) {
        if (!compilationUnit) {
            compilationUnit = unit
            unit.addNewPhaseOperation({SourceUnit sourceUnit ->
                if (!compilationUnit.phaseComplete) {
                    writeProperties()
                    compilationUnit.completePhase()
                }
            } as SourceUnitOperation, OUTPUT.phaseNumber)
        }
    }

    private static writeProperties() {
        def metaInf = new File(compilationUnit.configuration.targetDirectory, 'META-INF')
        metaInf.mkdirs()
        def propertyFile = new File(metaInf, 'spock-mockable.properties')
        propertyFile.withWriter("${UTF_8}", { writer ->
            new Properties().with {
                it['classes'] = MockableASTTransformation.detectedClasses.join(',')
                it['packages'] = MockableASTTransformation.detectedPackages.join(',')
                store(writer, 'Automatically generated by spock-mockable')
            }
        })
    }
}
