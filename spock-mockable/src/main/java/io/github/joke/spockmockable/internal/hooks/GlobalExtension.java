package io.github.joke.spockmockable.internal.hooks;

import com.google.auto.service.AutoService;
import io.github.joke.spockmockable.internal.MockableTransformer;
import org.spockframework.runtime.extension.AbstractGlobalExtension;
import org.spockframework.runtime.extension.IGlobalExtension;

import static io.github.joke.spockmockable.internal.MockableTransformer.getInstance;

@AutoService(IGlobalExtension.class)
public class GlobalExtension extends AbstractGlobalExtension {

    private static final MockableTransformer mockableTransformer = getInstance();

}
