package io.github.joke.spockmockable.agent;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.Synchronized;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice.AllArguments;
import net.bytebuddy.asm.Advice.Enter;
import net.bytebuddy.asm.Advice.OnMethodEnter;
import net.bytebuddy.asm.Advice.OnMethodExit;
import net.bytebuddy.asm.Advice.OnNonDefaultValue;
import net.bytebuddy.asm.Advice.Origin;
import net.bytebuddy.asm.Advice.Return;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spockframework.mock.CallRealMethodResponse;
import org.spockframework.mock.CannotInvokeRealMethodException;
import org.spockframework.mock.IMockController;
import org.spockframework.mock.IMockObject;
import org.spockframework.mock.runtime.FailingRealMethodInvoker;
import org.spockframework.mock.runtime.MockInvocation;
import org.spockframework.mock.runtime.MockObject;
import org.spockframework.mock.runtime.StaticMockMethod;
import org.spockframework.runtime.SpockException;
import spock.lang.Specification;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;
import static lombok.AccessLevel.PRIVATE;
import static net.bytebuddy.implementation.bytecode.assign.Assigner.Typing.DYNAMIC;

@Slf4j
public class StaticMockHandler {

    @Getter
    @Nullable
    @Setter(onMethod_ = @Synchronized)
    private static Specification specification;

    @Getter
    @Nullable
    @Setter(onMethod_ = @Synchronized)
    private static IMockController featureMockController;

    @Nullable
    @SuppressWarnings("unused")
    @OnMethodEnter(skipOn = OnNonDefaultValue.class)
    public static Callable<?> onMethodEnter(@Origin final Class<?> type, @Origin final Method origin, @AllArguments final Object... arguments) throws Throwable {
        return determineMockValue(type, origin, arguments);
    }

    @OnMethodExit
    @SuppressWarnings({"ReassignedVariable", "unused"})
    public static void onMethodExit(@Return(readOnly = false, typing = DYNAMIC) Object returned, @Enter final Callable<?> interactionResult) throws Throwable {
        returned = determineReturnValue(returned, interactionResult);
    }

    @Nullable
    public static ReturnWrapper determineMockValue(final Class<?> type, final Method origin, final Object... arguments) {
        final MockInvocation mockInvocation = buildMockInvocation(type, origin, arguments);
        try {
            return getInteractionValue(mockInvocation);
        } catch (final CannotInvokeRealMethodException e) {
            return null;
        }
    }


    public static ReturnWrapper getInteractionValue(final MockInvocation mockInvocation) {
        return new ReturnWrapper(ofNullable(featureMockController)
                .orElseThrow(() -> new SpockException("Can not invoke mock from current context"))
                .handle(mockInvocation));
    }

    @NotNull
    public static MockInvocation buildMockInvocation(final Class<?> type, final Method origin, final Object[] arguments) {
        final String name = format("global spy for %s", type.getSimpleName());
        final IMockObject mockObject = new MockObject(name, type, type, true, true, CallRealMethodResponse.INSTANCE, specification, null);
        final StaticMockMethod staticMockMethod = new StaticMockMethod(origin, type);
        return new MockInvocation(mockObject, staticMockMethod, asList(arguments), new FailingRealMethodInvoker(""));
    }

    public static Object determineReturnValue(final Object returned, final Callable<?> mocked) throws Exception {
        return mocked != null ? mocked.call() : returned;
    }

    @Value
    @RequiredArgsConstructor(access = PRIVATE)
    static class ReturnWrapper implements Callable<Object> {

        Object value;

        @Override
        public Object call() {
            return value;
        }
    }
}
