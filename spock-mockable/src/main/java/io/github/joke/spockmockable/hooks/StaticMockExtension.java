package io.github.joke.spockmockable.hooks;

import com.google.auto.service.AutoService;
import io.github.joke.spockmockable.agent.StaticMockHandler;
import lombok.extern.slf4j.Slf4j;
import org.spockframework.runtime.extension.AbstractMethodInterceptor;
import org.spockframework.runtime.extension.IGlobalExtension;
import org.spockframework.runtime.extension.IMethodInvocation;
import org.spockframework.runtime.model.SpecInfo;
import spock.lang.Specification;

@Slf4j
@SuppressWarnings("unused")
@AutoService(IGlobalExtension.class)
public class StaticMockExtension implements IGlobalExtension {

    @Override
    public void visitSpec(final SpecInfo spec) {
        final Extractor extractor = new Extractor();
        spec.addSetupInterceptor(extractor);
        spec.addSetupSpecInterceptor(extractor);
    }

    private static class Extractor extends AbstractMethodInterceptor {

        @Override
        public void interceptSetupMethod(final IMethodInvocation invocation) throws Throwable {
            StaticMockHandler.setFeatureMockController(((Specification) invocation.getInstance()).getSpecificationContext().getMockController());
            invocation.proceed();
        }

        @Override
        public void interceptSetupSpecMethod(final IMethodInvocation invocation) throws Throwable {
            StaticMockHandler.setSpecification(((Specification) invocation.getSharedInstance()));
            invocation.proceed();
        }
    }
}
