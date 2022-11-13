package io.github.joke.spockmockable.ast;

import dagger.BindsInstance;
import dagger.Component;
import org.codehaus.groovy.control.CompilationUnit;
import org.codehaus.groovy.control.SourceUnit;

import javax.inject.Singleton;

@Singleton
@Component(modules = Processor.Module.class)
abstract class Processor {

    static Processor create(final CompilationUnit unit) {
        return DaggerProcessor.factory().create(unit);
    }

    protected abstract MetaInfWriter metaIntWriter();

    protected void analyze(final SourceUnit sourceUnit) {
        specificationSelectorFactory().create(sourceUnit).process();
    }

    protected void generateOutput() {
        metaIntWriter().write();
    }

    protected abstract SourceUnitProcessor.Factory specificationSelectorFactory();

    @Component.Factory
    interface Factory {
        Processor create(@BindsInstance CompilationUnit compilationUnit);
    }

    @dagger.Module
    interface Module {
    }
}
