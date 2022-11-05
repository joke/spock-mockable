package io.github.joke.spockmockable.hooks;

import com.google.auto.service.AutoService;
import io.github.joke.spockmockable.agent.AgentInstaller;
import org.spockframework.runtime.extension.AbstractGlobalExtension;
import org.spockframework.runtime.extension.IGlobalExtension;

@AutoService(IGlobalExtension.class)
public class GlobalExtension extends AbstractGlobalExtension {

    static {
        AgentInstaller.install();
    }

}
