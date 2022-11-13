package io.github.joke.spockmockable.ast;

import dagger.BindsInstance;
import dagger.Subcomponent;
import io.github.joke.spockmockable.ast.scopes.SourceUnitScope;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.control.SourceUnit;
import spock.lang.Specification;

import static org.codehaus.groovy.ast.ClassHelper.makeWithoutCaching;

@SourceUnitScope
@Subcomponent(modules = SourceUnitProcessor.Module.class)
abstract class SourceUnitProcessor {

    private static final ClassNode SPECIFICATION = makeWithoutCaching(Specification.class);

    public void process() {
        final SpecificationProcessor.Factory specificationProcessorFactory = specificationProcessorFactory();
        getSourceUnit().getAST().getClasses().stream()
                .filter(clazz -> clazz.isDerivedFrom(SPECIFICATION))
                .forEach(classNode -> specificationProcessorFactory.create(classNode).process());
    }

    protected abstract SourceUnit getSourceUnit();
    protected abstract SpecificationProcessor.Factory specificationProcessorFactory();

    @Subcomponent.Factory
    interface Factory {
        SourceUnitProcessor create(@BindsInstance SourceUnit sourceUnit);
    }

    @dagger.Module
    interface Module {
    }
}
