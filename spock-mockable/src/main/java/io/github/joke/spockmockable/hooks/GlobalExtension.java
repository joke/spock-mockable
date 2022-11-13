package io.github.joke.spockmockable.hooks;

import com.google.auto.service.AutoService;
import io.github.joke.spockmockable.agent.MockableController;
import org.spockframework.runtime.extension.IGlobalExtension;

@SuppressWarnings("unused")
@AutoService(IGlobalExtension.class)
public class GlobalExtension implements IGlobalExtension {

    static {
        MockableController.init();
    }

}
