package io.github.joke.spockmockable.agent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.agent.builder.AgentBuilder.InitializationStrategy;
import net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy;
import net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.DiscoveryStrategy;
import net.bytebuddy.agent.builder.AgentBuilder.TypeStrategy;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.asm.AsmVisitorWrapper;
import net.bytebuddy.asm.ModifierAdjustment;
import net.bytebuddy.description.modifier.FieldManifestation;
import net.bytebuddy.description.modifier.MethodManifestation;
import net.bytebuddy.description.modifier.TypeManifestation;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.description.type.PackageDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.scaffold.TypeValidation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;
import org.jetbrains.annotations.NotNull;
import org.spockframework.mock.ISpockMockObject;
import org.spockframework.mock.runtime.ByteBuddyInvoker;
import spock.lang.Specification;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

import static net.bytebuddy.matcher.ElementMatchers.none;

@Slf4j
@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class ClassTransformer {

    private final ReferenceLoader referenceLoader;
    private final Instrumentation instrumentation;
    private final DiscoveryListener discoveryListener;
    private final InstallationListener installationListener;
    private final RedefinitionListener redefinitionListener;

    public void applyTransformation() {
        log.info("Activating spock-mockable transformation");
        new AgentBuilder.Default(
                new ByteBuddy()
                        .with(TypeValidation.DISABLED))
                .ignore(none())
                .with(discoveryListener)
                .with(installationListener)
                .with(InitializationStrategy.Minimal.INSTANCE)
                .with(RedefinitionStrategy.RETRANSFORMATION)
                .with(DiscoveryStrategy.Reiterating.INSTANCE)
                .with(redefinitionListener)
                .with(TypeStrategy.Default.REDEFINE)
                .type(this::needsTransformation)
                .transform(this::transform)
                .installOn(instrumentation);
    }

    @NotNull
    protected DynamicType.Builder<?> transform(
            final DynamicType.Builder<?> builder,
            final TypeDescription typeDescription,
            final ClassLoader classLoader,
            final JavaModule javaModule,
            final ProtectionDomain protectionDomain
    ) {
        return builder
                .visit(privateClassAdjustment())
                .visit(protectedMethodAdjustment())
                .visit(finalMethodAdjustment())
                .visit(privateFieldAdjustment())
                .visit(staticMethodAdvice());
    }

    @NotNull
    protected AsmVisitorWrapper.ForDeclaredMethods staticMethodAdvice() {
        return new AsmVisitorWrapper.ForDeclaredMethods().method(ElementMatchers.isStatic(), Advice.withCustomMapping()
                .to(StaticMockHandler.class));
    }

    @NotNull
    protected ModifierAdjustment privateFieldAdjustment() {
        return new ModifierAdjustment().withFieldModifiers(ElementMatchers.isPrivate(), FieldManifestation.PLAIN);
    }

    @NotNull
    protected ModifierAdjustment finalMethodAdjustment() {
        return new ModifierAdjustment().withMethodModifiers(ElementMatchers.isFinal(), MethodManifestation.PLAIN);
    }

    @NotNull
    protected ModifierAdjustment protectedMethodAdjustment() {
        return new ModifierAdjustment().withMethodModifiers(ElementMatchers.isPrivate(), Visibility.PROTECTED);
    }

    @NotNull
    protected ModifierAdjustment privateClassAdjustment() {
        return new ModifierAdjustment().withTypeModifiers(ElementMatchers.isFinal(), TypeManifestation.PLAIN);
    }

    protected boolean needsTransformation(final TypeDescription typeDescription) {
        return !isInternal(typeDescription) && (classMatches(typeDescription) || packageMatches(typeDescription));
    }

    protected boolean classMatches(final TypeDescription typeDescription) {
        return referenceLoader.getClasses().contains(typeDescription.getName());
    }

    protected boolean packageMatches(final TypeDescription typeDescription) {
        final PackageDescription packageDescription = typeDescription.getPackage();
        return packageDescription != null && referenceLoader.getPackages().contains(packageDescription.getName());
    }

    protected boolean isInternal(TypeDescription typeDescription) {
        return typeDescription.isAssignableTo(ISpockMockObject.class)
                || typeDescription.isAssignableTo(ByteBuddyInvoker.class)
                || typeDescription.isAssignableTo(Specification.class);
    }
}
