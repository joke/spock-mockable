package io.github.joke.spockmockable.hooks;

import com.google.auto.service.AutoService;
import io.github.joke.spockmockable.agent.MockableController;
import org.junit.platform.engine.EngineDiscoveryRequest;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestEngine;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.support.descriptor.EngineDescriptor;

@SuppressWarnings("unused")
@AutoService(TestEngine.class)
public class Engine implements TestEngine {

    static {
        MockableController.init();
    }

    @Override
    public String getId() {
        return "spock-mockable";
    }

    @Override
    public TestDescriptor discover(final EngineDiscoveryRequest discoveryRequest, final UniqueId uniqueId) {
        return new EngineDescriptor(uniqueId, getId());
    }

    @Override
    public void execute(final ExecutionRequest request) {
    }
}
