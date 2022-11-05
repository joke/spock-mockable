package io.github.joke.spockmockable.agent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.agent.builder.AgentBuilder.InitializationStrategy;
import net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy;
import net.bytebuddy.agent.builder.AgentBuilder.RedefinitionStrategy.DiscoveryStrategy;
import net.bytebuddy.agent.builder.AgentBuilder.TypeStrategy;
import net.bytebuddy.asm.ModifierAdjustment;
import net.bytebuddy.description.modifier.MethodManifestation;
import net.bytebuddy.description.modifier.TypeManifestation;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.description.type.PackageDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.scaffold.TypeValidation;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;
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

    public void start() {
        log.info("Activating spock-mockable transformation");
        new AgentBuilder.Default(
                new ByteBuddy()
                        .with(TypeValidation.ENABLED))
                .ignore(none())
                .with(discoveryListener)
                .with(installationListener)
                .with(InitializationStrategy.NoOp.INSTANCE)
                .with(RedefinitionStrategy.RETRANSFORMATION)
                .with(DiscoveryStrategy.Reiterating.INSTANCE)
                .with(redefinitionListener)
                .with(TypeStrategy.Default.REDEFINE)
                .type(this::needsTransformation)
                .transform(this::transform)
                .installOn(instrumentation);
    }

    protected DynamicType.Builder<?> transform(
            final DynamicType.Builder<?> builder,
            final TypeDescription typeDescription,
            final ClassLoader classLoader,
            final JavaModule javaModule,
            final ProtectionDomain protectionDomain
    ) {
        return builder
                .visit(new ModifierAdjustment().withTypeModifiers(ElementMatchers.isFinal(), TypeManifestation.PLAIN))
                .visit(new ModifierAdjustment().withMethodModifiers(ElementMatchers.isPrivate(), Visibility.PROTECTED))
                .visit(new ModifierAdjustment().withMethodModifiers(ElementMatchers.isFinal(), MethodManifestation.PLAIN))
                ;
    }

    protected boolean needsTransformation(final TypeDescription typeDescription) {
        return (classMatches(typeDescription) || packageMatches(typeDescription)) && !isInternal(typeDescription);
    }

    protected boolean classMatches(final TypeDescription typeDescription) {
        return referenceLoader.getClasses().contains(typeDescription.getName());
    }

    protected boolean packageMatches(final TypeDescription typeDescription) {
        final PackageDescription aPackage = typeDescription.getPackage();
        if (aPackage == null) {
            return false;
        }
        return referenceLoader.getPackages().contains(aPackage.getName());
    }

    protected boolean isInternal(TypeDescription typeDescription) {
        return typeDescription.isAssignableTo(ISpockMockObject.class)
                || typeDescription.isAssignableTo(ByteBuddyInvoker.class)
                || typeDescription.isAssignableTo(Specification.class);
    }

}
