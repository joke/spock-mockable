package io.github.joke.spockmockable.ast;

import dagger.BindsInstance;
import dagger.Subcomponent;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.control.SourceUnit;
import spock.lang.Specification;

import javax.inject.Inject;

import static org.codehaus.groovy.ast.ClassHelper.makeWithoutCaching;

@Subcomponent(modules = SpecificationSelector.Module.class)
abstract class SpecificationSelector {

    private static final ClassNode SPECIFICATION = makeWithoutCaching(Specification.class);

    @Inject
    SourceUnit sourceUnit;

    protected abstract SpecificationVisitor getSpecificationVisitor();

    protected abstract SourceUnit getSourceUnit();

    public void process() {
        final SpecificationVisitor specificationVisitor = getSpecificationVisitor();
        getSourceUnit().getAST().getClasses().stream()
                .filter(clazz -> clazz.isDerivedFrom(SPECIFICATION))
                .forEach(specificationVisitor::visit);
    }

    @Subcomponent.Factory
    interface Factory {
        SpecificationSelector create(@BindsInstance SourceUnit sourceUnit);
    }

    @dagger.Module
    interface Module {
    }

}
