package io.github.joke.spockmockable.agent;

import lombok.Synchronized;
import net.bytebuddy.agent.ByteBuddyAgent;
import org.jetbrains.annotations.Nullable;

import java.lang.instrument.Instrumentation;

public class InstrumentationInstaller {

    @Nullable
    public static Instrumentation instrumentation;

    public static void premain(final String agentArgs, final Instrumentation instrumentation) {
        InstrumentationInstaller.instrumentation = instrumentation;
        MockableController.init();
    }

    @Synchronized
    public static Instrumentation getInstrumentation() {
        if (instrumentation != null) {
            return instrumentation;
        }
        return instrumentation = ByteBuddyAgent.install();
    }
}
