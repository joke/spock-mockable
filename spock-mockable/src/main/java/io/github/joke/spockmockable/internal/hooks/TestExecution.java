package io.github.joke.spockmockable.internal.hooks;

import com.google.auto.service.AutoService;
import io.github.joke.spockmockable.internal.MockableTransformer;
import org.junit.platform.launcher.TestExecutionListener;

import static io.github.joke.spockmockable.internal.MockableTransformer.getInstance;

@AutoService(TestExecutionListener.class)
public class TestExecution implements TestExecutionListener {

    private static final MockableTransformer mockableTransformer = getInstance();

}
