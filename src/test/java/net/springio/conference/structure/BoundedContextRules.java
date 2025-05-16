package net.springio.conference.structure;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.Dependency;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaPackage;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import jakarta.persistence.Embeddable;
import jakarta.persistence.MappedSuperclass;
import net.springio.conference.shared.application.Policy;
import net.springio.conference.shared.application.UseCase;
import org.jmolecules.architecture.layered.ApplicationLayer;
import org.jmolecules.architecture.layered.DomainLayer;
import org.jmolecules.architecture.layered.InfrastructureLayer;
import org.jmolecules.architecture.layered.InterfaceLayer;
import org.jmolecules.ddd.annotation.*;
import org.jmolecules.ddd.annotation.Module;
import org.jmolecules.event.annotation.DomainEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.assignableTo;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.type;
import static com.tngtech.archunit.core.domain.properties.CanBeAnnotated.Predicates.annotatedWith;

public final class BoundedContextRules {

    private static final Function<JavaClasses, DescribedPredicate<JavaClass>> isInBoundedContext = classes -> RecursivePackageRules
            .resideInPackageOrAncestorAnnotatedWith(BoundedContext.class, classes);

    private BoundedContextRules() {
    }

    /**
     * Matches any class in a package annotated @BoundedContext(...)
     */
    public static DescribedPredicate<JavaClass> resideInAnyBoundedContext(JavaClasses classes) {
        return new DescribedPredicate<>("reside in any @BoundedContext") {
            @Override
            public boolean test(JavaClass clazz) {
                return isInBoundedContext.apply(classes).test(clazz);
            }
        };
    }

    /**
     * The core condition: no dependency to a different context unless allowed
     */
    public static ArchCondition<JavaClass> notDependOnOtherContextUnlessAllowed(JavaClasses classes) {
        return new ArchCondition<>("not depend on classes in another @BoundedContext (except interface, domain events, application)") {

            @Override
            public void check(JavaClass origin, ConditionEvents events) {
                BoundedContext originAnn = RecursivePackageRules
                        .findPackageOrAncestorAnnotatedWith(BoundedContext.class, classes, origin)
                        .map(pkg -> pkg.getAnnotationOfType(BoundedContext.class))
                        .orElseThrow(() -> new IllegalArgumentException(String
                                .format("Class <%s> is not in a package annotated with @BoundedContext", origin.getName())));

                String originId = originAnn == null ? null : originAnn.id();

                for (Dependency dep : origin.getDirectDependenciesFromSelf()) {
                    JavaClass target = dep.getTargetClass();
                    if (!needBoundedContext().test(target)) {
                        continue;
                    }

                    Optional<JavaPackage> optionalBoundedContextPkg = RecursivePackageRules
                            .findPackageOrAncestorAnnotatedWith(BoundedContext.class, classes, target);

                    if (optionalBoundedContextPkg.isEmpty()) {
                        events.add(SimpleConditionEvent.violated(origin, String.format(
                                "Class <%s> in context '%s' must always depends on classes in a package annotated @BoundedContext,"
                                        + " but depends on <%s> that is not in a @BoundedContext",
                                origin.getName(), originId, target.getName())));
                    } else {
                        JavaPackage targetPkg = optionalBoundedContextPkg.get();
                        BoundedContext targetAnn = targetPkg.getAnnotationOfType(BoundedContext.class);
                        String targetId = targetAnn == null ? null : targetAnn.id();

                        // only consider cross-context dependencies
                        if (originId != null && targetId != null && !originId.equals(targetId)) {
                            // check allowed exceptions
                            boolean allowed = target.isAnnotatedWith(InterfaceLayer.class) || RecursivePackageRules.findPackageOrAncestorAnnotatedWith(InterfaceLayer.class, classes, target).isPresent()
                                    || target.isAnnotatedWith(DomainEvent.class) || RecursivePackageRules.findPackageOrAncestorAnnotatedWith(DomainEvent.class, classes, target).isPresent()
                                    || target.isAnnotatedWith(ApplicationLayer.class) || RecursivePackageRules.findPackageOrAncestorAnnotatedWith(ApplicationLayer.class, classes, target).isPresent();

                            if (!allowed) {
                                events.add(SimpleConditionEvent.violated(origin,
                                        String.format(
                                                "Class <%s> in context '%s' must not depend on <%s> in context '%s'",
                                                origin.getName(), originId, target.getName(), targetId)));
                            }
                        }
                    }
                }
            }
        };
    }

    /**
     * All the annotations / interfaces that mandate a @BoundedContext.
     */
    public static DescribedPredicate<JavaClass> needBoundedContext() {
        return DescribedPredicate
                .describe("be a JMolecules‑ or JPA‑ or Spring‑Data‑repository - or Spring stereotype classes",
                        DescribedPredicate.or(annotatedWith(DomainLayer.class), annotatedWith(ApplicationLayer.class),
                                annotatedWith(InfrastructureLayer.class), annotatedWith(InterfaceLayer.class),

                                annotatedWith(AggregateRoot.class), annotatedWith(Entity.class),
                                annotatedWith(ValueObject.class), annotatedWith(Repository.class),
                                annotatedWith(Service.class), annotatedWith(Module.class), annotatedWith(Factory.class),

                                annotatedWith(UseCase.class), annotatedWith(Policy.class),

                                annotatedWith(DomainEvent.class),

                                annotatedWith(jakarta.persistence.Entity.class), annotatedWith(Embeddable.class),
                                annotatedWith(MappedSuperclass.class),

                                annotatedWith(Component.class),
                                annotatedWith(org.springframework.stereotype.Repository.class),
                                annotatedWith(org.springframework.stereotype.Service.class),
                                annotatedWith(org.springframework.stereotype.Controller.class),
                                annotatedWith(org.springframework.web.bind.annotation.RestController.class),

                                assignableTo(JpaRepository.class), assignableTo(CrudRepository.class),
                                assignableTo(PagingAndSortingRepository.class)))
                .and(DescribedPredicate.not(DescribedPredicate.or(type(DomainLayer.class), type(ApplicationLayer.class),
                        type(InfrastructureLayer.class), type(InterfaceLayer.class),

                        type(AggregateRoot.class), type(Entity.class), type(ValueObject.class), type(Repository.class),
                        type(Service.class), type(Module.class), type(Factory.class),

                        type(UseCase.class), type(Policy.class),

                        type(DomainEvent.class),

                        type(jakarta.persistence.Entity.class), type(Embeddable.class), type(MappedSuperclass.class),

                        type(Component.class), type(org.springframework.stereotype.Repository.class),
                        type(org.springframework.stereotype.Service.class),
                        type(org.springframework.stereotype.Controller.class),
                        type(org.springframework.web.bind.annotation.RestController.class),

                        type(JpaRepository.class), type(CrudRepository.class), type(PagingAndSortingRepository.class))));
    }

    /**
     * Condition: must live in a package annotated with @BoundedContext.
     */
    public static ArchCondition<JavaClass> resideInBoundedContext(JavaClasses classes) {
        return new ArchCondition<>("reside in a package or ancestor package annotated with @BoundedContext") {
            @Override
            public void check(JavaClass javaClass, ConditionEvents events) {
                if (!isInBoundedContext.apply(classes).test(javaClass)) {
                    JavaPackage pkg = javaClass.getPackage();
                    events.add(SimpleConditionEvent.violated(javaClass, String.format(
                            "Component class <%s> matches one of the JMolecules/JPA/Spring‑Data stereotypes but neither package <%s> nor any ancestor is annotated @BoundedContext",
                            javaClass.getName(), pkg.getName())));
                }
            }
        };
    }

}
