package io.github.joke.spockmockable.ast;

import dagger.BindsInstance;
import dagger.Subcomponent;
import io.github.joke.spockmockable.ast.scopes.ClassNodeScope;
import io.github.joke.spockmockable.ast.visitors.SpecificationVisitor;
import org.codehaus.groovy.ast.ClassNode;

@ClassNodeScope
@Subcomponent(modules = SpecificationProcessor.Module.class)
abstract class SpecificationProcessor {

    public void process() {
        specificationVisitor().visit();
    }

    protected abstract SpecificationVisitor specificationVisitor();

    @Subcomponent.Factory
    interface Factory {
        SpecificationProcessor create(@BindsInstance ClassNode classNode);
    }

    @dagger.Module
    interface Module {
    }
}
