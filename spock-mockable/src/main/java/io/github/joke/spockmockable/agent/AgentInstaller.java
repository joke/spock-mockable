package io.github.joke.spockmockable.agent;

import dagger.Component;
import dagger.Provides;
import lombok.Getter;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.agent.ByteBuddyAgent;
import org.jetbrains.annotations.Nullable;

import javax.inject.Singleton;
import java.lang.instrument.Instrumentation;
import java.util.Optional;

import static java.lang.Boolean.parseBoolean;
import static java.lang.System.getProperty;
import static lombok.AccessLevel.PRIVATE;

@Slf4j
@Singleton
@Component(modules = AgentInstaller.Module.class)
public abstract class AgentInstaller {

    @Nullable
    private static Instrumentation instrumentation;

    @SuppressWarnings("SameNameButDifferent")
    @Getter(value = PRIVATE, lazy = true, onMethod_ = @Nullable)
    private static final AgentInstaller agentInstaller = DaggerAgentInstaller.create().init();

    @SuppressWarnings("DoNotCallSuggester")
    public static void premain(String agentArgs, Instrumentation instrumentation) {
        AgentInstaller.instrumentation = instrumentation;
        install();
    }

    @Synchronized
    public static void install() {
        getAgentInstaller();
    }

    protected abstract ClassTransformer classTransformer();

    protected abstract ReferenceLoader referenceLoader();

    protected AgentInstaller init() {
        if (parseBoolean(getProperty("spock-mockable.disabled", "false"))) {
            log.info("@Mockable transformation is disabled by system property 'spock-mockable.disabled=true'");
        }

        if (referenceLoader().hasClasses()) {
            classTransformer().start();
        }
        return this;
    }

    @Component.Factory
    interface Factory {

        AgentInstaller create();

    }

    @dagger.Module
    interface Module {

        @Provides
        @Singleton
        static Instrumentation getInstrumentation() {
            return Optional.ofNullable(AgentInstaller.instrumentation)
                    .orElseGet(ByteBuddyAgent::install);
        }

    }

}
